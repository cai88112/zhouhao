package com.ruoyi.web.controller.websocket;

import com.ruoyi.common.core.domain.entity.DayWork;
import com.ruoyi.common.core.domain.entity.SysDevice;
import com.ruoyi.common.core.domain.entity.WorkData;
import com.ruoyi.system.mapper.SysDeviceWorkDataMapper;
import com.ruoyi.system.mapper.SysDeviceWorkMapper;
import com.ruoyi.system.service.ISysDeviceService;
import com.ruoyi.web.controller.common.HelloPacket;
import com.ruoyi.web.controller.tool.HexUtil;
import com.ruoyi.web.controller.tool.MsgService;
import com.ruoyi.web.controller.tool.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.Date;

/**
 * @author tanyaowu
 */
@Component
public class HelloServerAioHandler implements ServerAioHandler {
	@Autowired
	private ISysDeviceService deviceService;
	@Autowired
	private SysDeviceWorkMapper workMapper;
	@Autowired
	private SysDeviceWorkDataMapper workDataMapper;

	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	@Override
	public HelloPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) {
		//提醒：buffer的开始位置并不一定是0，应用需要从buffer.position()开始读取数据
		//收到的数据组不了业务包，则返回null以告诉框架数据不够
//		byte[]  bytes = new byte[buffer.capacity()];
//		buffer.position(position);
//		buffer.get(bytes);
//		byte []b = Arrays.copyOfRange(bytes,0,readableLength);
//
//		System.out.println("解析:"+bytesToHexString(b));
//		if (readableLength < HelloPacket.HEADER_LENGHT) {
//			return null;
//		}
//
//		//读取消息体的长度
//		int bodyLength = buffer.getInt();
//
//		//数据不正确，则抛出AioDecodeException异常
//		if (bodyLength < 0) {
//			throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
//		}
//
//		//计算本次需要的数据长度
//		int neededLength = HelloPacket.HEADER_LENGHT + bodyLength;
//		//收到的数据是否足够组包
//		int isDataEnough = readableLength - neededLength;
//		// 不够消息体长度(剩下的buffe组不了消息体)
//		if (isDataEnough < 0) {
//			return null;
//		} else //组包成功
//		{
//			HelloPacket imPacket = new HelloPacket();
//			if (bodyLength > 0) {
//				byte[] dst = new byte[bodyLength];
//				buffer.get(dst);
//				imPacket.setBody(dst);
//			}
//			return imPacket;
//		}
		HelloPacket imPacket = new HelloPacket();
		try {
			byte[] dst = new byte[readableLength];
			buffer.get(dst);
			imPacket.setBody(dst);
			return imPacket;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	@Override
	public ByteBuffer encode(Packet packet, TioConfig groupContext, ChannelContext channelContext) {
		HelloPacket helloPacket = (HelloPacket) packet;
		byte[] body = helloPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		//bytebuffer的总长度是 = 消息头的长度 + 消息体的长度
		//int allLen = HelloPacket.HEADER_LENGHT + bodyLen;
		//创建一个新的bytebuffer
		ByteBuffer buffer = ByteBuffer.allocate(bodyLen);
		//设置字节序
		buffer.order(groupContext.getByteOrder());

		//写入消息头----消息头的内容就是消息体的长度
		//buffer.putInt(bodyLen);

		//写入消息体
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}


	/**
	 * 处理消息
	 */
	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		HelloPacket helloPacket = (HelloPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			String hex = HexUtil.bytesToHexString(body);
			System.out.println("body:" + hex);
			if (!hex.startsWith("FA")) {
				return;
			}
			if (!HexUtil.validData(hex)) {
				return;
			}
			String code = hex.substring(6, 8);
			if (code.equals("30")) {
				String id = hex.substring(8, 20);
				Tio.bindUser(channelContext, id);
				int status = HexUtil.hex2Int(hex.substring(20, 22));//机器状态
				int workType = HexUtil.hex2Int(hex.substring(22, 24));//勾类型
				int password = HexUtil.hex2Int(hex.substring(24, 26));//密码是否更新
				int online = HexUtil.hex2Int(hex.substring(26, 28));//工作模式
				HelloPacket resppacket = new HelloPacket();
				SysDevice device = deviceService.findByNum(id);
				String pwd = StringUtil.generateRandomPassword();
				if (device != null) {
					device.setStatus(status == 1);
					device.setWorkNum(workType);
					device.setPasswordAuto(password == 1);
					device.setOnline(online == 1);
					Date currentDate = new Date();
					Calendar operationCalendar = Calendar.getInstance();
					operationCalendar.setTime(device.getPwdTime());
					int operationHour = operationCalendar.get(Calendar.HOUR_OF_DAY);
					Calendar currentCalendar = Calendar.getInstance();
					currentCalendar.setTime(currentDate);
					// 获取当前时间的小时和分钟
					int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
					int currentMinute = currentCalendar.get(Calendar.MINUTE);

					boolean isSameDay = currentCalendar.get(Calendar.DAY_OF_YEAR) == operationCalendar.get(Calendar.DAY_OF_YEAR);
					if (!isSameDay && currentHour >= 0 && currentMinute >= 0) {
						device.setPwd(pwd);
						device.setPwdTime(new Date());
					}else{
						pwd = device.getPwd();
					}
					deviceService.updateDevice(device);
				}
				resppacket.setBody(MsgService.command_30(pwd));
				Tio.send(channelContext, resppacket);
			} else {
				commandDeal(hex, channelContext.userid);
			}
		}
	}

	public void commandDeal(String hex, String clientId) {
		String code = hex.substring(6, 8);
		SysDevice device = deviceService.findByNum(clientId);
		switch (Integer.parseInt(code)) {
			case 32:
				int workNum = HexUtil.hex2Int(hex.substring(8, 10));
				WorkData workData = workDataMapper.findFirstByDeviceIdAndWorkNum(device.getId(),workNum);
				if(workData == null){
					workData = new WorkData();
				}
				workData.setDataHex(hex);
				workData.setWorkNum(workNum);
				workData.setDeviceId(device.getId());
				workDataMapper.save(workData);
				break;
			case 34:
				String start_date = hex.substring(20, 32);
				String end_date = hex.substring(32, 44);
				String type = hex.substring(44, 46);
				String volume = hex.substring(46, 50);
				DayWork dayWork = new DayWork();
				dayWork.setDeviceId(device.getId());
				dayWork.setWorkType(type);
				dayWork.setEndDate(end_date);
				dayWork.setStartDate(start_date);
				dayWork.setVolume(HexUtil.hex2Int(volume)+"");
				workMapper.save(dayWork);
				MsgService.command_34(clientId,start_date);
				break;
			default:
				break;
		}
	}
}
