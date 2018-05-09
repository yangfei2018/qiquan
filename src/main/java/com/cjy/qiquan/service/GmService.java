package com.cjy.qiquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.dao.GmDao;
import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.model.GmUser;
import com.cjy.qiquan.model.LoginVo;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.utils.CipherUtil;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.SpringApplicationContext;
import com.cjy.qiquan.utils.StatesUtils;

@Service
public class GmService {

	@Autowired
	private GmDao gmDao;

	public static GmService instance() {
		return SpringApplicationContext.getBean("gmService");
	}

	public int create(GmUser user) {
		int ref = gmDao.create(user);
		if (ref > 0) {
			user.setUserId(ref);
		}

		return ref;
	}

	public int update(GmUser user) {
		return gmDao.update(user);
	}

	public GmUser getUserById(final int id) {
		return gmDao.getUserById(id);
	}

	public GmUser getUserByName(final String name) {
		return gmDao.getUserByName(name);
	}

	public LoginVo<GmUser> login(final String name, final String password) throws AppServiceException {
		GmUser user = getUserByName(name);
		if (user == null) {
			throw new AppServiceException(StatesUtils.States.not_found, "无效的管理账号");
		}

		if (!CipherUtil.generatePassword(password).equals(user.getPassword_hash())) {
			throw new AppServiceException(StatesUtils.States.forbidden_user, "用户密码不正确");
		}

		String token = CommonUtils.newToken();
		UserSession session = new UserSession(token, user.getUserId(), user.getName(), user.getRole(), 1);
		CacheManager.instance.addCacheEx(Constant.CacheGroup.GMSESSION, token, session,
				Constant.DATE.TIME_ONE_DAY_SECOND * 300);
		LoginVo<GmUser> login = new LoginVo.Builder<GmUser>().setToken(token).setPartner(user).build();
		return login;
	}

	public int updateColById(final String col, final Object value, final int id) {
		return gmDao.updateColById(col, value, id);
	}

	
	public List<GmUser> listAllGmUser(){
		return gmDao.listAllGmUser();
	}
	
	public GmUser getUserByToken(final String token) {
		if (CommonUtils.isBlankOrNull(token)) {
			return null;
		}
		String id = CacheManager.instance.getCache(Constant.CacheGroup.GMSESSION, token);
		if (CommonUtils.isBlankOrNull(id)) {
			return null;
		}
		return getUserById(Integer.valueOf(id));
	}

}
