package com.cjy.qiquan.po;


public class ErrorVo {
	
	private String state;

	private String desc;

	
	public ErrorVo(){
		
	}
	
	public ErrorVo(String state,String desc){
		this.state = state;
		this.desc = desc;
	}
	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}
