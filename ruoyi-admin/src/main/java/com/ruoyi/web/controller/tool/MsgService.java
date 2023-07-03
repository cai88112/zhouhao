package com.ruoyi.web.controller.tool;


import com.ruoyi.web.controller.common.HelloPacket;
import com.ruoyi.web.controller.pojo.WorkDataResponse;
import com.ruoyi.web.controller.pojo.WorkFloor;
import com.ruoyi.web.controller.websocket.HelloServerStarter;
import org.tio.core.Tio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CAI
 * @desc 消息交互类
 * @create 2020-09-12 15:00
 **/
public class MsgService {
//	private static DeviceService deviceService = new DeviceService();
	public static void sendMsg(byte[] msg,String clientId) throws Exception{
		HelloPacket packet = new HelloPacket();
		packet.setBody(msg);
		Tio.sendToUser(HelloServerStarter.serverTioConfig,clientId,packet);
	}

	/**
	 * 发送心跳
	 */
	public static byte[] command_30(String password){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(14,2);
		String date = StringUtil.getCurrentTimeString();
		sb.append(hexLength);
		sb.append("30");
		sb.append(date);
		String pwd = getPwd(password);
		sb.append(pwd);
		sb.append(HexUtil.getValidData(hexLength,"30"+date+pwd));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		return bytes;
	}
	private static String getPwd(String pwd){
		StringBuilder sb = new StringBuilder();
		for(int i = 0;i<6;i++){
			sb.append(HexUtil.int2Hex(Integer.parseInt(pwd.substring(i,i+1)),1));
		}
		return sb.toString();
	}
	/**
	 * 云端对控制板发送启动流程动作命令（31H）
	 * @param clientId 设备id
	 */
	public static void command_31(String clientId,boolean password,boolean online){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(3,2);
		String pwd = password == false ? "00":"01";
		String workOnline = online == false ? "00":"01";
		sb.append(hexLength);
		sb.append("31");
		sb.append(pwd);
		sb.append(workOnline);
		sb.append(HexUtil.getValidData(hexLength,"31"+pwd+workOnline));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *云端对控制板发送启动流程动作命令（32H）
	 * @param clientId 设备id
	 */
	public static void command_32(String clientId,int workNum){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(2,2);
		String channelHex = HexUtil.int2Hex(workNum,1);
		sb.append(hexLength);
		sb.append("32");
		sb.append(channelHex);
		sb.append(HexUtil.getValidData(hexLength,"32"+channelHex));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public static WorkDataResponse command_32_parse(String hex){
		WorkDataResponse workDataResponse = new WorkDataResponse();
		int time = HexUtil.hex2Int(hex.substring(10,12));
		int speed = HexUtil.hex2Int(hex.substring(12,20));
		int end = HexUtil.hexToDecimal(hex.substring(20,28));
		int dentisy = HexUtil.hex2Int(hex.substring(28,32));
		int floors = HexUtil.hex2Int(hex.substring(32,34));
		workDataResponse.setTime(BigDecimal.valueOf(time).divide(BigDecimal.TEN).doubleValue());
		workDataResponse.setSpeed(speed/10000);
		workDataResponse.setEnd(end/10000);
		workDataResponse.setDensity(dentisy);
		workDataResponse.setFloors(floors);
		List<WorkFloor> workFloors = new ArrayList<>();
		int startsub = 34;
		for(int i=1;i<=8;i++){
			WorkFloor workFloor = new WorkFloor();
			int start =  HexUtil.hexToDecimal(hex.substring(startsub,startsub+8));
			startsub += 8;
			int floorSpeed =  HexUtil.hex2Int(hex.substring(startsub,startsub+8));
			startsub += 8;
			int floorEnd =  HexUtil.hexToDecimal(hex.substring(startsub,startsub+8));
			startsub += 8;
			int floorDentisy =  HexUtil.hex2Int(hex.substring(startsub,startsub+4));
			startsub += 4;
			workFloor.setFloor(i);
			workFloor.setStart(start/10000);
			workFloor.setEnd(floorEnd/10000);
			workFloor.setDensity(floorDentisy);
			workFloor.setSpeed(floorSpeed/10000);
			workFloors.add(workFloor);
		}
		workDataResponse.setList(workFloors);
		return workDataResponse;
	}
	/**
	 * 云端对控制板设定系统到期时间命令（33H）
	 */

	public static void command_33(String clientId,int workNum,WorkDataResponse workDataResponse){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(14+workDataResponse.getList().size()*14,2);
		sb.append(hexLength);
		sb.append("33");
		String wn = HexUtil.int2Hex(workNum,1);
		sb.append(wn);
		String time = HexUtil.int2Hex(BigDecimal.valueOf(workDataResponse.getTime()).multiply(BigDecimal.TEN).intValue(),1);
		sb.append(time);
		String speed = HexUtil.int2Hex(workDataResponse.getSpeed()*10000,4);
		String end = HexUtil.int2Hex(workDataResponse.getEnd()*10000,4);
		String density = HexUtil.int2Hex(workDataResponse.getDensity(),2);
		String floor = HexUtil.int2Hex(workDataResponse.getFloors(),1);
		sb.append(speed);
		sb.append(end);
		sb.append(density);
		sb.append(floor);
		for(WorkFloor workFloor : workDataResponse.getList()){
			String start = HexUtil.int2Hex(workFloor.getStart()*10000,4);
			String sp = HexUtil.int2Hex(workFloor.getSpeed()*10000,4);
			String ed = HexUtil.int2Hex(workFloor.getEnd()*10000,4);
			String dt = HexUtil.int2Hex(workFloor.getDensity(),2);
			sb.append(start);
			sb.append(sp);
			sb.append(ed);
			sb.append(dt);
		}
		sb.append(HexUtil.getValidData(hexLength,sb.substring(6)));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}


	public static void command_34(String clientId,String date){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(8,2);
		sb.append(hexLength);
		sb.append("3401");
		sb.append(date);
		sb.append(HexUtil.getValidData(hexLength,"3401"+date));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
