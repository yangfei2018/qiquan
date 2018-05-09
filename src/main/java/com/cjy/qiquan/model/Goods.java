package com.cjy.qiquan.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;

public class Goods implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1919449539297628340L;

	private int id;
	
	private String name;
	
	private String code;
	
	private int categoryId;

	private double feilv_15;
	
	private double feilv_30;
	
	private String feilv_15_time;
	
	private String feilv_30_time;
	
	private int unit;
	
	private int min_shou;
	
	private String lastUpdateTime;
	
	private String danwei;
	
	public Goods() {
		
	}
	
	
	public Goods(ResultSet res)throws SQLException{
		id = res.getInt("f_id");
		name = res.getString("f_name");
		categoryId  =res.getInt("f_categoryId");
		code = res.getString("f_code");
		feilv_15 = res.getDouble("f_feilv_15");
		feilv_30 = res.getDouble("f_feilv_30");
		lastUpdateTime = res.getString("f_lastupdateTime");
		unit = res.getInt("f_unit");
		feilv_15_time = res.getString("f_feilv_15_time");
		feilv_30_time = res.getString("f_feilv_30_time");
		min_shou = res.getInt("f_min_shou");
		danwei = res.getString("f_danwei");
	}
	
	
	
	
	public String getDanwei() {
		return CommonUtils.defaultNullString(danwei);
	}


	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
	
	public String getCode() {
		return CommonUtils.defaultNullString(code);
	}


	public void setCode(String code) {
		this.code = code;
	}

	
	

	public String getFeilv_15_time() {
		return CommonUtils.defaultNullString(feilv_15_time);
	}


	public void setFeilv_15_time(String feilv_15_time) {
		this.feilv_15_time = feilv_15_time;
	}


	public String getFeilv_30_time() {
		return CommonUtils.defaultNullString(feilv_30_time);
	}


	public void setFeilv_30_time(String feilv_30_time) {
		this.feilv_30_time = feilv_30_time;
	}


	public int getUnit() {
		return unit;
	}


	public void setUnit(int unit) {
		this.unit = unit;
	}


	public String getCategoryName() {
		return Constant.GoodsCategory.get(this.categoryId)!=null?Constant.GoodsCategory.get(this.categoryId).getName():"";
	}


	public double getFeilv_15() {
		return feilv_15;
	}


	public void setFeilv_15(double feilv_15) {
		this.feilv_15 = feilv_15;
	}


	public double getFeilv_30() {
		return feilv_30;
	}


	public void setFeilv_30(double feilv_30) {
		this.feilv_30 = feilv_30;
	}


	public String getLastUpdateTime() {
		return CommonUtils.defaultNullString(lastUpdateTime);
	}


	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public int getMin_shou() {
		return min_shou;
	}


	public void setMin_shou(int min_shou) {
		this.min_shou = min_shou;
	}
	
	
	
}