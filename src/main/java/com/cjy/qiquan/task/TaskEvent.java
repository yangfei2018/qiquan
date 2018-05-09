package com.cjy.qiquan.task;

public abstract class TaskEvent implements Runnable {

	
	protected abstract void impl();
	
	@Override
	public void run() {

		try{
			impl();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
