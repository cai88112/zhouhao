package com.ruoyi.web.controller.websocket;

import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioListener;

/**
 * @author CAI
 * @desc
 * @create 2020-08-10 15:12
 **/
public class HelloListener implements ServerAioListener {
	public boolean onHeartbeatTimeout(ChannelContext channelContext, Long aLong, int i) {
		System.out.println("listener::心跳..."+aLong+"====="+channelContext.userid);
		return true;
	}

	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		if(isConnected){
			System.out.println("listener::连接成功...");
		}

	}

	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int i) throws Exception {
	}


	public void onAfterReceivedBytes(ChannelContext channelContext, int i) throws Exception {
	}

	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean b) throws Exception {
	}

	public void onAfterHandled(ChannelContext channelContext, Packet packet, long l) throws Exception {
	}

	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String s, boolean b) throws Exception {
		System.out.println("listener::要断开连接了..." + (throwable != null ? throwable.getMessage() : ""));
	}
}
