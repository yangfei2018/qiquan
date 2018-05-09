package com.cloopen.rest.sdk;

public class SDKSendTemplateSMS {

	public static CCPRestSmsSDK restAPI;

	static {

		restAPI = new CCPRestSmsSDK();
		// 初始化SDK
		// ******************************注释*********************************************
		// *初始化服务器地址和端口 *
		// *沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
		// *生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883"); *
		// *******************************************************************************
		restAPI.init("app.cloopen.com", "8883");
		restAPI.setAccount("8a216da860bad76d0160e3b474b510ba", "12cfed57964942f580cc509a55b82fcc");
		restAPI.setAppId("8a216da860bad76d0160e3b4751010c1");// 初始化应用ID

	}


}
