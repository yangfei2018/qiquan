package com.cjy.qiquan.utils;


import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import com.cjy.qiquan.utils.properties.Prop;
import com.cjy.qiquan.utils.properties.Proper;

public class SecurityConst implements Observer{
	
	final static Logger Out = Logger.getLogger(SecurityConst.class);
	
	public static String gmName;
	public static String gmPassword;
	
	
	static {
		Prop constants = null;
		try {
			constants = Proper.use("security.properties");
		} catch (Exception e) {
			Out.warn(e.getMessage());
		}
		if (constants == null) {
			gmName = "rest";
			gmPassword = "rest0001";
		} else {
			gmName = constants.get("app.gm.name");
			gmPassword = constants.get("app.gm.password");
		}
	}
	
	
	
	@Override
	public void update(Observable o, Object arg) {
		Prop constants = null;
		try {
			constants = Proper.use("security.properties");
			if (constants!=null) {
				gmName = constants.get("app.gm.name");
				gmPassword = constants.get("app.gm.password");
			}
			System.out.println("security.properties数据已更新:");
		} catch (Exception e) {
			Out.warn(e.getMessage());
		}
	}
}
