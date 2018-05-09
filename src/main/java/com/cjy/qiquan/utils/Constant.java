package com.cjy.qiquan.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.cjy.qiquan.utils.properties.Prop;
import com.cjy.qiquan.utils.properties.Proper;

public final class Constant {

	final static Logger Out = Logger.getLogger(Constant.class);

	public final static String encoding;// 编码

	public final static String CONNECTOR = "::";

	public final static String Sep = "@@";

	public final static List<String> allowType = new ArrayList<String>();

	public final static boolean TEST_MODE;

	public static final String ACCESS_TOKEN = "access_token";

	public static final String SIGN = "sign";

	public static final String APPID = "app_id";

	public static final String APPID_KEY = "web";

	public static final String APPID_ENC = "XeDcsk*XjdcF34x65";

	public static final String USER_AVAR_BASE = "";

	public static final String WEB_KEY;
	public static final String APP_URL;
	public static final String APP_UPLOAD_PATH;
	public static final String APP_UPLOAD_URL;
	public static final int BLACKCOOL;
	public static final int __TIME__;
	public static final int __COUNT__;
	public static final int PAGE_SIZE = 20;

	public static final int PAGE_SIZE_INDEX = 8;

	public static final String X_AUTH_MODE = "client_auth";
	public static final String UPLOAD_MODE = "file";
	public static final int TRY_COUNT_CACHE_LIEF_CYCLE;

	public static final double BS = 1.1;
	
	public static final Map<String, String> EMAIL_MAP = new HashMap<String, String>() {
		private static final long serialVersionUID = 1697157155140063467L;

		{
			put("qq.com", "http://mail.qq.com");
			put("gmail.com", "http://www.gmail.com");
			put("hotmail.com", "mail.live.com");
			put("yahoo.com", "http://mail.yahoo.com");
			put("sina.com", "http://mail.sina.com");
			put("163.com", "http://mail.163.com");
			put("126.com", "http://mail.126.com");
			put("vip.sina.com", "http://mail.sina.com");
			put("sina.cn", "http://mail.sina.cn");
			put("sohu.com", "http://mail.sohu.com");
		}
	};
	static {
		Prop constants = null;
		try {
			constants = Proper.use("application.properties");
		} catch (Exception e) {
			Out.warn(e.getMessage());
		}
		if (constants == null) {
			System.out.println("constants null");
			encoding = "UTF-8";
			allowType.add("jpg");
			allowType.add("png");
			allowType.add("jpeg");
			allowType.add("bmp");
			allowType.add("gif");
			TEST_MODE = false;
			BLACKCOOL = 1800 * 1000;
			__TIME__ = 800;
			__COUNT__ = 150;
			WEB_KEY = "34z4kdc83sdlinbe";
			TRY_COUNT_CACHE_LIEF_CYCLE = 3600;
			APP_URL = "localhost:8080/restful";
			APP_UPLOAD_PATH = "/opt/htdocs/upload/";
			APP_UPLOAD_URL = "http://localhost:8080/restful/upload/";
		} else {
			encoding = constants.get("app.encoding", "UTF-8");
			String allowStr = constants.get("allowType");
			System.out.println("allowStr:" + allowStr);
			if (StringUtils.hasText(allowStr)) {
				String[] ar = allowStr.split(",");
				for (String a : ar) {
					allowType.add(a);
				}
			}

			TEST_MODE = constants.getBoolean("app.testmode", false);
			BLACKCOOL = constants.getInt("ip.blacks.cooldown.millis", 1800 * 1000);
			__TIME__ = constants.getInt("ip.req.interval.millis", 800);
			__COUNT__ = constants.getInt("ip.req.count", 150);
			WEB_KEY = constants.get("app.web.key");
			thumbnailImageSize.height = constants.getInt("app.thum.height", 480);
			thumbnailImageSize.width = constants.getInt("app.thum.width", 640);
			APP_URL = constants.get("app.url");
			APP_UPLOAD_PATH = constants.get("app.upload.path");
			APP_UPLOAD_URL = constants.get("app.upload.url");
			TRY_COUNT_CACHE_LIEF_CYCLE = constants.getInt("trycount.cacheLifeCycle", 3600);
		}
	}

	public static class CustomerStatus {
		public static final int REG = 1;
		public static final int WAIT_CERTIFICATION = 2;
		public static final int CERTIFICATION = 3;
		public static final int UNCERTIFICATION = -1;
	}

	public enum GM_ROLE {
		ADMIN(0, "系统管理员"), CAIWU(1, "财务"), OPER(2, "客服"),OP_MASTER(3, "客服总管");

		private final String value;
		private final int id;

		GM_ROLE(Integer id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static GM_ROLE get(int id) {
			for (GM_ROLE gender : GM_ROLE.values()) {
				if (gender.getId() == id) {
					return gender;
				}
			}
			return null;
		}
	}

	public enum Gender {
		MAN(1, "男"), WOMEN(2, "女");
		private final String value;
		private final int id;

		Gender(Integer id, String value) {
			this.id = id;
			this.value = value;
		}

		public int getId() {
			return id;
		}

		public String getValue() {
			return value;
		}

		public static Gender get(int id) {
			for (Gender gender : Gender.values()) {
				if (gender.getId() == id) {
					return gender;
				}
			}
			return null;
		}
	}

	public enum UserStates {

		NORMAL(0), VALIADE(1), FORBIDDEN(-1), MASTER_VALIDE(2);
		private final int id;

		UserStates(Integer id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	public static class DATE {
		/**
		 * 数量定义
		 */
		public static final int ONE = 1;
		public static final int TWO = 2;
		public static final int FIVE = 5;
		public static final int EIGHT = 8;
		public static final int TEN = 10;
		public static final int SIXTEEN = 16;
		public static final int THIRTY = 30;
		public static final int THIRY_TWO = 32;
		public static final int FIFTY = 50;

		/**
		 * 时间毫秒数定义
		 */
		public static final long TIME_ONE_SECOND = 1 * 1000;
		public static final long TIME_ONE_MINUTE = 60 * TIME_ONE_SECOND;
		public static final long TIME_ONE_HOUR = 60 * TIME_ONE_MINUTE;
		public static final long TIME_ONE_DAY = 24 * TIME_ONE_HOUR;
		public static final int TIME_ONE_DAY_SECOND = 86400;
		public static final int ONE_MINUTE_SECOND = 60;
	}
	
	
	public static class MAINID{
		public static final int HUIYUANRENZHENG  = 1;
		public static final int XUNJIA	= 2;
		public static final int ORDER	= 3;
		public static final int PINGCANG	= 4;
		public static final int WITHDRAWCASH	= 5;
	}

	/**
	 * 
	 * @author pc
	 *
	 */
	public static class thumbnailImageSize {
		public static int height = 480;
		public static int width = 640;
	}

	public static class SESSION_KEY {
		public static final String currentUser = "currentUser";
		public static final String currentGmUser = "currentGmUser";
		public static final String currentPartnerUser = "currentPartnerUser";
		public static final String gmUser = "gmUser";
		public static final String partnerUser = "partnerUser";
		public static final String verifyPic = "verifyPic";
		public static final String reset_mobile = "reset_mobile";
		public static final String reset_email_s = "reset_email_s";
	}

	public static class CacheGroup {
		public static final String SESSION = "_session";
		public static final String TOKEN = "_token";
		public static final String MOBILE = "_mobile";
		public static final String MAIL = "_mail";
		public static final String FIND_PWD = "_find_pwd";
		public static final String TRY_COUNT = "_trycount";
		public static final String USER_NAME = "_username";
		public static final String GMSESSION = "_gmsession";
		public static final String PARTNERSESSION = "_partnersession";
		public static final String IMPORT_FEILV = "_import_feilv";
		public static final String USER_REASON = "_auditUserReason";
		public static final String IMPORT_TRADE = "_import_trade";
		public static final String JOB_LIST = "_job_list";
		public static final String JOB_OBJ = "_job_obj";
	}

	/**
	 * controllor 验证类型
	 * 
	 * @author chenjiyin
	 *
	 */
	public enum AuthorityType {
		CUSTOMER(1), MASTER(2), PARTNER(3);

		private int id;

		private AuthorityType(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

	}

	public enum GoodsCategory {
		ZHISHU(1, "指数期权"), SHANGPIN(2, "商品期权"),GEGUQIQUAN(3, "个股期权");

		private int id;

		private String name;

		private GoodsCategory(int id, String name) {
			this.id = id;
			this.name = name;
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

		public static GoodsCategory get(int id) {
			for (GoodsCategory gender : GoodsCategory.values()) {
				if (gender.getId() == id) {
					return gender;
				}
			}
			return null;
		}

	}

	public static enum TRADE_STATUS {
		CREATE(0, "等待报价"), OFFER(1, "已报价"), DEAL(2, "成交");

		private int id;

		private String name;

		private TRADE_STATUS(int id, String name) {
			this.id = id;
			this.name = name;
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

		public static TRADE_STATUS get(int id) {
			for (TRADE_STATUS gender : TRADE_STATUS.values()) {
				if (gender.getId() == id) {
					return gender;
				}
			}
			return null;
		}
	}

	public static enum ORDER_STATUS {
		CREATE(0, "待成交"), POSITION(1, "已成交"), EVENING_UP(2, "平仓中"), SETTLE(3, "已结算");

		private int id;

		private String name;

		private ORDER_STATUS(int id, String name) {
			this.id = id;
			this.name = name;
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

		public static ORDER_STATUS get(int id) {
			for (ORDER_STATUS gender : ORDER_STATUS.values()) {
				if (gender.getId() == id) {
					return gender;
				}
			}
			return null;
		}
	}

	public static class PAGE_INFO {
		public static final String TITLE = "期权";
	}
}
