package com.cjy.qiquan.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjy.qiquan.client.model.PostParameter;

public class ArrayUtils {

	
	/** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            prestr.append(key).append("=").append(value);
            if (i != keys.size() - 1) {//拼接时，不是最后一个，末尾加&
            	prestr.append("&");
            }
        }
        return prestr.toString();
    }
    
    
    
	public static PostParameter[] mapToArray(Map<String, String> map) {
		PostParameter[] parList = new PostParameter[map.size()];
		Iterator<String> iter = map.keySet().iterator();
		int i = 0;
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);
			parList[i++] = new PostParameter(key, value);
		}
		return parList;
	}

}
