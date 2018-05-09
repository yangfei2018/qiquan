package com.cjy.qiquan.client.model;

import org.apache.log4j.Logger;

import com.cjy.qiquan.mail.transport.EmailTransportConfiguration;
import com.cjy.qiquan.utils.properties.Prop;
import com.cjy.qiquan.utils.properties.Proper;

public class Configuration {

	private final static Logger Out = Logger
			.getLogger(EmailTransportConfiguration.class);

	private static final String PROPERTIES_FILE = "restapi.properties";
	public static String proxyHost = "";
	public static int proxyPort = 0;
	public static String proxyAuthUser = null;
	public static String proxyAuthPassword = null;

	public static String baseUrl = null;
	
	public static boolean debug = false;

	public static boolean dalvik = false;
	
	static {
		Prop constants = null;
		try {
			constants = Proper.use(PROPERTIES_FILE);
		} catch (Exception e) {
			Out.warn(e.getMessage());
		}
		if (constants != null) {
			proxyHost = constants.get("proxy_host");
			proxyPort = constants.getInt("proxy_port");
			proxyAuthUser = constants.get("proxy_authuser");
			proxyAuthPassword = constants.get("proxy_auth_password");
			debug = constants.getBoolean("api.debug");
			baseUrl = constants.get("api.baseurl");
		}
	}

}
