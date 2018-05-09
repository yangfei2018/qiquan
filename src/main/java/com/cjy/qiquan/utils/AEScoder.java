package com.cjy.qiquan.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



/**
 * AES加密解密
 * @author chenjiyin
 *
 */
public class AEScoder {

	public static String encrypt(String content, String key) {

		try {
			SecretKey enckey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, enckey);
			
			return new BASE64Encoder().encode(c1.doFinal(content.getBytes("utf-8")));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String content, String password) {
		try {
			SecretKey deskey = new SecretKeySpec(password.getBytes(), "AES");
			// 解密
			Cipher c1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);

			BASE64Decoder decoder = new BASE64Decoder();
			String data = new String(c1.doFinal(decoder.decodeBuffer(content)),
					"UTF-8");
			return data;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] arg){
//		String data = encrypt("{emai", "34z4kdc83sdlinbe");
//		System.out.println(data);
		String data1 = decrypt("nftG+0/8UmctKzCkvh6beeggkyqgBGOhD1nE1WLRvyFbR3AheaztI4+GFxhu+NK8vE09vhym1iDHd69CY6nXqffR8+QMvyNIcmmllwlyBd4=", Constant.WEB_KEY);
		System.out.println(data1);
	}
	
}
