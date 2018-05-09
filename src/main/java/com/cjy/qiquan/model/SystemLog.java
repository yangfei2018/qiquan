package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.DateFormater;

public class SystemLog {

	private int id;

	private String body;

	private int mainId;

	private int belongUserId;// 属于用户id

	private Date createTime;

	private String readTime;

	public SystemLog() {

	}

	public SystemLog(ResultSet res) throws SQLException {
		this.id = res.getInt("f_id");
		this.body = res.getString("f_body");
		this.mainId = res.getInt("f_mainId");
		this.belongUserId = res.getInt("f_belongUserId");
		this.createTime = new Date(res.getTimestamp("f_createTime").getTime());
		this.readTime = res.getString("f_readTime");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return CommonUtils.defaultNullString(body);
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getMainId() {
		return mainId;
	}

	public void setMainId(int mainId) {
		this.mainId = mainId;
	}

	public int getBelongUserId() {
		return belongUserId;
	}

	public void setBelongUserId(int belongUserId) {
		this.belongUserId = belongUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReadTime() {
		return CommonUtils.defaultNullString(readTime);
	}

	public String getCreateFormat() {
		return DateFormater.viewDateStandardFormat(getCreateTime());
	}

	public void setReadTime(String readTime) {
		this.readTime = readTime;
	}

}
