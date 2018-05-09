package com.cjy.qiquan.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DBService {

	private static DBService instance = new DBService();
	private ExecutorService executor1 = Executors.newSingleThreadExecutor();
	private ExecutorService executor2 = Executors.newSingleThreadExecutor();
	
	
	public void put(DBHandler event) {
		if (event.id()%2 == 0){
			executor1.execute(event);
		}else{
			executor2.execute(event);
		}
		
	}
	
	public static DBService instance() {
		if (instance == null) {
			synchronized (DBService.class) {
				instance = new DBService();
			}
		}
		return instance;
	}
}
