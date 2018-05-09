package com.cjy.qiquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.dao.UserDao;
import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.model.LoginVo;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.model.VUser;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.utils.CipherUtil;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.SpringApplicationContext;
import com.cjy.qiquan.utils.StatesUtils;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;


	public static UserService instance() {
		return SpringApplicationContext.getBean("userService");
	}

	public void loadByDB() {
	}
	
	
	public int create(User user) {
		int ref = userDao.create(user);
		if (ref>0) {
			user.setUserId(ref);
		}
	
		return ref;
	}
	
	public int update(User user) {
		return userDao.update(user);
	}

	public User getUserById(final int id) {
		return userDao.getUserById(id);
	}

	public User getUserByName(final String name) {
		return userDao.getUserByName(name);
	}
	
	
	public VUser getVUserById(final int id) {
		return userDao.getVUserById(id);
	}

	public LoginVo<User> login(final String name, final String password) throws AppServiceException {
		User user = getUserByName(name);
		if (user == null) {
			throw new AppServiceException(StatesUtils.States.not_found, "无效的管理账号");
		}

		if (!CipherUtil.generatePassword(password).equals(user.getPassword_hash())) {
			throw new AppServiceException(StatesUtils.States.forbidden_user, "用户密码不正确");
		}

		String token = CommonUtils.newToken();
		UserSession session = new UserSession(token,user.getUserId(), user.getName(), user.getRole(), 1);
		CacheManager.instance.addCacheEx(Constant.CacheGroup.SESSION, token, session,
				Constant.DATE.TIME_ONE_DAY_SECOND * 300);
		LoginVo<User> login = new LoginVo.Builder<User>().setToken(token).setPartner(user).build();
		return login;
	}
	
	public int updateColById(final String col, final Object value, final int id) {
		return userDao.updateColById(col, value, id);
	}

	public User getUserByToken(final String token) {
		if (CommonUtils.isBlankOrNull(token)) {
			return null;
		}
		String id = CacheManager.instance.getCache(Constant.CacheGroup.SESSION, token);
		if (CommonUtils.isBlankOrNull(id)) {
			return null;
		}
		return getUserById(Integer.valueOf(id));
	}
	
	public List<VUser> listUserByHehuoId(final int hehuoId){
		return userDao.listUserByHehuoId(hehuoId);
	}
	
	
	/**
	 * 根据激活码获取用户id
	 * @param codes
	 * @return
	 */
	public List<String> listUserIdsByCodes(final List<String> codes) {
		return userDao.listUserIdsByCodes(codes);
	}

	public Page<VUser> listUserPagedByStatusId(final int status,List<String> codes, int index, int size){
		if (index <= 0) {
			index = 1;
		}
		if (size <= 0) {
			size = 20;
		}
		return userDao.listUserPagedByStatusId(status,codes, index, size);
	}
	
}
