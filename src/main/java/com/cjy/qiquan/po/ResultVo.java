package com.cjy.qiquan.po;

import java.io.Serializable;



public class ResultVo<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean result;
	
	private T data;
	
	public ResultVo(Builder<T> b){
		this.result = b.result;
		this.data = b.data;
	}
	
	
	public static class Builder<T>{
		private boolean result;
		private T data;
		
		public Builder(boolean result){
			this.result = result;
		}
		
		public Builder(){
		}
		
		public Builder<T> setData(T data){
			this.data = data;
			return this;
		}
		
		public Builder<T> setResult(boolean result){
			this.result = result;
			return this;
		}
		
		public ResultVo<T> build(){
			return new ResultVo<T>(this);
		}
		
	}
	
	
	public ResultVo(boolean result) {
		super();
		this.result = result;
	}

	
	public T getData() {
		return data;
	}

	public boolean isResult() {
		return result;
	}
	
}