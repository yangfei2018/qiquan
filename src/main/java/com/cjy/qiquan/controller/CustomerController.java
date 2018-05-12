package com.cjy.qiquan.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.cjy.qiquan.job.Job;
import com.cjy.qiquan.job.JobTaskIds;
import com.cjy.qiquan.job.JobTracker;
import com.cjy.qiquan.model.CashRecord;
import com.cjy.qiquan.model.Cl;
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.model.Order;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.RechargeRecord;
import com.cjy.qiquan.model.TradeRecord;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.model.VTradeRecord;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.service.AppEnv;
import com.cjy.qiquan.service.ClService;
import com.cjy.qiquan.service.DBService;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.TradeService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.task.SendSmsTask;
import com.cjy.qiquan.task.SystemLogHandler;
import com.cjy.qiquan.task.TaskFacotry;
import com.cjy.qiquan.utils.CipherUtil;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.Constant.AuthorityType;
import com.cjy.qiquan.utils.Constant.MAINID;
import com.cjy.qiquan.utils.DateFormater;
import com.cjy.qiquan.utils.RandomUtil;
import com.cjy.qiquan.utils.RegistAuthority;
import com.cjy.qiquan.utils.SessionScope;
import com.cjy.qiquan.utils.StatesUtils;
import com.cjy.qiquan.utils.VerifyCodeUtils;
import com.cjy.qiquan.web.WebContext;
import com.cjy.qiquan.web.utils.CookieUtils;

import cn.tsign.ching.eSign.SignHelper;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private UserService userService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private ClService clService;

	@RequestMapping("/login")
	public String login() {
		return "customer/login";
	}

	@RequestMapping("/reg")
	public String reg() {
		return "reg";
	}

	@RequestMapping("/logout")
	public String logout() {
		WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentUser);
		WebContext.delCookie(Constant.SESSION_KEY.currentUser);
		return "redirect:/index";
	}

	private void setUserSession(User user, HttpServletRequest req, HttpServletResponse resp) {
		String token = CommonUtils.newToken();
		UserSession session = new UserSession(token, user.getUserId(), user.getName(), user.getRole(),
				user.getStatus());
		CacheManager.instance.addCacheEx(Constant.CacheGroup.SESSION, token, user.getUserId() + "",
				Constant.DATE.TIME_ONE_DAY_SECOND * 30);
		user.setToken(token);
		req.getSession().setAttribute(Constant.SESSION_KEY.currentUser, session);
		CookieUtils.addCookie(req, resp, Constant.SESSION_KEY.currentUser, token);
	}

	@RequestMapping(value = "/json/doRegist", method = RequestMethod.POST)
	public @ResponseBody ResultVo<User> doRegist(@RequestParam(required = false, defaultValue = "") String account,
			@RequestParam(required = false, defaultValue = "") String code,
			@RequestParam(required = false, defaultValue = "") String partnerCode,
			@RequestParam(required = false, defaultValue = "") String password, HttpServletRequest req,
			HttpServletResponse resp) throws AppServiceException {

		User user = userService.getUserByName(account);
		if (user != null) {
			throw new AppServiceException(StatesUtils.States.params_error, "账号已存在");
		}

		if (CommonUtils.isBlankOrNull(partnerCode)) {
			throw new AppServiceException(StatesUtils.States.params_error, "激活码不能为空");
		}

		Partner partner = partnerService.getPartnerByCode(partnerCode);
		if (partner == null) {
			throw new AppServiceException(StatesUtils.States.params_error, "无效的激活码");
		}

		String codeOld = CacheManager.instance.getCache(Constant.CacheGroup.MOBILE, account);
		if (CommonUtils.isBlankOrNull(codeOld) || (!codeOld.equals(code))) {
			throw new AppServiceException(StatesUtils.States.params_error, "短信验证码不正确");
		}
		user = new User();
		user.setName(account);
		user.setAmount(0);
		user.setMobile(account);
		user.setPartnerCode(partnerCode);
		user.setPassword_hash(CipherUtil.buildPasswordHash(password));
		user.setStatus(1);
		user.setRole("customer");
		userService.create(user);

		setUserSession(user, req, resp);

		return new ResultVo.Builder<User>(true).setData(user).build();
	}

	@RequestMapping(value = "/json/doLogin", method = RequestMethod.POST)
	public @ResponseBody User doLogin(@RequestParam int loginMethod,
			@RequestParam(required = false, defaultValue = "") String account,
			@RequestParam(required = false, defaultValue = "") String smsCode,
			@RequestParam(required = false, defaultValue = "") String password, HttpServletRequest req,
			HttpServletResponse resp) throws AppServiceException {
		User user = userService.getUserByName(account);
		if (user == null) {
			throw new AppServiceException(StatesUtils.States.params_error, "用户尚未注册");
		}
		
		if (user.getBan()==1) {
			throw new AppServiceException(StatesUtils.States.params_error, "该账号已被冻结，请联系管理员");
		}
		
		if (loginMethod == 1) {
			// 短信登录
			if (CommonUtils.isBlankOrNull(account) || CommonUtils.isBlankOrNull(smsCode)) {
				throw new AppServiceException(StatesUtils.States.params_error, "用户名或短信验证码不能为空");
			}
			String code = CacheManager.instance.getCache(Constant.CacheGroup.MOBILE, account);
			if (CommonUtils.isBlankOrNull(code) || (!smsCode.equals(code))) {
				throw new AppServiceException(StatesUtils.States.params_error, "用户名或短信验证码不正确");
			}

		} else {
			// 密码登录
			if (!CipherUtil.buildPasswordHash(password).equals(user.getPassword_hash())) {
				throw new AppServiceException(StatesUtils.States.forbidden_user, "用户密码不正确");
			}
		}

		setUserSession(user, req, resp);
		return user;
	}

	@RequestMapping(value = "/verify_pic")
	public void verifyPic(HttpServletResponse response) {
		response.setDateHeader("Expires", -1);
		response.setHeader("cache-control", "no-cache");
		response.setHeader("pragma", "no-cache");
		response.setContentType("image/jpeg");// 告诉浏览器用什么方式打开

		try {
			String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
			WebContext.getSession().setAttribute(Constant.SESSION_KEY.verifyPic, verifyCode);
			VerifyCodeUtils.outputImage(100, 35, response.getOutputStream(), verifyCode);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/json/sendsms", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> sendSms(HttpServletRequest req,
			@RequestParam(required = false, defaultValue = "") String verifyCode,
			@RequestParam(required = false, defaultValue = "") String account,

			@RequestParam(required = false, defaultValue = "0") int sess) {

		if (CommonUtils.isBlankOrNull(account) || CommonUtils.isBlankOrNull(verifyCode)) {
			return new ResultVo.Builder<String>(false).setData("手机号或验证码不能为空").build();
		}

		String sessionVerfyCode = (String) req.getSession().getAttribute(Constant.SESSION_KEY.verifyPic);
		if (CommonUtils.isBlankOrNull(sessionVerfyCode) || (!verifyCode.equalsIgnoreCase(sessionVerfyCode))) {
			return new ResultVo.Builder<String>(false).setData("验证码不正确,或已过期！").build();
		}
		String ip = CommonUtils.getClientIP(req);
		int count = CommonUtils.getRecordTryCount(ip);
		if (count > 5) {
			return new ResultVo.Builder<String>(false).setData("您获取手机验证码太频繁，请稍后再试！").build();
		}

		String code = CacheManager.instance.getCache(Constant.CacheGroup.MOBILE, account);
		if (CommonUtils.isBlankOrNull(code)) {
			code = RandomUtil.getRandomNumber(4);
			// code = "4321";
		}
		System.out.println("mobile:" + account + ",code:" + code);
		CacheManager.instance.addCacheEx(Constant.CacheGroup.MOBILE, account, code,
				Constant.DATE.ONE_MINUTE_SECOND * 2);
		TaskFacotry.instance().put(new SendSmsTask(account, "232331", code));

		CommonUtils.recordTryCount(ip);
		return new ResultVo.Builder<String>(true).setData(code).build();
	}

	@RequestMapping("/calcu")
	public String calcu() {
		return "customer/calcu";
	}

	@RequestMapping("/calcu1")
	public String calcu1() {
		return "customer/calcu1";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping("/index")
	public String index(Model model, User user) {

		model.addAttribute("user", user);
		return "customer/index";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping("/xunjia")
	public String xunjia(Model model, User user) {

		model.addAttribute("goodsCategory", Constant.GoodsCategory.values());
		return "customer/xunjia";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping("/certification")
	public String certification(Model model, User user) {

		model.addAttribute("user", user);
		String reason = CacheManager.instance.getCache(Constant.CacheGroup.USER_REASON,
				String.valueOf(user.getUserId()));
		model.addAttribute("error", reason);
		model.addAttribute("banklist", AppEnv.BANKLIST);

		if (AppEnv.BANKLIST.contains(user.getBankOfDeposit())) {
			model.addAttribute("otherBank", false);
			model.addAttribute("selectBank", user.getBankOfDeposit());
		} else {
			model.addAttribute("otherBank", true);
			model.addAttribute("selectBank", "其他");
		}

		return "customer/certification";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/certification", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> certification(
			@RequestParam(required = false, defaultValue = "") String realName,
			@RequestParam(required = false, defaultValue = "") String idCard,
			@RequestParam(required = false, defaultValue = "") String address,
			@RequestParam(required = false, defaultValue = "") String bankOfDeposit,
			@RequestParam(required = false, defaultValue = "") String bankOfDepositOther,
			@RequestParam(required = false, defaultValue = "") String bankCardNo,
			@RequestParam(required = false, defaultValue = "") String bankAddress,
			@RequestParam(required = false, defaultValue = "") String idCardFrontPic,
			@RequestParam(required = false, defaultValue = "") String idCardBackgroundPic, User user)
			throws AppServiceException {

		if (bankCardNo.length() < 11 || bankCardNo.length() > 19) {
			throw new AppServiceException(StatesUtils.States.params_error, "银行卡号格式不正确");
		}

		if (bankOfDeposit.equals("其他")) {
			bankOfDeposit = bankOfDepositOther;
		}

		if (CommonUtils.isBlankOrNull(bankOfDeposit)) {
			throw new AppServiceException(StatesUtils.States.params_error, "开户银行不能为空");
		}

		user.setAddress(bankAddress);
		user.setBankAddress(bankAddress);
		user.setBankCardNo(bankCardNo);
		user.setBankOfDeposit(bankOfDeposit);
		user.setIdCardBackgroundPic(idCardBackgroundPic);
		user.setIdCardFrontPic(idCardFrontPic);
		user.setRealName(realName);
		user.setIdCard(idCard);
		user.setAddress(address);
		user.setStatus(Constant.CustomerStatus.WAIT_CERTIFICATION);
		int ref = userService.update(user);
		if (ref > 0) {
			CacheManager.instance.removeCache(Constant.CacheGroup.USER_REASON, String.valueOf(user.getUserId()));
			StringBuilder body = new StringBuilder();

			int belongUserId = 0;
			Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
			if (partner != null) {
				belongUserId = partner.getBelongtogmsuer();
			}

			body.append("客户“").append(user.getRealName()).append("”").append("提交了实名认证申请");
			DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.HUIYUANRENZHENG, belongUserId));

			return new ResultVo.Builder<String>(true).setData("SUCCESS").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("FAIL").build();
		}
	}

	@RequestMapping("/json/listGoodsByCategoryId")
	public @ResponseBody List<Goods> listGoodsByCategoryId(@RequestParam int categoryId) {
		return goodsService.listGoodsByCategoryId(categoryId);
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/xunjialist", method = RequestMethod.GET)
	public String xunjialist(User user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		Page<TradeRecord> pages = tradeService.listTradeByUser(user.getUserId(), status, index, 20);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		return "customer/xunjialist";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/trade/{id}", method = RequestMethod.GET)
	public String trade(@PathVariable String id, User user, Model model) {
		TradeRecord tradeRecord = tradeService.getTradeRecordByTradeNo(id);
		if (tradeRecord == null || tradeRecord.getUserId() != user.getUserId()) {
			model.addAttribute("error", "无效的记录");
		}
		model.addAttribute("item", tradeRecord);
		return "customer/trade";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/doTrade", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doTrade(@RequestParam String tradeNo,
			@RequestParam(required = false, defaultValue = "1") int buyType, User user) {
		TradeRecord tradeRecord = tradeService.getTradeRecordByTradeNo(tradeNo);
		if (tradeRecord == null || tradeRecord.getUserId() != user.getUserId()) {
			return new ResultVo.Builder<String>(false).setData("无效的询价记录").build();
		}

		if (tradeRecord.getStatus() != Constant.TRADE_STATUS.OFFER.getId()) {
			return new ResultVo.Builder<String>(false).setData("当前询价记录无此权限").build();
		}

		tradeRecord.setBuyType(buyType);

		if (tradeRecord.getCategoryId() == Constant.GoodsCategory.SHANGPIN.getId()) {
			Goods goods = goodsService.getGoodsByCode(tradeRecord.getProductCode());

			if (tradeRecord.getShou() < goods.getMin_shou()) {
				return new ResultVo.Builder<String>(false).setData("商品期权低于" + goods.getMin_shou() + "手不能下单").build();
			}
		}

		if (user.getAmount() < tradeRecord.getAmount()) {
			return new ResultVo.Builder<String>(false).setData("您的当前账户余额不足购买此交易，建议先去充值").build();
		}

		try {
			int ref = tradeService.trade(tradeRecord, user);
			if (ref > 0) {
				StringBuilder body = new StringBuilder();

				int belongUserId = 0;
				Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
				if (partner != null) {
					belongUserId = partner.getBelongtogmsuer();
				}

				body.append("客户“").append(user.getRealName()).append("”").append(
						" 购买了一份产品[" + tradeRecord.getProductName() + "]，权利金：" + tradeRecord.getAmountFormat() + "元");
				DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.ORDER, belongUserId));

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo.Builder<String>(false).setData("交易失败，请联系我们的客服。").build();
		}

		return new ResultVo.Builder<String>(true).setData("交易成功，请在交易记录中查看详情").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/postTrade", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> postTrade(@RequestParam int categoryId, @RequestParam String productCode,
			@RequestParam int buyAndFall, @RequestParam(required = false, defaultValue = "个股期权") String productName,
			@RequestParam double amount, @RequestParam(required = false, defaultValue = "0") int shou,
			@RequestParam int type, @RequestParam int period, User user) {
		if (user.getAmount() < 20000) {
			return new ResultVo.Builder<String>(false).setData("为了保证询价的可靠性，请确保您账户余额不低于2万").build();
		}

		int unit = 0;
		Goods goods = goodsService.getGoodsByCode(productCode);
		if (goods != null) {
			unit = goods.getUnit();
		}

		if (categoryId == Constant.GoodsCategory.GEGUQIQUAN.getId()) {
			TradeRecord tradeRecord = new TradeRecord();
			tradeRecord.setAmount(amount);
			tradeRecord.setBuyAndFall(buyAndFall);
			tradeRecord.setCategoryId(categoryId);
			tradeRecord.setCreateTime(new Date(System.currentTimeMillis()));
			tradeRecord.setPeriod(period);
			tradeRecord.setProductName(productName);
			tradeRecord.setTradeNo(CommonUtils.getNewTradeSN());
			tradeRecord.setType(type);
			tradeRecord.setUserId(user.getUserId());
			tradeRecord.setProductCode(productCode);
			tradeRecord.setUnit(unit);
			int ref = tradeService.createTrade(tradeRecord);
			if (ref > 0) {
				Job job = new Job();
				job.setJobId("AUTO_XUNJIA_" + tradeRecord.getTradeNo());
				job.setDelayTime((long) (10));
				job.setTaskId(JobTaskIds.AUTO_BAOJIA);
				Map<String, String> params1 = new HashMap<>();
				params1.put("tradeNo", tradeRecord.getTradeNo());
				job.setExtParams(params1);
				JobTracker.instance().put(job);

				StringBuilder body = new StringBuilder();
				int belongUserId = 0;
				Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
				if (partner != null) {
					belongUserId = partner.getBelongtogmsuer();
				}
				body.append("客户“").append(user.getRealName()).append("”").append("提交了一份询价 关于 产品["
						+ tradeRecord.getProductName() + "]，权利金：" + tradeRecord.getAmountFormat() + "元");
				DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.XUNJIA, belongUserId));

				return new ResultVo.Builder<String>(true).setData("询价信息已提交,，客服将在5分钟内给与回复，请您留意询价记录").build();
			}
		} else {

			if (shou < goods.getMin_shou()) {
				return new ResultVo.Builder<String>(false).setData("下单手数不能低于起投手数").build();

			}

			// 手数
			double notionalPrincipal = 0;
			double feilv = period == 15 ? goods.getFeilv_15() : goods.getFeilv_30();
			if (goods.getCategoryId() == Constant.GoodsCategory.SHANGPIN.getId()) {
				Cl cl = clService.getRecentPriceByszlable(goods.getCode());
				if (cl != null) {
					// 起投权利金
					double min_amount =  CommonUtils.floatFormat(cl.getFnewprice()>0?CommonUtils.floatFormat((double)goods.getMin_shou() * (double)goods.getUnit() * cl.getFnewprice() / 100.00 * feilv * Constant.BS):0.00);
					double per_amount =  CommonUtils.floatFormat(goods.getMin_shou()>0?CommonUtils.floatFormat(min_amount / (double)goods.getMin_shou()):0.00);
					amount =  CommonUtils.floatFormat((double)shou * per_amount);
					notionalPrincipal =  CommonUtils.floatFormat(feilv>0?CommonUtils.floatFormat(amount * 100.00 / feilv):0.00);
					
//					double min_amount = CommonUtils.floatFormat(goods.getMin_shou() * goods.getUnit() * cl.getFnewprice() / 100.00 * feilv * Constant.BS);
//					double per_amount = CommonUtils.floatFormat(min_amount / goods.getMin_shou());
//					amount = shou * per_amount;
//					notionalPrincipal = CommonUtils.floatFormat(amount * 100 / feilv);

				}
			} else if (goods.getCategoryId() == Constant.GoodsCategory.ZHISHU.getId()) {
				double min_amount =  CommonUtils.floatFormat(goods.getUnit()>0?CommonUtils.floatFormat((double)goods.getMin_shou() * (double)goods.getUnit() / 100.00 * feilv * Constant.BS):0.00);
				double per_amount =  CommonUtils.floatFormat(goods.getMin_shou()>0?CommonUtils.floatFormat(min_amount / (double)goods.getMin_shou()):0.00);
				amount =  CommonUtils.floatFormat((double)shou * per_amount);
				notionalPrincipal =  CommonUtils.floatFormat(feilv>0?CommonUtils.floatFormat(amount * 100.00 / feilv):0.00);
				
//				double min_amount = CommonUtils.floatFormat(goods.getMin_shou() * goods.getUnit() / 100.00 * feilv * Constant.BS);
//				double per_amount = CommonUtils.floatFormat(min_amount / goods.getMin_shou());
//				amount = shou * per_amount;
//				notionalPrincipal = CommonUtils.floatFormat(amount * 100 / feilv);
			}
			// 直接生成交易
			TradeRecord tradeRecord = new TradeRecord();
			tradeRecord.setAmount(amount);
			tradeRecord.setBuyAndFall(buyAndFall);
			tradeRecord.setCategoryId(categoryId);
			tradeRecord.setCreateTime(new Date(System.currentTimeMillis()));
			tradeRecord.setPeriod(period);
			tradeRecord.setShou(shou);
			tradeRecord.setNotionalPrincipal(notionalPrincipal);
			tradeRecord.setProductName(productName);
			tradeRecord.setTradeNo(CommonUtils.getNewTradeSN());
			tradeRecord.setType(type);
			tradeRecord.setUserId(user.getUserId());
			tradeRecord.setProductCode(productCode);
			tradeRecord.setUnit(unit);
			int ref = tradeService.createTrade(tradeRecord);
			if (ref > 0) {
				VTradeRecord vtradeRecord = tradeService.getVTradeRecordById(ref);
				vtradeRecord.setNotionalPrincipal(notionalPrincipal);
				vtradeRecord.setUpdateTime(new Date(System.currentTimeMillis()));
				vtradeRecord.setStatus(1);
				ref = tradeService.baojia(vtradeRecord);
			}
		}
		return new ResultVo.Builder<String>(true).setData("下单成功，请在已报价中点击购买").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/jiaoyilist", method = RequestMethod.GET)
	public String jiaoyilist(User user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		Page<Order> pages = tradeService.listOrderByUser(user.getUserId(), status, index, 20);
		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		return "customer/jiaoyilist";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/pingcang/{id}", method = RequestMethod.GET)
	public String pingcang(@PathVariable String id, User user, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("user", user);
		model.addAttribute("mobile", CommonUtils.hidePhoneNum(user.getMobile()));
		return "customer/pingcang";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/doPingCang", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doPingCang(@RequestParam String id, @RequestParam String code,
			@RequestParam(required = false, defaultValue = "1") int pingcangType,
			@RequestParam(required = false, defaultValue = "0.00") double zhishu, User user) throws AppServiceException {
		if (CommonUtils.isBlankOrNull(id) || CommonUtils.isBlankOrNull(code)) {
			throw new AppServiceException(StatesUtils.States.params_error, "参数错误");
		}
		String oldcode = CacheManager.instance.getCache(Constant.CacheGroup.MOBILE, user.getMobile());
		if (CommonUtils.isBlankOrNull(code) || (!oldcode.equals(code))) {
			throw new AppServiceException(StatesUtils.States.params_error, "短信验证码不正确");
		}

		Order order = tradeService.getOrderByOrderNo(id);
		if (order == null || order.getUserId() != user.getUserId()
				|| order.getStatus() != Constant.ORDER_STATUS.POSITION.getId()) {
			throw new AppServiceException(StatesUtils.States.params_error, "无此权限或操作已过期");
		}

		if (pingcangType != 3) {
			zhishu = 0;
		}
		int ref = tradeService.pingcang(order.getId(), pingcangType, zhishu);
		// int ref = 0;
		if (ref > 0) {

			StringBuilder body = new StringBuilder();

			int belongUserId = 0;
			Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
			if (partner != null) {
				belongUserId = partner.getBelongtogmsuer();
			}
			body.append("客户“").append(user.getRealName()).append("”").append("进行了一次平仓操作，订单号：" + order.getOrderNo());
			DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.PINGCANG, belongUserId));

			return new ResultVo.Builder<String>(true).setData("操作成功。您可要在【交易记录】中的【平仓记录】里查询。").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("操作失败，请联系客服。").build();
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/chongzhi", method = RequestMethod.GET)
	public String chongzhi(User user, Model model) {

		if (user.getStatus() < 3) {
			model.addAttribute("error", "当前账户尚未通过实名认证，无法使用此功能，请联系您的代理商");
		} else {
			model.addAttribute("vxkey", RandomUtil.getRandomTxt(20));
			model.addAttribute("createTime",
					DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.datetimeFormat2));
			model.addAttribute("user", user);

			PartnerBankInfo partnerBankInfo = partnerService.getPartnerBankInfoOnice(user);
			model.addAttribute("partnerBankInfo", partnerBankInfo);
			
			
			PartnerBankInfo partner = partnerService.getPartnerBankInfoById(user.getPartnerbankinfoId());
			{
				Map txtFields = new HashMap<>();
				txtFields.put("xingming", user.getRealName());
				txtFields.put("dangri",
						DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormat));
				txtFields.put("youxianhehuogongsimingcheng", partner.getCompanyName());
				SignHelper.fillPdf("有限合伙企业合伙协议", user, partner, txtFields);
			}

			{
				Map txtFields = new HashMap<>();
				txtFields.put("Text2", user.getPartnerposition());
				txtFields.put("dangri",
						DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormat));
				txtFields.put("youxianhehuogongsimingcheng", partner.getCompanyName());
				SignHelper.fillPdf("合伙协议补充协议", user, partner, txtFields);

			}
			
			
		}

		return "customer/chongzhi";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/agreement", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> agreement(User user) {

		PartnerBankInfo partner = partnerService.getPartnerBankInfoById(user.getPartnerbankinfoId());
		{
			Map txtFields = new HashMap<>();
			txtFields.put("xingming", user.getRealName());
			txtFields.put("dangri",
					DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormat));
			txtFields.put("youxianhehuogongsimingcheng", partner.getCompanyName());
			SignHelper.signPdf("有限合伙企业合伙协议", user, partner, txtFields);
		}

		{
			Map txtFields = new HashMap<>();
			txtFields.put("Text2", user.getPartnerposition());
			txtFields.put("dangri",
					DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.dateFormat));
			txtFields.put("youxianhehuogongsimingcheng", partner.getCompanyName());
			SignHelper.signPdf("合伙协议补充协议", user, partner, txtFields);

		}
		
		userService.updateColById("f_agreementTime", new Date(System.currentTimeMillis()), user.getUserId());

		return new ResultVo.Builder<String>(true).setData("SUCCESS").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/chongzhi", method = RequestMethod.POST)
	public String doChongzhi(User user, @RequestParam(required = false, defaultValue = "0") double amount,
			@RequestParam(required = false, defaultValue = "") String bankcardname,
			@RequestParam(required = false, defaultValue = "") String vxkey, Model model) {

		model.addAttribute("amount", amount);

		PartnerBankInfo partnerBankInfo = partnerService.getPartnerBankInfoOnice(user);
		if (partnerBankInfo != null) {
			model.addAttribute("partnerBankInfo", partnerBankInfo);
			RechargeRecord rechargeRecord = new RechargeRecord();
			rechargeRecord.setAmount(amount);
			rechargeRecord.setUserId(user.getUserId());
			rechargeRecord.setBankName(partnerBankInfo.getBankName());
			rechargeRecord.setBankNo(partnerBankInfo.getBankNo());
			rechargeRecord.setPartnerId(partnerBankInfo.getPartnerId());
			rechargeRecord.setCompanyName(partnerBankInfo.getCompanyName());
			rechargeRecord.setRechargeNo(CommonUtils.getNewRechargeSN());
			rechargeRecord.setBankcardname(bankcardname);
			// 生成充值订单
			tradeService.createRechargeRecord(rechargeRecord);
		}

		return "customer/chongzhiresult";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/rechargelist", method = RequestMethod.GET)
	public String rechargeList(User user, Model model, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index) {
		Page<RechargeRecord> page = tradeService.listRechargeRecordByUser(user.getUserId(), status, index, 20);
		model.addAttribute("page", page);
		model.addAttribute("status", status);
		model.addAttribute("index", index);

		return "customer/rechargelist";
	}

	@RequestMapping(value = "/moban", method = RequestMethod.GET)
	public String moban(@RequestParam String path, Model model) {
		model.addAttribute("path", path);
		return "customer/moban";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public String changepwd() {
		return "/customer/changepwd";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/doChangePwd", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doChangePwd(User user,
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

			userService.updateColById("f_password_hash", CipherUtil.buildPasswordHash(newpwd), user.getUserId());
			return new ResultVo.Builder<String>(true).setData("新密码修改成功").build();
		} else {
			throw new AppServiceException(StatesUtils.States.params_error, "原密码不正确");
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/withdrawCash", method = RequestMethod.GET)
	public String withdrawCash(User user, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("mobile", CommonUtils.hidePhoneNum(user.getMobile()));
		return "customer/withdrawCash";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/withdrawCashlist", method = RequestMethod.GET)
	public String withdrawCashlist(User user, Model model,
			@RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index) {

		List<String> userIds = new ArrayList<>();
		userIds.add(user.getUserId() + "");
		Page<CashRecord> page = tradeService.listCashRecordByStatus(status, userIds, index, 999,null);
		model.addAttribute("page", page);
		model.addAttribute("index", index);
		model.addAttribute("status", status);
		return "customer/withdrawCashlist";
	}

	@SessionScope(Constant.SESSION_KEY.currentUser)
	@RegistAuthority(AuthorityType.CUSTOMER)
	@RequestMapping(value = "/json/withdrawCash", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> withdrawCash(@RequestParam(required = false, defaultValue = "") String code,
			User user) throws AppServiceException {
		if (CommonUtils.isBlankOrNull(code)) {
			throw new AppServiceException(StatesUtils.States.params_error, "参数错误");
		}
		String oldcode = CacheManager.instance.getCache(Constant.CacheGroup.MOBILE, user.getMobile());
		if (CommonUtils.isBlankOrNull(code) || (!oldcode.equals(code))) {
			throw new AppServiceException(StatesUtils.States.params_error, "短信验证码不正确");
		}

		if (user.getAmount() <= 0) {
			throw new AppServiceException(StatesUtils.States.params_error, "账户没有余额，不能提现。");
		}

		try {
			int ref = tradeService.withdrawCash(user, user.getAmount());
			if (ref > 0) {

				StringBuilder body = new StringBuilder();

				int belongUserId = 0;
				Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
				if (partner != null) {
					belongUserId = partner.getBelongtogmsuer();
				}
				body.append("客户“").append(user.getRealName()).append("”").append("申请提现，提现金额：" + user.getAmountFormat())
						.append("元。");
				DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.WITHDRAWCASH, belongUserId));

				return new ResultVo.Builder<String>(true).setData("提现申请已收到，工作日内24小时到账，非工作日顺延。").build();
			} else {
				return new ResultVo.Builder<String>(false).setData("操作失败，请联系客服。").build();
			}
		} catch (Exception e) {

			e.printStackTrace();
			return new ResultVo.Builder<String>(false).setData("操作失败，请联系客服。").build();
		}

	}
}
