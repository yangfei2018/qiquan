package com.cjy.qiquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.dao.SystemLogDao;
import com.cjy.qiquan.model.SystemLog;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class SystemLogService {

	@Autowired
	private SystemLogDao systemLogDao;

	public static SystemLogService instance() {
		return SpringApplicationContext.getBean("systemLogService");
	}

	public int create(SystemLog systemLog) {
		return systemLogDao.create(systemLog);
	}

	public int updateColById(final String col, final Object value, final int id) {
		return systemLogDao.updateColById(col, value, id);
	}

	public Page<SystemLog> listPaged(final int userid, int index, int size) {

		return systemLogDao.listPaged(userid, index, size);
	}

	public List<SystemLog> listUnreadMessage(final int userid) {
		return systemLogDao.listUnreadMessage(userid);
	}
	
	
	public int updateUnReadMessage(final int userid,final int mainId) {
		return systemLogDao.updateUnReadMessage(userid, mainId);
	}
}
