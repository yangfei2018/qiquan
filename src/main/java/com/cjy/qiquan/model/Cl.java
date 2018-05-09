package com.cjy.qiquan.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cl {

	private String szlable;
	
	private double fnewprice;

	
	public Cl() {
		
	}
	
	
	public Cl(ResultSet res)throws SQLException{
		szlable = res.getString("szlable");
		fnewprice = res.getDouble("fnewprice");
	}
	
	public String getSzlable() {
		return szlable;
	}

	public void setSzlable(String szlable) {
		this.szlable = szlable;
	}

	public double getFnewprice() {
		return fnewprice;
	}

	public void setFnewprice(double fnewprice) {
		this.fnewprice = fnewprice;
	}
	
	
	
}
