package com.cjy.qiquan.service;

import org.springframework.stereotype.Service;

import com.cjy.qiquan.utils.SpringApplicationContext;
import com.ws.tool.ReadDataFromWSZFile;
import com.ws.tool.ReceiveFromSocket;

@Service
public class WsSocketService {

	public static WsSocketService instance() {
		return SpringApplicationContext.getBean("wsSocketService");
	}

	public void startServer() {
		Thread receiveFromSockThread = new Thread(new ReceiveFromSocket());
		receiveFromSockThread.start();
		Thread readDataFromWSZThread = new Thread(new ReadDataFromWSZFile());
		readDataFromWSZThread.start();
	}

}
