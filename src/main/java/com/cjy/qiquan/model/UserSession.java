package com.cjy.qiquan.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.po.UserTicket;

public class UserSession implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String token;
	
	private int userId;
	
	private String name;
	
	private String role;

	private int status;
	
	public UserSession(){
		
	}
	
	
	public UserSession(ResultSet res) throws SQLException {
		super();
		this.userId = res.getInt("f_id");
		this.name = res.getString("f_name");
		this.role = res.getString("f_role");
		this.status = res.getInt("f_status");
	}
	
	public UserSession(String token, int userId, String name, String role,int status) {
		super();
		this.token = token;
		this.userId = userId;
		this.name = name;
		this.role = role;
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	
	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public UserTicket buildTicket(){
		return new UserTicket.Builder()
		.setUserId(this.getUserId()).setUserName(this.getName()).setRole(this.getRole())
		.build();
	}
}
