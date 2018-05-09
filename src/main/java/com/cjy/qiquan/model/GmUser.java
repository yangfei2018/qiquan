package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.Constant.GM_ROLE;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GmUser extends UserSession {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3120685839725515081L;

	@JsonIgnore
	private String password_hash;

	private int isAdmin;

	private int positionId;// 职位

	private String realName;// 姓名

	private int status;

	public GmUser() {
		super();
	}

	public GmUser(ResultSet res) throws SQLException {
		super(res);
		password_hash = res.getString("f_password_hash");
		realName = res.getString("f_realName");
		positionId = res.getInt("f_positionId");
		status = res.getInt("f_status");
		isAdmin = res.getInt("f_isAdmin");
	}

	public String getPassword_hash() {
		return password_hash;
	}

	public void setPassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public GM_ROLE getPosition() {
		return Constant.GM_ROLE.get(getPositionId());
	}
	
	
	public String getPositionName() {
		if (getPosition()!=null) {
			return getPosition().getValue();
		}
		return "";
	}

}
