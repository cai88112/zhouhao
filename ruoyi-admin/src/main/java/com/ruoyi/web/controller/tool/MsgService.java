package com.ruoyi.web.controller.tool;


import com.ruoyi.web.controller.common.HelloPacket;
import com.ruoyi.web.controller.websocket.HelloServerStarter;
import org.tio.core.Tio;

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
	public static byte[] command_30(String clientId){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(8,2);
		String date = StringUtil.getCurrentTimeString();
		sb.append(hexLength);
		sb.append("30");
		sb.append(date);
		sb.append(HexUtil.getValidData(hexLength,"30"+date));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
//		Device device = deviceService.findByNum(clientId);
//		if(device.getState() == 0){
//			deviceService.upstate(device);
//		}
		return bytes;
	}
	/**
	 * 云端对控制板发送启动流程动作命令（31H）
	 * @param clientId 设备id
	 * @param channel 通道
	 */
	public static void command_31(String clientId,int channel){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(2,2);
		String channelHex = HexUtil.int2Hex(channel,1);
		sb.append(hexLength);
		sb.append("31");
		sb.append(channelHex);
		sb.append(HexUtil.getValidData(hexLength,"31"+channelHex));
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
	 * @param channel 通道
	 * @param state 0关 1开
	 */
	public static void command_32(String clientId,int channel,int state){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(3,2);
		String channelHex = HexUtil.int2Hex(channel,1);
		String stateHex = HexUtil.int2Hex(state,1);
		sb.append(hexLength);
		sb.append("32");
		sb.append(channelHex);
		sb.append(stateHex);
		sb.append(HexUtil.getValidData(hexLength,"32"+channelHex+stateHex));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 云端对控制板设定系统到期时间命令（33H）
	 */
	public static void command_33(String clientId,String date){
		StringBuilder sb = new StringBuilder();
		sb.append("FA");
		String hexLength = HexUtil.int2Hex(8,2);
		sb.append(hexLength);
		sb.append("33");
		sb.append(date);
		sb.append(HexUtil.getValidData(hexLength,"33"+date));
		byte[] bytes = StringUtil.hexStrTobytes(sb.toString());
		try {
			sendMsg(bytes, clientId);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
