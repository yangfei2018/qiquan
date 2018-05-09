package com.cloopen.rest.sdk;

import java.util.HashMap;
import java.util.Set;

public class CCPApi {
	static CCPRestSDK restAPI;
	static {
		restAPI = new CCPRestSDK();
//		restAPI.init("sandboxapp.cloopen.com", "8883");//沙盒环境
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("aaf98f894d9e9c40014da3f708ba0332", "7388d475528e49febb7fadfea9351e4f");
		restAPI.setAppId("8a48b5514d9eb90e014da41a247f0330");// 初始化应用ID
	}
	
	
	
	public static void landingCall(final String callPhone,final String message,final String userData){
		HashMap<String, Object> result = null;

		result = restAPI.landingCall(callPhone, "", message, "01086210120", "1", "http://api1.ejiarens.com:8080/ccp/notify_url", userData,"", "", "", "", "");

		System.out.println("SDKTestLandingCall result=" + result);
		
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
		
	}
}
