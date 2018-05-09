package com.cjy.qiquan.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
	/**
	 * 序列化
	 *
	 * @param object
	 *            对象
	 * @return byte[]
	 * 
	 * 
	 * bs = new ByteArrayOutputStream(1024);
			os = new ObjectOutputStream(bs);
			os.writeObject(t);
			byte[] date = bs.toByteArray();
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		if (object != null) {
			try {
				baos = new ByteArrayOutputStream(1024);
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (baos != null) {
						baos.close();
					}
					if (oos != null) {
						oos.close();
					}
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 反序列化
	 *
	 * @param bytes
	 *            byte数据
	 * @return Object
	 */
	public static Object unserialize(byte[] bytes) {
		if (bytes != null) {
			ByteArrayInputStream bais = null;
			ObjectInputStream ois = null;
			try {
				bais = new ByteArrayInputStream(bytes);
				ois = new ObjectInputStream(bais);
				return ois.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bais != null) {
						bais.close();
					}
					if (ois != null) {
						ois.close();
					}
				} catch (IOException e2) {
				}
			}
		}
		return null;

	}
}
