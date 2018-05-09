package com.cjy.qiquan.service;

public abstract class DBHandler implements Runnable{

	protected abstract void impl();
	
	protected abstract int id();
	
	@Override
	public void run() {

		try{
			impl();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
