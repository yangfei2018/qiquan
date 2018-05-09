package com.cjy.qiquan.po;

import java.io.Serializable;

public class UserTicket implements Serializable{

	/**
	 * 用户
	 */
	private static final long serialVersionUID = 1L;
	
	private String token;
	
	private int userId;
	
	private String userName;
	
	private String role;
	
	private int status;
	
	private UserTicket(Builder b){
		userId = b.userId;
		userName = b.userName;
		role = b.role;
		token = b.token;
		status=b.status;
	}
	
	
	public static class Builder{
		private int userId;
		
		private String userName;
		
		private String role;
		
		private String token;
		
		private int status;
		
		public Builder(){
			super();
		}
		
		public Builder setUserId(int userId){
			this.userId = userId;
			return this;
		}
		
		public Builder setUserName(String userName){
			this.userName = userName;
			return this;
		}
		
		public Builder setRole(String role){
			this.role = role;
			return this;
		}
		
		public Builder setToken(String token){
			this.token = token;
			return this;
		}
		
		public Builder setStatus(int status){
			this.status = status;
			return this;
		}
		
		public UserTicket build(){
			return new UserTicket(this);
		}
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
