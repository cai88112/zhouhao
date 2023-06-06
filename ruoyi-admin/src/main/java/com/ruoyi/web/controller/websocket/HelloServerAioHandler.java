package com.ruoyi.web.controller.websocket;

import com.ruoyi.web.controller.common.HelloPacket;
import com.ruoyi.web.controller.tool.HexUtil;
import com.ruoyi.web.controller.tool.MsgService;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;

/**
 * @author tanyaowu
 */
public class HelloServerAioHandler implements ServerAioHandler {
//	private DeviceService deviceService = new DeviceService();
	/**
	 * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
	public HelloPacket decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)  {
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
		}catch (Exception e){
			return null;
		}
	}

	/**
	 * 编码：把业务消息包编码为可以发送的ByteBuffer
	 * 总的消息结构：消息头 + 消息体
	 * 消息头结构：    4个字节，存储消息体的长度
	 * 消息体结构：   对象的json串的byte[]
	 */
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
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		HelloPacket helloPacket = (HelloPacket) packet;
		byte[] body = helloPacket.getBody();
		if (body != null) {
			String hex= HexUtil.bytesToHexString(body);
			System.out.println("body:"+hex);
//			if (!hex.startsWith("FA")) {
//				return;
//			}
//			if (!HexUtil.validData(hex)) {
//				return;
//			}
//			String code = hex.substring(6,8);
//
//			if(code.equals("30")) {
//				String id = hex.substring(8,20);
//				Tio.bindUser(channelContext,id);
//				System.out.println("--------当前userid:"+channelContext.userid);
//				HelloPacket resppacket = new HelloPacket();
//				resppacket.setBody(MsgService.command_30(channelContext.userid));
//				Tio.send(channelContext, resppacket);
//				String state = hex.substring(20,22);
//				String expireD = hex.substring(22,36);
//				String inputState = hex.substring(52,60);
////				DeviceService deviceService = new DeviceService();
////				deviceService.upstate(id,inputState,state,expireD);
//				//Tio.bindUser(channelContext,id);
//
//			}else{
//				commandDeal(hex,channelContext.userid);
//			}
		}
		return;
	}

	public void commandDeal(String hex,String clientId){
		String code = hex.substring(6,8);
		int result = 0;
		switch (Integer.parseInt(code)){
			case 31:
				result = HexUtil.hex2Int(hex.substring(10,12));
				if(result == 1){
					//MsgService.command_31(clientId,HexUtil.hex2Int(hex.substring(8,10)));
					String channel = HexUtil.hex2Int(hex.substring(8,10))+"";
					if(channel.length() == 1){
						channel = "0"+channel;
					}
//					new UserService().updateEQstate(channel,clientId);
				}
				break;
			case 32:
				result = HexUtil.hex2Int(hex.substring(12,14));
				if(result == 1){//成功
					//MsgService.command_32(clientId,HexUtil.hex2Int(hex.substring(8,10)),HexUtil.hex2Int(hex.substring(10,12)));
					String channel = HexUtil.hex2Int(hex.substring(8,10))+"";
					if(channel.length() == 1){
						channel = "0"+channel;
					}
//					new UserService().updateEQstate(channel,clientId);
				}
				break;
			case 33:
				result = HexUtil.hex2Int(hex.substring(8,10));
				if(result == 0){
//					Device device = Aop.get(DeviceService.class).findByNum(clientId);
//					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
//					MsgService.command_33(clientId,format.format(device.getExpireD()));
				}
				break;
			case 50:
				result = HexUtil.hex2Int(hex.substring(8,10));
				if(result == 0){
//					Device device = Aop.get(DeviceService.class).findByNum(clientId);
//					ConfigService.inputConfig(device.getId().intValue());
				}else{
//					Device device = deviceService.findByNum(clientId);
//					ConfigService.outputConfig(device.getId().intValue());
//					ConfigService.upState(1,device.getId().intValue(),1);
				}
				break;
			case 51:
				result = HexUtil.hex2Int(hex.substring(8,10));
				if(result == 0){
//					Device device = Aop.get(DeviceService.class).findByNum(clientId);
//					ConfigService.outputConfig(device.getId().intValue());
				}else{
//					Device device = deviceService.findByNum(clientId);
//					ConfigService.scenarioConfig(device.getId().intValue(),"100");
//					ConfigService.upState(2,device.getId().intValue(),1);
				}
				break;
			case 52:
				result = HexUtil.hex2Int(hex.substring(10,12));
				int num = HexUtil.hex2Int(hex.substring(8,10));
				if(result == 1 && num<110){
//					Device device = deviceService.findByNum(clientId);
//					ConfigService.scenarioConfig(device.getId().intValue(),(num+1)+"");
//					ConfigService.upState(3,device.getId().intValue(),1);
				}
				break;
			case 54:
				int address = HexUtil.hex2Int(hex.substring(8,10));//地址 TODO 多地址时用
				int idx = HexUtil.hex2Int(hex.substring(10,12));//次序
				result = HexUtil.hex2Int(hex.substring(12,14));
				if(result == 1){
//					Device device = deviceService.findByNum(clientId);
//					int rs = deviceService.getRsState(device.getId().intValue());
					if(idx < 8) {
//						ConfigService.rssConfig(device.getId().intValue(), idx + 1);
					}
//					deviceService.upRsState(device.getId().intValue(), idx + 1);
				}
				break;
			default:
				break;
		}
	}

}
