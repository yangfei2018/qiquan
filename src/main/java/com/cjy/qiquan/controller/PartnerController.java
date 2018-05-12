package com.cjy.qiquan.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.model.CashRecord;
import com.cjy.qiquan.model.GmUser;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.model.VRechargeRecord;
import com.cjy.qiquan.model.VTradeRecord;
import com.cjy.qiquan.model.VUser;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.TradeService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.utils.CipherUtil;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.Constant.AuthorityType;
import com.cjy.qiquan.utils.RegistAuthority;
import com.cjy.qiquan.utils.SessionScope;
import com.cjy.qiquan.utils.StatesUtils;
import com.cjy.qiquan.web.WebContext;
import com.cjy.qiquan.web.utils.CookieUtils;

@Controller
@RequestMapping("/partner")
public class PartnerController {

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private UserService userService;

	@Autowired
	private TradeService tradeService;

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping("/index")
	public String index(Partner user, Model model) {
		model.addAttribute("user", user);
		List<PartnerBankInfo> bankInfoList = partnerService.listPartnerBankByPartnerId(user.getUserId());
		model.addAttribute("bankInfoList", bankInfoList);
		return "partner/index";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public String changepwd() {
		return "partner/changepwd";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/json/doChangePwd", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doChangePwd(Partner user,
			@RequestParam(required = false, defaultValue = "") String oldpwd,
			@RequestParam(required = false, defaultValue = "") String newpwd,
			@RequestParam(required = false, defaultValue = "") String newpwd1) throws AppServiceException {
		ResultVo.Builder<String> res = new ResultVo.Builder<>();
		if (CommonUtils.isBlankOrNull(oldpwd) || CommonUtils.isBlankOrNull(newpwd)
				|| CommonUtils.isBlankOrNull(newpwd1)) {
			return res.setResult(false).setData("信息填写不完整").build();
		}
		if (!newpwd.equals(newpwd1)) {
			return res.setResult(false).setData("2次密码不一样").build();
		}

		if (StringUtils.isEmpty(newpwd) || newpwd.length() < 4) {
			throw new AppServiceException(StatesUtils.States.params_error, "新密码格式不正确");
		}

		if (user.getPassword_hash().equals(CipherUtil.buildPasswordHash(oldpwd))) {

			partnerService.updateColById("f_password_hash", CipherUtil.buildPasswordHash(newpwd), user.getUserId());
			return new ResultVo.Builder<String>(true).setData("新密码修改成功").build();
		} else {
			throw new AppServiceException(StatesUtils.States.params_error, "原密码不正确");
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping("/userlist")
	public String userlist(Partner user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		model.addAttribute("user", user);
		List<String> partnerCodes = new ArrayList<>();
		partnerCodes.add(user.getCode());
		Page<VUser> page = userService.listUserPagedByStatusId(status, partnerCodes, index, 9999);
		model.addAttribute("page", page);
		model.addAttribute("index", index);
		model.addAttribute("status", status);
		return "partner/userlist";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping("/userlist/{id}")
	public String userlistByHehuoId(Partner user, @PathVariable int id, Model model) {
		model.addAttribute("user", user);
		List<String> partnerCodes = new ArrayList<>();
		partnerCodes.add(user.getCode());
		List<VUser> list = userService.listUserByHehuoId(id);
		model.addAttribute("list", list);
		return "partner/userlistByHehuoId";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/userView/{id}", method = RequestMethod.GET)
	public String userView(@PathVariable int id, GmUser user, Model model) {
		VUser customer = userService.getVUserById(id);
		model.addAttribute("customer", customer);
		return "partner/userView";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/json/auditUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> auditUser(@RequestParam int userId,
			@RequestParam(required = false, defaultValue = "4") int status, Partner user) {
		User customer = userService.getUserById(userId);
		if (customer != null && customer.getPartnerCode().equals(user.getCode())) {
			userService.updateColById("f_status", status, userId);
			userService.updateColById("f_statusChangeTime", new Date(System.currentTimeMillis()), userId);
		}
		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}

	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable String id) {

		return "partner/view";
	}

	@RequestMapping("/login")
	public String login() {
		return "partner/login";
	}

	@RequestMapping("/logout")
	public String logout() {
		WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentPartnerUser);
		WebContext.delCookie(Constant.SESSION_KEY.currentPartnerUser);
		return "redirect:/partner/login";
	}

	@RequestMapping(value = "/json/doLogin", method = RequestMethod.POST)
	public @ResponseBody Partner doLogin(@RequestParam(required = false, defaultValue = "") String account,
			@RequestParam(required = false, defaultValue = "") String password, HttpServletRequest req,
			HttpServletResponse resp) throws AppServiceException {
		Partner user = partnerService.getPartnerByName(account);

		if (user == null) {
			throw new AppServiceException(StatesUtils.States.params_error, "代理商不存在");
		}
		if (!CipherUtil.buildPasswordHash(password).equals(user.getPassword_hash())) {
			throw new AppServiceException(StatesUtils.States.forbidden_user, "代理商密码不正确");
		}
		setUserSession(user, req, resp);
		return user;
	}

	/**
	 * 显示一个人询价列表
	 * 
	 * @param user
	 * @param id
	 * @param status
	 * @param index
	 * @param model
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/tradelist/{id}", method = RequestMethod.GET)
	public String tradelistView(Partner user, @PathVariable int id,
			@RequestParam(required = false, defaultValue = "-1") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		List<String> userIds = new ArrayList<>();
		userIds.add(id + "");
		Page<VTradeRecord> pages = tradeService.listVTradeByStatus(status, userIds, index, 9999);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		model.addAttribute("id", id);
		return "partner/tradelistView";
	}

	/**
	 * 显示一个人交易列表
	 * 
	 * @param user
	 * @param id
	 * @param status
	 * @param index
	 * @param model
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/orderlist/{id}", method = RequestMethod.GET)
	public String orderlistView(Partner user, @PathVariable int id,
			@RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		List<String> userIds = new ArrayList<>();
		userIds.add(id + "");
		Page<VOrder> pages = tradeService.listVOrderByStatus(status, userIds, index, 9999, "",null);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		model.addAttribute("id", id);
		return "partner/orderlistView";
	}

	/**
	 * 显示多用户询价列表
	 * 
	 * @param user
	 * @param status
	 * @param index
	 * @param model
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/tradelist", method = RequestMethod.GET)
	public String tradelist(Partner user, @RequestParam(required = false, defaultValue = "-1") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		List<String> partnerCodes = new ArrayList<>();
		partnerCodes.add(user.getCode());
		// Page<VUser> page = userService.listUserPagedByStatusId(status, partnerCodes,
		// index, 9999);
		//
		// List<String> userIds = new ArrayList<>();
		// for (VUser u : page.getList()) {
		// userIds.add(u.getUserId() + "");
		// }
		List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
		Page<VTradeRecord> pages = tradeService.listVTradeByStatus(status, userIds, index, 9999);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		return "partner/tradelist";
	}

	/**
	 * 显示多会员交易列表
	 * 
	 * @param user
	 * @param id
	 * @param status
	 * @param index
	 * @param model
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/orderlist", method = RequestMethod.GET)
	public String orderlist(Partner user, @RequestParam(required = false, defaultValue = "-1") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		List<String> partnerCodes = new ArrayList<>();
		partnerCodes.add(user.getCode());

		List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
		Page<VOrder> pages = tradeService.listVOrderByStatus(status, userIds, index, 9999, "",null);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		return "partner/orderlist";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/exchangelist", method = RequestMethod.GET)
	public String exchangelist(Partner user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		Page<VRechargeRecord> page = tradeService.listRechargeRecordByPartnerId(user.getUserId(), status, index, 9999);
		model.addAttribute("status", status);
		model.addAttribute("page", page);
		model.addAttribute("index", index);
		return "partner/exchangelist";
	}

	@SessionScope(Constant.SESSION_KEY.currentPartnerUser)
	@RegistAuthority(AuthorityType.PARTNER)
	@RequestMapping(value = "/withdrawcashlist", method = RequestMethod.GET)
	public String withdrawcashlist(Partner user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {

		List<String> partnerCodes = new ArrayList<>();
		partnerCodes.add(user.getCode());

		List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
		Page<CashRecord> pages = tradeService.listCashRecordByStatus(status, userIds, index, 9999);

		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);

		return "partner/withdrawcashlist";
	}

	private void setUserSession(Partner user, HttpServletRequest req, HttpServletResponse resp) {
		String token = CommonUtils.newToken();
		UserSession session = new UserSession(token, user.getUserId(), user.getName(), user.getRole(),
				user.getStatus());
		CacheManager.instance.addCacheEx(Constant.CacheGroup.PARTNERSESSION, token, user.getUserId() + "",
				Constant.DATE.TIME_ONE_DAY_SECOND * 30);
		user.setToken(token);
		req.getSession().setAttribute(Constant.SESSION_KEY.currentPartnerUser, session);
		CookieUtils.addCookie(req, resp, Constant.SESSION_KEY.currentPartnerUser, token);
	}

}
