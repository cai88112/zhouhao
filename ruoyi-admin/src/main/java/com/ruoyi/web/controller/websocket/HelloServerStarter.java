package com.ruoyi.web.controller.websocket;


import com.ruoyi.web.controller.tool.SpringUtil;
import org.springframework.stereotype.Component;
import org.tio.server.ServerTioConfig;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class HelloServerStarter  {
	//handler, 包括编码、解码、消息处理
	public static ServerAioHandler aioHandler = new HelloServerAioHandler();

	//事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
	public static ServerAioListener aioListener = new HelloListener();

	//一组连接共用的上下文对象
	public static ServerTioConfig serverTioConfig = null;

	//tioServer对象
	public static TioServer tioServer = null;

	//有时候需要绑定ip，不需要则null
	public static String serverIp = null;

	//监听的端口
	public  static int serverPort = 6789;

	public  static long timeout = 5000;

	private static void populateServerTioConfig() {
		aioHandler = SpringUtil.getBean(HelloServerAioHandler.class);
		serverTioConfig  = new ServerTioConfig("tio-server", aioHandler, aioListener);
		tioServer = new TioServer(serverTioConfig);
	}

	/**
	 * 启动程序入口
	 */
	@PostConstruct
	public  void init() throws IOException {
		populateServerTioConfig();
		serverTioConfig.setHeartbeatTimeout(timeout);
		tioServer.start(serverIp, serverPort);
	}

	public boolean start() {
		//serverTioConfig.setHeartbeatTimeout(Const.TIMEOUT);
		try {
			tioServer.start(serverIp, serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean stop() {
		return false;
	}
}