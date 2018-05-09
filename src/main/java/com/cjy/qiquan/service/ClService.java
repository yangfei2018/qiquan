package com.cjy.qiquan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.dao.ClDao;
import com.cjy.qiquan.model.Cl;
import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class ClService {

	@Autowired
	private ClDao clDao;

	public static ClService instance() {
		return SpringApplicationContext.getBean("clService");
	}
	
	
	public Cl getRecentPriceByszlable(final String szlable) {
		return clDao.getRecentPriceByszlable(szlable);
	}

}
