package com.cjy.qiquan.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.client.model.PostParameter;

public class CommonUtils {

	/**
	 * 生成新的token
	 * 
	 * @return
	 */

	static Pattern p = Pattern.compile("[a-zA-z]");

	public static String newToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String defaultNullString(String value) {

		if (StringUtils.isEmpty(value)) {
			return "";
		} else {
			return value;
		}
	}

	public static double floatFormat(double f) {
		BigDecimal bg = new BigDecimal(f);
		return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String convertNumToText(String cellValue) {
		if (CommonUtils.hasText(cellValue)) {
			if (cellValue.contains(".")) {
				if (p.matcher(cellValue).find()) {
					return cellValue;
				} else {
					return Integer.valueOf(cellValue) + "";
				}

			} else {
				return cellValue;
			}
		}

		return "";
	}

	public static String getAmountFormat(double amount) {

		NumberFormat nf = new DecimalFormat("##.###");
		return nf.format(amount);
	}

	public static String getNewOrderSN(String firstLetter) {

		StringBuilder sBuilder = new StringBuilder(firstLetter);
		String numString = CacheManager.instance.getCache(firstLetter, "inc");
		if (CommonUtils.isBlankOrNull(numString)) {
			numString = "0";
		}

		int newNum = Integer.valueOf(numString);
		newNum++;

		CacheManager.instance.addCache(firstLetter, "inc", String.valueOf(newNum));

		DecimalFormat df = new DecimalFormat(STR_FORMAT);
		sBuilder.append(df.format(newNum));

		return sBuilder.toString();
	}

	public static String getNewRechargeSN() {
		StringBuilder sn = new StringBuilder("RC");
		sn.append(System.nanoTime()).append(RandomUtil.getRandomNumber(5));
		return sn.toString();
	}

	private static final String STR_FORMAT = "000";

	public static String getNewTradeSN() {
		StringBuilder sn = new StringBuilder("TR");
		sn.append(System.nanoTime()).append(RandomUtil.getRandomNumber(5));
		return sn.toString();
	}

	public static boolean hasText(String value) {
		return (!CommonUtils.isBlankOrNull(value));
	}

	public static String getEmailLoginUri(final String email) {
		// EMAIL_MAP
		if (CommonUtils.isBlankOrNull(email)) {
			return "";
		}
		String email_last = email.substring(email.indexOf("@") + 1);
		if (Constant.EMAIL_MAP.containsKey(email_last)) {
			return Constant.EMAIL_MAP.get(email_last);
		}

		return "http://www." + email_last;
	}

	public static boolean isBlankOrNull(String value) {
		return StringUtils.isEmpty(value) || "null".equals(value);
	}

	public static String cutString(final String text, final int length) {
		if (CommonUtils.isBlankOrNull(text)) {
			return "";
		}

		if (text.length() < length) {
			return text;
		}

		return text.substring(0, length) + "...";
	}

	public static Float stringToFloat(String str) {
		float p = 0.00f;
		if (CommonUtils.hasText(str)) {
			try {
				p = Float.valueOf(str);
			} catch (Exception e) {
			}
		}
		return p;
	}

	public static int stringToInt(String str) {
		int p = 0;
		if (CommonUtils.hasText(str)) {
			try {
				p = Integer.valueOf(str);
			} catch (Exception e) {
			}
		}
		return p;
	}

	/**
	 * 记录单位时间内密码错误次数
	 * 
	 * @param username
	 *            久游通行证
	 * @param userIp
	 *            用户IP
	 */

	public static String getSign(PostParameter[] params, final String token) {
		List<String> sortList = new ArrayList<String>();
		for (PostParameter p : params) {
			String key = p.getName();
			String value = p.getValue();
			if (key.equals(Constant.SIGN)) {
				continue;
			}
			if (key.equals(Constant.ACCESS_TOKEN)) {
				// token单独获取了
				continue;
			}
			// if (key.equals(Constant.APPID)){
			// continue;
			// }

			if (CommonUtils.isBlankOrNull(value)) {
				continue;
			}

			sortList.add(value);
		}
		sortList.add(Constant.APPID_KEY);
		sortList.add(Constant.APPID_ENC);
		if (!CommonUtils.isBlankOrNull(token)) {
			sortList.add(token);
		}
		// value从小到大排序
		Collections.sort(sortList);

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < sortList.size(); i++) {
			result.append(sortList.get(i));
		}
		System.out.println("web:values:" + result.toString());
		String excepted = CipherUtil.hash(result.toString());
		System.out.println("web:sign:" + excepted);
		return excepted;
	}

	// @SuppressWarnings("rawtypes")
	// public static String getContentType(byte[] mapObj) {
	//
	// String type = "";
	// ByteArrayInputStream bais = null;
	// MemoryCacheImageInputStream mcis = null;
	// try {
	// bais = new ByteArrayInputStream(mapObj);
	// mcis = new MemoryCacheImageInputStream(bais);
	// Iterator itr = ImageIO.getImageReaders(mcis);
	// while (itr.hasNext()) {
	// ImageReader reader = (ImageReader) itr.next();
	// if (reader instanceof GIFImageReader) {
	// type = "gif";
	// } else if (reader instanceof JPEGImageReader) {
	// type = "jpg";
	// } else if (reader instanceof PNGImageReader) {
	// type = "png";
	// } else if (reader instanceof BMPImageReader) {
	// type = "bmp";
	// }
	// }
	// } finally {
	// if (bais != null) {
	// try {
	// bais.close();
	// } catch (IOException ioe) {
	//
	// }
	// }
	// if (mcis != null) {
	// try {
	// mcis.close();
	// } catch (IOException ioe) {
	//
	// }
	// }
	// }
	// return type;
	// }

	/**
	 * map按value排序
	 * 
	 * @param oriMap
	 * @return
	 */
	public static Map<String, String> sortMapByValue(Map<String, String> oriMap) {
		Map<String, String> sortedMap = new LinkedHashMap<String, String>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Map.Entry<String, String>> entryList = new ArrayList<Map.Entry<String, String>>(oriMap.entrySet());
			Collections.sort(entryList, new Comparator<Map.Entry<String, String>>() {
				public int compare(Entry<String, String> entry1, Entry<String, String> entry2) {
					int value1 = 0, value2 = 0;
					try {
						value1 = Integer.valueOf(entry1.getValue());
						value2 = Integer.valueOf(entry2.getValue());
					} catch (NumberFormatException e) {
						value1 = 0;
						value2 = 0;
					}
					return value2 - value1;
				}
			});
			Iterator<Map.Entry<String, String>> iter = entryList.iterator();
			Map.Entry<String, String> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}

	public static Map<String, String> sortMapByKey(Map<String, String> oriMap) {
		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String key1, String key2) {
				int intKey1 = 0, intKey2 = 0;
				try {
					intKey1 = Integer.valueOf(key1);
					intKey2 = Integer.valueOf(key2);
				} catch (Exception e) {
					intKey1 = 0;
					intKey2 = 0;
				}
				return intKey1 - intKey2;
			}
		});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	public static int getRecordTryCount(String key) {
		int count = 0;
		String tryCount = CacheManager.instance.getCache(Constant.CacheGroup.TRY_COUNT, key);
		if (!isBlankOrNull(tryCount)) {
			count = Integer.parseInt(tryCount);
		}
		return count;
	}

	public static void recordTryCount(String key) {
		int count = 0;
		String tryCount = CacheManager.instance.getCache(Constant.CacheGroup.TRY_COUNT, key);
		if (isBlankOrNull(tryCount)) {
			count = 1;
		} else {
			count = Integer.parseInt(tryCount) + 1;
		}
		CacheManager.instance.addCacheEx(Constant.CacheGroup.TRY_COUNT, key, String.valueOf(count),
				Constant.TRY_COUNT_CACHE_LIEF_CYCLE);
	}

	public static void recordTryCount(String key, int sec) {
		int count = 0;
		String tryCount = CacheManager.instance.getCache(Constant.CacheGroup.TRY_COUNT, key);
		if (isBlankOrNull(tryCount)) {
			count = 1;
		} else {
			count = Integer.parseInt(tryCount) + 1;
		}
		CacheManager.instance.addCacheEx(Constant.CacheGroup.TRY_COUNT, key, String.valueOf(count), sec);
	}

	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = "1.1.1.1";
		}
		return ip;
	}

	/**
	 * 隐藏手机号码部分字符
	 * 
	 * @param telphone
	 *            手机号码
	 * @return
	 */
	public static String hidePhoneNum(String telphone) {
		if (isBlankOrNull(telphone)) {
			return "";
		}
		char[] chars = telphone.toCharArray();
		if (chars.length > 5) {
			chars[4] = '*';
		}
		if (chars.length > 6) {
			chars[5] = '*';
		}
		if (chars.length > 7) {
			chars[6] = '*';
		}
		if (chars.length > 8) {
			chars[7] = '*';
		}
		return new String(chars);
	}

	public static String hideUsername(String username) {
		if (isBlankOrNull(username)) {
			return "";
		}

		char[] chars = username.toCharArray();
		if (chars.length > 1) {
			chars[1] = '*';
		}
		if (chars.length > 2) {
			chars[1] = '*';
			chars[2] = '*';
		}
		return new String(chars);
	}

	/**
	 * 隐藏邮箱部分字符
	 * 
	 * @param email
	 * @return
	 */
	public static String hideEmail(String email) {
		if (isBlankOrNull(email)) {
			return "";
		}
		if (email.indexOf("@") == -1) {
			return email;
		}
		// @符号前面的字符
		char[] chars = email.split("@")[0].toCharArray();
		for (int i = 1; i < 4; i++) {// 最多4个*
			if (chars.length - i > 0) {
				chars[chars.length - i] = '*';
			} else {
				break;
			}
		}
		return new String(chars) + "@" + email.split("@")[1];
	}

	public static String hideIdCard(String idCard) {
		return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
	}

	public static String hideBankCardNo(String bankAccount) {
		int length = bankAccount.length();
		String str = bankAccount.substring(0, length - 11) + "*******" + bankAccount.substring(length - 2);
		return str;
	}

	public static void main(Object[] arg) {

	}

}
