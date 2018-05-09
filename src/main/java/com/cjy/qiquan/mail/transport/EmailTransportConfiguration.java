package com.cjy.qiquan.mail.transport;

import org.apache.log4j.Logger;

import com.cjy.qiquan.utils.properties.Prop;
import com.cjy.qiquan.utils.properties.Proper;

public class EmailTransportConfiguration {

	private final static Logger Out = Logger
			.getLogger(EmailTransportConfiguration.class);

	private static final String PROPERTIES_FILE = "mail.properties";
	private static final String KEY_SMTP_SERVER = "smtp.server";
	private static final String KEY_AUTH_REQUIRED = "auth.required";
	private static final String KEY_USE_SECURE_SMTP = "use.secure.smtp";
	private static final String KEY_USERNAME = "smtp.username";
	private static final String KEY_PASSWORD = "smtp.password";
	private static final String KEY_FROM = "mail.from";
	private static String smtpServer = "";
	private static boolean authenticationRequired = false;
	private static boolean useSecureSmtp = false;
	private static String username = null;
	private static String password = null;

	public static String from = null;

	static {
		Prop constants = null;
		try {
			constants = Proper.use(PROPERTIES_FILE);
		} catch (Exception e) {
			Out.warn(e.getMessage());
		}
		if (constants == null) {
			System.out.println("constants null");
			smtpServer = "smtp.aliyun.com";
			authenticationRequired = true;
			useSecureSmtp = true;
			username = "postmanager@ejiarens.com";
			password = "Pmg123456";
			from = "postmanager@ejiarens.com";
		} else {
			smtpServer = constants.get(KEY_SMTP_SERVER);
			authenticationRequired = constants.getBoolean(KEY_AUTH_REQUIRED);
			useSecureSmtp = constants.getBoolean(KEY_USE_SECURE_SMTP);
			username = constants.get(KEY_USERNAME);
			password = constants.get(KEY_PASSWORD);
			from = constants.get(KEY_FROM);
		}
	}

	// static {
	// Properties properties = loadProperties();
	//
	// String smtpServer = properties.getProperty(KEY_SMTP_SERVER);
	// boolean authenticationRequired = Boolean
	// .parseBoolean(KEY_AUTH_REQUIRED);
	// boolean useSecureSmtp = Boolean.parseBoolean(KEY_USE_SECURE_SMTP);
	// String username = properties.getProperty(KEY_USERNAME);
	// String password = properties.getProperty(KEY_PASSWORD);
	//
	// configure(smtpServer, authenticationRequired, useSecureSmtp, username,
	// password);
	// }

	/**
	 * Configure mail transport to use the specified SMTP server. Because this
	 * configuration mode does not require to inform username and password, it
	 * assumes that authentication and secure SMTP are not required.
	 * 
	 * @param smtpServer
	 *            The SMTP server to use for mail transport. To use a specific
	 *            port, user the syntax server:port.
	 */
	// public static void configure(String smtpServer) {
	// configure(smtpServer, false, false, null, null);
	// }

	/**
	 * @param smtpServer
	 *            The SMTP server to use for mail transport. To use a specific
	 *            port, user the syntax server:port.
	 * @param authenticationRequired
	 *            Informs if mail transport needs to authenticate to send mail
	 *            or not.
	 * @param useSecureSmtp
	 *            Use secure SMTP to send messages.
	 * @param username
	 *            The SMTP username.
	 * @param password
	 *            The SMTP password.
	 */
	public static void configure(String smtpServer,
			boolean authenticationRequired, boolean useSecureSmtp,
			String username, String password) {
		EmailTransportConfiguration.smtpServer = smtpServer;
		EmailTransportConfiguration.authenticationRequired = authenticationRequired;
		EmailTransportConfiguration.useSecureSmtp = useSecureSmtp;
		EmailTransportConfiguration.username = username;
		EmailTransportConfiguration.password = password;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public boolean isAuthenticationRequired() {
		return authenticationRequired;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public boolean useSecureSmtp() {
		return useSecureSmtp;
	}

}
