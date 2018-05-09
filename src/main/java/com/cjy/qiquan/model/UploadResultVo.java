package com.cjy.qiquan.model;

import java.io.Serializable;

public class UploadResultVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938786584532504510L;
	private String name;
	private String originalName;
	
	private long size;
	
	private String state;
	
	private String type;
	
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
