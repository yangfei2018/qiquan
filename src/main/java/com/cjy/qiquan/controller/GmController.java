package com.cjy.qiquan.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.model.GoodsCompare;
import com.cjy.qiquan.model.HzList;
import com.cjy.qiquan.model.Order;
import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.RechargeRecord;
import com.cjy.qiquan.model.SystemLog;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.UserSession;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.model.VRechargeRecord;
import com.cjy.qiquan.model.VTradeRecord;
import com.cjy.qiquan.model.VUser;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.service.ClService;
import com.cjy.qiquan.service.DBService;
import com.cjy.qiquan.service.GmService;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.SystemLogService;
import com.cjy.qiquan.service.TradeService;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.task.SendSmsTask;
import com.cjy.qiquan.task.TaskFacotry;
import com.cjy.qiquan.task.UpdateSignAccount;
import com.cjy.qiquan.task.UpdateUnReadMessage;
import com.cjy.qiquan.utils.CipherUtil;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.Constant.AuthorityType;
import com.cjy.qiquan.utils.Constant.MAINID;
import com.cjy.qiquan.utils.DateFormater;
import com.cjy.qiquan.utils.RegistAuthority;
import com.cjy.qiquan.utils.SessionScope;
import com.cjy.qiquan.utils.StatesUtils;
import com.cjy.qiquan.web.WebContext;
import com.cjy.qiquan.web.utils.CookieUtils;

@Controller
@RequestMapping("/gm")
public class GmController {

	@Autowired
	private GmService gmService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private UserService userService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private SystemLogService systemLogService;

	@Autowired
	private ClService clService;

	@RequestMapping("/login")
	public String login() {
		return "gm/login";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/changepwd", method = RequestMethod.GET)
	public String changepwd() {
		return "gm/changepwd";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/doChangePwd", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> doChangePwd(GmUser user,
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

			gmService.updateColById("f_password_hash", CipherUtil.buildPasswordHash(newpwd), user.getUserId());
			return new ResultVo.Builder<String>(true).setData("新密码修改成功").build();
		} else {
			throw new AppServiceException(StatesUtils.States.params_error, "原密码不正确");
		}
	}

	@RequestMapping("/logout")
	public String logout() {
		WebContext.getSession().removeAttribute(Constant.SESSION_KEY.currentGmUser);
		WebContext.delCookie(Constant.SESSION_KEY.currentGmUser);
		return "redirect:/gm/login";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/index")
	public String index(GmUser user, Model model) {
		model.addAttribute("user", user);
		if (user.getIsAdmin() == 1) {
			Page<SystemLog> pages = systemLogService.listPaged(0, 1, 20);
			model.addAttribute("messages", pages);
		} else {
			Page<SystemLog> pages = systemLogService.listPaged(user.getUserId(), 1, 20);
			model.addAttribute("messages", pages);
		}

		return "gm/index";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/json/checkUnReadMessage")
	public @ResponseBody List<SystemLog> checkUnReadMessage(GmUser user) {
		return systemLogService.listUnreadMessage(user.getUserId());
	}
	
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/json/updateSignData/{id}")
	public @ResponseBody ResultVo<String> updateSignData(@PathVariable int id){
		
		TaskFacotry.instance().put(new UpdateSignAccount(1, id, 2));		
		return new ResultVo.Builder<String>(true).setData("SUCCESS").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/partnerlist")
	public String partnerlist(GmUser user, Model model) {
		model.addAttribute("user", user);
		int uId = 0;
		if (user.getPositionId() == Constant.GM_ROLE.OPER.getId()) {
			uId = user.getUserId();
		}
		Page<Partner> page = partnerService.listPartnerByGmUserId(uId, 1, 9999);
		model.addAttribute("page", page);
		return "gm/partnerlist";
	}

	@RequestMapping(value = "/json/doLogin", method = RequestMethod.POST)
	public @ResponseBody GmUser doLogin(@RequestParam(required = false, defaultValue = "") String account,
			@RequestParam(required = false, defaultValue = "") String password, HttpServletRequest req,
			HttpServletResponse resp) throws AppServiceException {
		GmUser user = gmService.getUserByName(account);
		if (user == null) {
			throw new AppServiceException(StatesUtils.States.params_error, "用户尚未注册");
		}
		if (!CipherUtil.buildPasswordHash(password).equals(user.getPassword_hash())) {
			throw new AppServiceException(StatesUtils.States.forbidden_user, "用户密码不正确");
		}
		setUserSession(user, req, resp);
		return user;
	}

	@RequestMapping(value = "/exportTradeToExcel")
	public void exportTradeToExcel(@RequestParam(required = false, defaultValue = "0") int status,
			HttpServletResponse response) {
		Page<VOrder> pages = tradeService.listVOrderByStatus(status, null, 1, 9999, "",null);
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("交易订单");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("订单号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("客户");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("产品名称");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("产品代码");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("成交价格");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("期权金");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("名义本金");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("合同开始日期");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("合同终止日期");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("实时权益");
		cell.setCellStyle(style);
		cell = row.createCell(10);
		cell.setCellValue("结算金额");
		cell.setCellStyle(style);
		List<VOrder> list = pages.getList();
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			VOrder order = list.get(i);
			row.createCell(0).setCellValue(order.getOrderNo());
			row.createCell(1).setCellValue(order.getBuyerName());
			row.createCell(2).setCellValue(order.getProductName());
			row.createCell(3).setCellValue(order.getProductCode());
			if (order.getTradeAmount() == 0) {
				row.createCell(4).setCellValue("");
			} else {
				row.createCell(4).setCellValue(order.getTradeAmount());
			}
			row.createCell(5).setCellValue(order.getAmount());
			row.createCell(6).setCellValue(order.getNotionalPrincipal());
			row.createCell(7).setCellValue(order.getOrderStartTime() + "");
			row.createCell(8).setCellValue(order.getOrderEndTime() + "");
			row.createCell(9).setCellValue(" ");
			row.createCell(10).setCellValue(" ");
		}
		OutputStream fOut = null;
		try {
			response.setContentType("application/vnd.ms-excel");
			String codedFileName = "交易订单_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
					DateFormater.datetimeFormatNoSplit);
			codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
			response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
			fOut = response.getOutputStream();
			wb.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				wb.close();
				fOut.flush();
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/createPartner", method = RequestMethod.GET)
	public String partnerView(GmUser user, Model model) {
		Partner partner = new Partner();
		model.addAttribute("edit", 1);
		model.addAttribute("create", 1);
		model.addAttribute("partner", partner);

		return "gm/partnerView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/partnerView/{id}", method = RequestMethod.GET)
	public String partnerView(@PathVariable String id, @RequestParam(required = false, defaultValue = "0") int edit,
			GmUser user, Model model) {
		Partner partner = partnerService.getPartnerByNo(id);
		model.addAttribute("edit", edit);
		model.addAttribute("create", 0);
		if (partner.getBelongtogmsuer() != user.getUserId() && user.getIsAdmin() == 0
				&& user.getPositionId() != Constant.GM_ROLE.OP_MASTER.getId()) {
			model.addAttribute("error", "无此权限");
		} else {
			model.addAttribute("partner", partner);
		}
		return "gm/partnerView";
	}

	//
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/partnerView/{id}/bankinfo", method = RequestMethod.GET)
	public String partnerViewBankinfo(@PathVariable String id,
			@RequestParam(required = false, defaultValue = "0") int edit, GmUser user, Model model) {
		Partner partner = partnerService.getPartnerByNo(id);
		if (partner.getBelongtogmsuer() != user.getUserId() && user.getIsAdmin() == 0
				&& user.getPositionId() != Constant.GM_ROLE.OP_MASTER.getId()) {
			model.addAttribute("error", "无此权限");
		} else {
			model.addAttribute("partner", partner);

			List<PartnerBankInfo> bankInfoList = partnerService.listPartnerBankByPartnerId(partner.getUserId());
			model.addAttribute("bankInfoList", bankInfoList);
			model.addAttribute("partnerId", partner.getUserId());
		}
		return "gm/partnerViewbankinfo";
	}

	/**
	 * companyName:companyName, bankName:bankName, bankNo:bankNo
	 * 
	 * @param user
	 * @param id
	 * @param id
	 * @param id
	 * @param id
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updatePartnerBankInfo", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updatePartner(GmUser user, @RequestParam int id,
			@RequestParam(required = false, defaultValue = "") String companyName,
			@RequestParam(required = false, defaultValue = "") String bankName,
			@RequestParam(required = false, defaultValue = "") String bankNo,
			@RequestParam(required = false, defaultValue = "") String realName,
			@RequestParam(required = false, defaultValue = "") String hehuoName,
			@RequestParam(required = false, defaultValue = "") String address,
			@RequestParam(required = false, defaultValue = "") String idCard,
			@RequestParam(required = false, defaultValue = "") String organCode,
			
			@RequestParam(required = false, defaultValue = "0") int partnerId) {

		if (id == 0) {
			PartnerBankInfo partnerBankInfo = new PartnerBankInfo();
			partnerBankInfo.setBankName(bankName);
			partnerBankInfo.setBankNo(bankNo);
			partnerBankInfo.setCompanyName(companyName);
			partnerBankInfo.setUsedCount(0);
			partnerBankInfo.setPartnerId(partnerId);
			partnerBankInfo.setRealName(realName);
			partnerBankInfo.setIdCard(idCard);
			partnerBankInfo.setOrganCode(organCode);
			partnerBankInfo.setHehuoName(hehuoName);
			partnerBankInfo.setAddress(address);
			partnerService.createPartnerBankInfo(partnerBankInfo);
			TaskFacotry.instance().put(new UpdateSignAccount(1, partnerBankInfo.getId(), 1));
		} else {
			PartnerBankInfo partnerBankInfo = partnerService.getPartnerBankInfoById(id);
			if (partnerBankInfo == null) {
				return new ResultVo.Builder<String>(false).setData("无效的记录").build();
			}
			boolean change = (!companyName.equals(partnerBankInfo.getCompanyName())) || (CommonUtils.isBlankOrNull(partnerBankInfo.getOrganCode())&& CommonUtils.hasText(organCode));
			partnerBankInfo.setBankName(bankName);
			partnerBankInfo.setBankNo(bankNo);
			partnerBankInfo.setCompanyName(companyName);
			partnerBankInfo.setRealName(realName);
			partnerBankInfo.setHehuoName(hehuoName);
			partnerBankInfo.setAddress(address);
			partnerBankInfo.setIdCard(idCard);
			partnerBankInfo.setOrganCode(organCode);
			partnerService.updatePartnerBankInfoById(partnerBankInfo);
			if (change) {
				TaskFacotry.instance().put(new UpdateSignAccount(1, partnerBankInfo.getId(), 2));
			}
		}

		return new ResultVo.Builder<String>(true).setData("更新成功").build();
	}
	
	
	//resetPasswordPartner
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/resetPasswordPartner", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> resetPasswordPartner(GmUser user,
			@RequestParam(required = false, defaultValue = "0") int userId){
		Partner partner = partnerService.getPartnerById(userId);
		if (partner!=null) {
			
			partnerService.updateColById("f_password_hash", CipherUtil.buildPasswordHash("1111"), partner.getUserId());
			return new ResultVo.Builder<String>(true).setData("SUCCESS").build();
		}
		
		return new ResultVo.Builder<String>(false).setData("无效的参数").build();

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updatePartner", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updatePartner(GmUser user,
			@RequestParam(required = false, defaultValue = "0") int userId,
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "") String password,
			@RequestParam(required = false, defaultValue = "") String realName,
			@RequestParam(required = false, defaultValue = "") String partnerNo,
			@RequestParam(required = false, defaultValue = "") String companyName,
			@RequestParam(required = false, defaultValue = "") String mobile,
			@RequestParam(required = false, defaultValue = "") String bankOfDeposit,
			@RequestParam(required = false, defaultValue = "") String bankCardNo,
			@RequestParam(required = false, defaultValue = "") String idCardFrontPic,
			@RequestParam(required = false, defaultValue = "") String businessLicencePic,
			@RequestParam(required = false, defaultValue = "") String idCardBackgroundPic,
			@RequestParam(required = false, defaultValue = "") String organCode,
			@RequestParam(required = false, defaultValue = "") String idCard,
			@RequestParam(required = false, defaultValue = "") String bankhm,
			@RequestParam(required = false, defaultValue = "") String code) {
		Partner partner = null;
		int ref = -1;
		if (CommonUtils.isBlankOrNull(organCode) || CommonUtils.isBlankOrNull(idCard)) {
			return new ResultVo.Builder<String>(false).setData("组织结构代码号或法人身份证不能为空").build();
		}
		if (userId == 0) {
			// 表示新增
			{
				Partner oPartner = partnerService.getPartnerByName(name);
				if (oPartner != null) {
					return new ResultVo.Builder<String>(false).setData("更新失败，代理商登录名已被占用。").build();
				}
			}
			{
				Partner oPartner = partnerService.getPartnerByCode(code);
				if (oPartner != null) {
					return new ResultVo.Builder<String>(false).setData("更新失败，客户邀请码已被占用。").build();
				}
			}

			partner = new Partner();
			partner.setName(name);
			partner.setPassword_hash(CipherUtil.buildPasswordHash(password));
			partner.setCode(code);
			partner.setRealName(realName);
			partner.setIdCard(idCard);
			partner.setBankOfDeposit(bankOfDeposit);
			partner.setBusinessLicencePic(businessLicencePic);
			partner.setBelongtogmsuer(user.getUserId());
			partner.setCompanyName(companyName);
			partner.setIdCardBackgroundPic(idCardBackgroundPic);
			partner.setIdCardFrontPic(idCardFrontPic);
			partner.setMobile(mobile);
			partner.setBankCardNo(bankCardNo);
			partner.setPartnerNo(partnerNo);
			partner.setOrganCode(organCode);
			partner.setBankhm(bankhm);
			ref = partnerService.createPartner(partner);
			TaskFacotry.instance().put(new UpdateSignAccount(1, ref, 1));
		} else {
			partner = partnerService.getPartnerById(userId);
			boolean change = (!partner.getRealName().equals(realName) || !partner.getCompanyName().equals(companyName));
			partner.setRealName(realName);
			partner.setBankOfDeposit(bankOfDeposit);
			partner.setBankCardNo(bankCardNo);
			partner.setIdCard(idCard);
			partner.setBusinessLicencePic(businessLicencePic);
			// partner.setBelongtogmsuer(user.getUserId());
			partner.setCompanyName(companyName);
			partner.setIdCardBackgroundPic(idCardBackgroundPic);
			partner.setIdCardFrontPic(idCardFrontPic);
			partner.setMobile(mobile);
			partner.setPartnerNo(partnerNo);
			partner.setOrganCode(organCode);
			partner.setBankhm(bankhm);
			ref = partnerService.updatePartner(partner);
//			TaskFacotry.instance().put(new UpdateSignAccount(1, partner.getUserId(), 1));
			if (change) {
				TaskFacotry.instance().put(new UpdateSignAccount(1, partner.getUserId(), 2));
			}
		}
		if (ref > 0) {
			return new ResultVo.Builder<String>(true).setData("更新成功").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("更新失败").build();
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/userlist")
	public String userList(GmUser user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "0") int partnerId,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		model.addAttribute("user", user);
		model.addAttribute("partnerId", partnerId);
		model.addAttribute("role_id", user.getPositionId());
		Page<VUser> page = null;
		if (user.getIsAdmin() == 1 || user.getPositionId() == Constant.GM_ROLE.OP_MASTER.getId() || user.getPositionId() == Constant.GM_ROLE.CAIWU.getId()) {
			List<String> partnerCodes = new ArrayList<>();
			if (partnerId > 0) {
				Partner partner = partnerService.getPartnerById(partnerId);
				partnerCodes.add(partner.getCode());
			}

			page = userService.listUserPagedByStatusId(status, partnerCodes, index, 9999);
			model.addAttribute("partners", partnerService.listPartnerByGmUserId(0, 1, 99999));
		} else {
			// 普通管理员，先获取合作方信息
			Page<Partner> partners = partnerService.listPartnerByGmUserId(user.getUserId(), 1, 99999);
			model.addAttribute("partners", partners);
			List<String> partnerCodes = new ArrayList<>();
			for (Partner partner : partners.getList()) {
				if (partnerId > 0 && partner.getUserId() == partnerId) {
					partnerCodes.add(partner.getCode());
				} else {
					partnerCodes.add(partner.getCode());
				}
			}
			page = userService.listUserPagedByStatusId(status, partnerCodes, index, 9999);
		}
		model.addAttribute("page", page);
		model.addAttribute("status", status);

		if (status == 2) {
			DBService.instance().put(new UpdateUnReadMessage(user.getUserId(), MAINID.HUIYUANRENZHENG));

		}

		return "gm/userlist";
	}

	// private static EsignsdkService SDK = EsignsdkServiceFactory.instance();
	// private AccountService SERVICE = AccountServiceFactory.instance();

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/userView/{id}", method = RequestMethod.GET)
	public String userView(@PathVariable int id, @RequestParam(required = false, defaultValue = "0") int chongzhi,
			GmUser user, Model model) {
		VUser customer = userService.getVUserById(id);
		model.addAttribute("chongzhi", chongzhi);
		model.addAttribute("customer", customer);
		return "gm/userView";
	}

	// static {
	// Result result = SDK.init(ProjectConstants.PROJECTID,
	// ProjectConstants.PROJECT_SECRET);
	// if (0 == result.getErrCode()) {
	// LoginResult loginResult = SDK.login();
	// if (0 == loginResult.getErrCode()) {
	//
	// }
	// }
	//
	// }

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/auditUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> auditUser(@RequestParam int userId,
			@RequestParam(required = false, defaultValue = "3") int status,
			@RequestParam(required = false, defaultValue = "") String reason, // 驳回理由
			GmUser user) {
		User customer = userService.getUserById(userId);
		if (customer != null) {
			userService.updateColById("f_status", status, userId);
			userService.updateColById("f_statusChangeTime", new Date(System.currentTimeMillis()), userId);
			
			if (status == 3) {
				// 认证通过
				TaskFacotry.instance().put(new UpdateSignAccount(0, customer.getUserId(), 1));
			}

			if (CommonUtils.hasText(reason)) {
				CacheManager.instance.addCache(Constant.CacheGroup.USER_REASON, String.valueOf(userId), reason);
			}
		}
		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}
	
	
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/banUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> banUser(@RequestParam int userId,
			@RequestParam(required = false, defaultValue = "0") int ban,
			@RequestParam(required = false, defaultValue = "") String reason, // 驳回理由
			GmUser user) {
		User customer = userService.getUserById(userId);
		if (customer != null) {
			userService.updateColById("f_ban", ban==0?1:0, userId);
			userService.updateColById("f_statusChangeTime", new Date(System.currentTimeMillis()), userId);
		}
		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updateUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updateUser(@RequestParam int userId,
			@RequestParam(required = false, defaultValue = "0") double amount, GmUser user) {
		User customer = userService.getUserById(userId);
		if (customer != null) {
			Partner partner = partnerService.getPartnerByCode(customer.getPartnerCode());
			RechargeRecord rechargeRecord = new RechargeRecord();
			rechargeRecord.setAmount(amount);
			rechargeRecord.setUserId(customer.getUserId());
			rechargeRecord.setBankName("");
			rechargeRecord.setBankNo("");
			rechargeRecord.setPartnerId(partner.getUserId());
			rechargeRecord.setCompanyName("");
			rechargeRecord.setRechargeNo(CommonUtils.getNewRechargeSN());
			rechargeRecord.setBankcardname("");
			// 生成充值订单
			tradeService.createRechargeRecord(rechargeRecord);

		}
		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}
	//

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping("/goodslist")
	public String goodsList(GmUser user, @RequestParam(required = false, defaultValue = "0") int categoryid,
			@RequestParam(required = false, defaultValue = "1") int index, Model model) {
		model.addAttribute("user", user);
		List<Goods> goodsList = goodsService.listGoodsByCategoryId(0);

		model.addAttribute("goodsList", goodsList);
		model.addAttribute("categoryid", categoryid);
		return "gm/goodslist";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/tradelist", method = RequestMethod.GET)
	public String tradelist(GmUser user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index, 
			@RequestParam(required = false, defaultValue = "0") int excel, 
			HttpServletResponse response,
			Model model) {

		Page<VTradeRecord> pages = null;
		if (user.getIsAdmin() == 1 || user.getPositionId() == Constant.GM_ROLE.OP_MASTER.getId()) {
			pages = tradeService.listVTradeByStatus(status, null, index, 9999);
		} else {
			// 普通管理员，先获取合作方信息
			Page<Partner> partners = partnerService.listPartnerByGmUserId(user.getUserId(), 1, 99999);
			List<String> partnerCodes = new ArrayList<>();
			for (Partner partner : partners.getList()) {
				partnerCodes.add(partner.getCode());
			}

			List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
			pages = tradeService.listVTradeByStatus(status, userIds, index, 9999);
		}

		if (excel==0) {
			model.addAttribute("page", pages);
			model.addAttribute("status", status);
			model.addAttribute("index", index);
			DBService.instance().put(new UpdateUnReadMessage(user.getUserId(), MAINID.XUNJIA));
			return "gm/tradelist";
		}else {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("询价明细");
			HSSFRow row = sheet.createRow((int) 0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("名称");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("	代码");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("涨跌	");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("权利金（元）	");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("期限	");
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("名义本金（元）");
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellValue("期权类型");
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellValue("询价时间");
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellValue("报单方式");
			cell.setCellStyle(style);

			List<VTradeRecord> list = pages.getList();
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow((int) i + 1);
				VTradeRecord order = list.get(i);
				row.createCell(0).setCellValue(order.getProductName());
				row.createCell(1).setCellValue(order.getProductCode());
				row.createCell(2).setCellValue(order.getBuyAndFall()==1?"涨":"跌");
				row.createCell(3).setCellValue(order.getAmount());
				row.createCell(4).setCellValue(order.getPeriod());
				row.createCell(5).setCellValue(order.getNotionalPrincipal());
				row.createCell(6).setCellValue(order.getType()==1?"美式":"欧式");
				row.createCell(6).setCellValue(order.getCreateTimeFormat());
				row.createCell(8).setCellValue(order.getBuyType()==1?"实时下单":"收盘价下单");
			}
			OutputStream fOut = null;
			try {
				response.setContentType("application/vnd.ms-excel");
				String codedFileName = "询价明细_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
						DateFormater.datetimeFormatNoSplit);
				codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
				fOut = response.getOutputStream();
				wb.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					wb.close();
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return null;
		}
		
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/tradeView/{id}", method = RequestMethod.GET)
	public String tradeView(@PathVariable String id, @RequestParam(required = false, defaultValue = "0") int baojia,
			GmUser user, Model model) {
		VTradeRecord tradeRecord = tradeService.getVTradeRecordByTradeNo(id);
		model.addAttribute("tradeRecord", tradeRecord);
		model.addAttribute("id", id);
		model.addAttribute("baojia", baojia);
		return "gm/tradeView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/baojia", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> baojia(@RequestParam String tradeNo,
			@RequestParam(required = false, defaultValue = "0") double notionalPrincipalFormat, GmUser user) {
		VTradeRecord tradeRecord = tradeService.getVTradeRecordByTradeNo(tradeNo);
		if (tradeRecord.getStatus() != 0) {
			return new ResultVo.Builder<String>(false).setData("当前状态不能报价").build();
		}

		tradeRecord.setNotionalPrincipal(notionalPrincipalFormat);
		tradeRecord.setUpdateTime(new Date(System.currentTimeMillis()));
		tradeRecord.setStatus(1);

		int ref = tradeService.baojia(tradeRecord);
		if (ref > 0) {
			return new ResultVo.Builder<String>(true).setData("报价成功").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("报价失败，请联系管理员").build();
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/orderlist", method = RequestMethod.GET)
	public String orderlist(GmUser user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index,
			@RequestParam(required = false, defaultValue = "0") int excel,
			@RequestParam(required = false, defaultValue = "0") int hz,
			@RequestParam(required = false, defaultValue = "") String ids,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			HttpServletResponse response, Model model) {
		Map map = Maps.newHashMap();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		Page<VOrder> pages = null;
		if (user.getIsAdmin() == 1 || user.getPositionId() == Constant.GM_ROLE.OP_MASTER.getId()) {
			pages = tradeService.listVOrderByStatus(status, null, index, 9999, ids,map);
		} else {
			// 普通管理员，先获取合作方信息
			Page<Partner> partners = partnerService.listPartnerByGmUserId(user.getUserId(), 1, 99999);
			List<String> partnerCodes = new ArrayList<>();
			for (Partner partner : partners.getList()) {
				partnerCodes.add(partner.getCode());
			}

			List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
			pages = tradeService.listVOrderByStatus(status, userIds, index, 9999, ids,map);
		}

		if (excel == 0) {

			model.addAttribute("page", pages);
			model.addAttribute("status", status);
			model.addAttribute("index", index);
			DBService.instance().put(new UpdateUnReadMessage(user.getUserId(), MAINID.ORDER));
			if (status == 2) {
				DBService.instance().put(new UpdateUnReadMessage(user.getUserId(), MAINID.PINGCANG));
			}

			return "/gm/orderlist";
		} else {

			if (hz == 1) {
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("交易记录(汇总）");
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

				HSSFCell cell = row.createCell(0);
				cell.setCellValue("期权买卖方向");
				cell.setCellStyle(style);

				cell = row.createCell(1);
				cell.setCellValue("名称");
				cell.setCellStyle(style);

				cell = row.createCell(2);
				cell.setCellValue("涨跌");
				cell.setCellStyle(style);

				cell = row.createCell(3);
				cell.setCellValue("权利金（元）");
				cell.setCellStyle(style);

				cell = row.createCell(4);
				cell.setCellValue("期限");
				cell.setCellStyle(style);

				cell = row.createCell(5);
				cell.setCellValue("合同开始时间");
				cell.setCellStyle(style);

				cell = row.createCell(6);
				cell.setCellValue("合同结束时间");
				cell.setCellStyle(style);

				cell = row.createCell(7);
				cell.setCellValue("名义本金/手数");
				cell.setCellStyle(style);

				cell = row.createCell(8);
				cell.setCellValue("成交价格（元）");
				cell.setCellStyle(style);

				cell = row.createCell(9);
				cell.setCellValue("行权价格（元）");
				cell.setCellStyle(style);

				cell = row.createCell(10);
				cell.setCellValue("报价方式");
				cell.setCellStyle(style);

				cell = row.createCell(11);
				cell.setCellValue("手数");
				cell.setCellStyle(style);

				List<VOrder> list = pages.getList();

				HzList hzList = new HzList();
				hzList.setList(list);

				for (int i = 0; i < hzList.getSize(); i++) {
					row = sheet.createRow((int) i + 1);
					VOrder order = hzList.get(i);

					if (status == 0 || status == 2) {
						row.createCell(0).setCellValue(status == 0 ? "买入" : "行权");
					}

					row.createCell(1).setCellValue(order.getProductName());

					row.createCell(2).setCellValue(order.getBuyAndFall() == 1 ? "看涨" : "看跌");
					row.createCell(3).setCellValue(order.getAmount());
					row.createCell(4).setCellValue(order.getPeriod());
					if (status == 0) {
						// 待成交时候，自动算出合同日期
						Goods goods = goodsService.getGoodsByCode(order.getProductCode());
						if (goods != null) {
							row.createCell(5).setCellValue(DateFormater.simpleDateFormat(
									new Date(System.currentTimeMillis()), DateFormater.dateFormatNoSplit));
							if (order.getPeriod() == 15) {
								row.createCell(6).setCellValue(goods.getFeilv_15_time());
							} else {
								row.createCell(6).setCellValue(goods.getFeilv_30_time());
							}
						}

					} else {
						row.createCell(5).setCellValue(order.getOrderStartTime());
						row.createCell(6).setCellValue(order.getOrderEndTime());
					}

					row.createCell(7).setCellValue(order.getNotionalPrincipal());

					row.createCell(8).setCellValue(order.getTradeAmount() == 0 ? "" : order.getTradeAmount() + "");
					row.createCell(9).setCellValue(order.getBalanceAmount() == 0 ? "" : order.getBalanceAmount() + "");

					row.createCell(10).setCellValue(order.getBuyType() == 1 ? "实时下单" : "收盘价下单");
					row.createCell(11).setCellValue(order.getShou());

				}
				OutputStream fOut = null;
				try {
					response.setContentType("application/vnd.ms-excel");
					String codedFileName = "交易记录(汇总)_" + DateFormater
							.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.datetimeFormatNoSplit);
					codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
					response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
					fOut = response.getOutputStream();
					wb.write(fOut);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						wb.close();
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} else {

				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("交易记录");
				HSSFRow row = sheet.createRow((int) 0);
				HSSFCellStyle style = wb.createCellStyle();
				style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

				int addRow = 0;
				if (status == 0 || status == 2) {
					addRow = 1;
					HSSFCell cell = row.createCell(0);
					cell.setCellValue("期权买卖方向");
					cell.setCellStyle(style);
				}

				HSSFCell cell = row.createCell(0 + addRow);
				cell.setCellValue("订单号	");
				cell.setCellStyle(style);

				cell = row.createCell(1 + addRow);
				cell.setCellValue("代理商	");
				cell.setCellStyle(style);

				cell = row.createCell(2 + addRow);
				cell.setCellValue("购买者	");
				cell.setCellStyle(style);

				cell = row.createCell(3 + addRow);
				cell.setCellValue("名称");
				cell.setCellStyle(style);

				cell = row.createCell(4 + addRow);
				cell.setCellValue("代码");
				cell.setCellStyle(style);

				cell = row.createCell(5 + addRow);
				cell.setCellValue("涨跌");
				cell.setCellStyle(style);

				cell = row.createCell(6 + addRow);
				cell.setCellValue("权利金（元）");
				cell.setCellStyle(style);

				cell = row.createCell(7 + addRow);
				cell.setCellValue("期限");
				cell.setCellStyle(style);

				cell = row.createCell(8 + addRow);
				cell.setCellValue("合同开始时间");
				cell.setCellStyle(style);

				cell = row.createCell(9 + addRow);
				cell.setCellValue("合同结束时间");
				cell.setCellStyle(style);

				cell = row.createCell(10 + addRow);
				cell.setCellValue("名义本金/手数");
				cell.setCellStyle(style);

				// cell = row.createCell(8+addRow);
				// cell.setCellValue("期权类型");
				// cell.setCellStyle(style);
				//
				// cell = row.createCell(9+addRow);
				// cell.setCellValue("购买时间");
				// cell.setCellStyle(style);

				cell = row.createCell(11 + addRow);
				cell.setCellValue("成交价格（元）");
				cell.setCellStyle(style);

				cell = row.createCell(12 + addRow);
				cell.setCellValue("行权价格（元）");
				cell.setCellStyle(style);

				cell = row.createCell(13 + addRow);
				cell.setCellValue("报价方式");
				cell.setCellStyle(style);

				if (status == 2 || status == 3) {
					cell = row.createCell(14 + addRow);
					cell.setCellValue("行权方式");
					cell.setCellStyle(style);
				}

				if (status == 0 || status == 2) {
					cell = row.createCell(15 + addRow);
					cell.setCellValue("手数");
					cell.setCellStyle(style);
				}

				List<VOrder> list = pages.getList();
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow((int) i + 1);
					VOrder order = list.get(i);
					if (status == 0 || status == 2) {
						row.createCell(0).setCellValue(status == 0 ? "买入" : "行权");
					}
					row.createCell(0 + addRow).setCellValue(order.getOrderNo());
					row.createCell(1 + addRow).setCellValue(order.getBuyerPartnerCompanyName());

					row.createCell(2 + addRow).setCellValue(order.getBuyerName());
					row.createCell(3 + addRow).setCellValue(order.getProductName());
					row.createCell(4 + addRow).setCellValue(order.getProductCode());
					row.createCell(5 + addRow).setCellValue(order.getBuyAndFall() == 1 ? "看涨" : "看跌");
					row.createCell(6 + addRow).setCellValue(order.getAmount());
					row.createCell(7 + addRow).setCellValue(order.getPeriod());
					if (status == 0) {
						// 待成交时候，自动算出合同日期
						Goods goods = goodsService.getGoodsByCode(order.getProductCode());
						if (goods != null) {
							row.createCell(8 + addRow).setCellValue(DateFormater.simpleDateFormat(
									new Date(System.currentTimeMillis()), DateFormater.dateFormatNoSplit));
							if (order.getPeriod() == 15) {
								row.createCell(9 + addRow).setCellValue(goods.getFeilv_15_time());
							} else {
								row.createCell(9 + addRow).setCellValue(goods.getFeilv_30_time());
							}
						}

					} else {
						row.createCell(8 + addRow).setCellValue(order.getOrderStartTime());
						row.createCell(9 + addRow).setCellValue(order.getOrderEndTime());
					}

					row.createCell(10 + addRow).setCellValue(status==0?order.getNotionalPrincipalbefore():order.getNotionalPrincipal());
					// row.createCell(8+addRow).setCellValue(order.getType()==1?"美式":"欧式");
					// row.createCell(9+addRow).setCellValue(order.getCreateTimeFormat());
					row.createCell(11 + addRow)
							.setCellValue(order.getTradeAmount() == 0 ? "" : order.getTradeAmount() + "");
					row.createCell(12 + addRow)
							.setCellValue(order.getBalanceAmount() == 0 ? "" : order.getBalanceAmount() + "");

					row.createCell(13 + addRow).setCellValue(order.getBuyType() == 1 ? "实时下单" : "收盘价下单");
					if (status == 2 || status == 3) {
						row.createCell(14 + addRow).setCellValue(order.getPingcangType() == 1 ? "市价交易"
								: (order.getPingcangType() == 3 ? "挂价交易:" + order.getZhishu() : "收盘价交易"));

					}

					if (status == 0) {
						row.createCell(15 + addRow).setCellValue(order.getShou());
					}

					if (status == 2) {
						double shou = order.getShou();
						row.createCell(15 + addRow).setCellValue(shou);
					}
				}

				if (status == 0) {
					HSSFSheet sheet1 = wb.createSheet("交易记录(汇总）");
					HSSFRow row1 = sheet1.createRow((int) 0);
					HSSFCellStyle style1 = wb.createCellStyle();
					style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

					HSSFCell cell1 = row1.createCell(0);
					cell1.setCellValue("期权买卖方向");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(1);
					cell1.setCellValue("名称");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(2);
					cell1.setCellValue("代码");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(3);
					cell1.setCellValue("涨跌");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(4);
					cell1.setCellValue("权利金（元）");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(5);
					cell1.setCellValue("期限");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(6);
					cell1.setCellValue("合同开始时间");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(7);
					cell1.setCellValue("合同结束时间");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(8);
					cell1.setCellValue("名义本金/手数");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(9);
					cell1.setCellValue("成交价格（元）");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(10);
					cell1.setCellValue("行权价格（元）");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(11);
					cell1.setCellValue("报价方式");
					cell1.setCellStyle(style);

					cell1 = row1.createCell(12);
					cell1.setCellValue("手数");
					cell1.setCellStyle(style);

					HzList hzList = new HzList();
					hzList.setList(list);

					for (int i = 0; i < hzList.getSize(); i++) {
						row1 = sheet1.createRow((int) i + 1);
						VOrder order = hzList.get(i);

						if (status == 0 || status == 2) {
							row1.createCell(0).setCellValue(status == 0 ? "买入" : "行权");
						}

						row1.createCell(1).setCellValue(order.getProductName());
						row1.createCell(2).setCellValue(order.getProductCode());
						row1.createCell(3).setCellValue(order.getBuyAndFall() == 1 ? "看涨" : "看跌");
						row1.createCell(4).setCellValue(order.getAmount());
						row1.createCell(5).setCellValue(order.getPeriod());
						if (status == 0) {
							// 待成交时候，自动算出合同日期
							Goods goods = goodsService.getGoodsByCode(order.getProductCode());
							if (goods != null) {
								row1.createCell(6).setCellValue(DateFormater.simpleDateFormat(
										new Date(System.currentTimeMillis()), DateFormater.dateFormatNoSplit));
								if (order.getPeriod() == 15) {
									row1.createCell(7).setCellValue(goods.getFeilv_15_time());
								} else {
									row1.createCell(7).setCellValue(goods.getFeilv_30_time());
								}
							}

						} else {
							row1.createCell(6).setCellValue(order.getOrderStartTime());
							row1.createCell(7).setCellValue(order.getOrderEndTime());
						}

						row1.createCell(8).setCellValue(order.getNotionalPrincipal());

						row1.createCell(9).setCellValue(order.getTradeAmount() == 0 ? "" : order.getTradeAmount() + "");
						row1.createCell(10)
								.setCellValue(order.getBalanceAmount() == 0 ? "" : order.getBalanceAmount() + "");

						row1.createCell(11).setCellValue(order.getBuyType() == 1 ? "实时下单" : "收盘价下单");
						row1.createCell(12).setCellValue(order.getShou());
					}
				}

				OutputStream fOut = null;
				try {
					response.setContentType("application/vnd.ms-excel");
					String codedFileName = "交易记录_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
							DateFormater.datetimeFormatNoSplit);
					codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
					response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
					fOut = response.getOutputStream();
					wb.write(fOut);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						wb.close();
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}
	}

	/**
	 * 交易详情
	 * 
	 * @param id
	 * @param user
	 * @param model
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/goodsView/{id}", method = RequestMethod.GET)
	public String goodsView(@PathVariable int id, GmUser user, Model model) {
		Goods goods = goodsService.getGoodsById(id);
		model.addAttribute("goodsCategory", Constant.GoodsCategory.values());
		model.addAttribute("goods", goods);
		model.addAttribute("id", id);
		return "gm/goodsView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/orderView/{id}", method = RequestMethod.GET)
	public String orderView(@PathVariable String id, GmUser user, Model model) {
		VOrder order = tradeService.getVOrderByOrderNo(id);
		model.addAttribute("tradeRecord", order);
		model.addAttribute("id", id);
		return "gm/orderView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/closeOrder", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> closeOrder(@RequestParam(required = false, defaultValue = "") String orderNo,
			@RequestParam(required = false, defaultValue = "") String reason) {

		if (CommonUtils.isBlankOrNull(reason)) {
			return new ResultVo.Builder<String>(false).setData("驳回理由不能为空").build();
		}

		Order order = tradeService.getOrderByOrderNo(orderNo);
		if (order == null || order.getStatus() != 0) {
			return new ResultVo.Builder<String>(false).setData("当前状态不操作").build();
		}

		try {
			tradeService.closeOrder(order, reason);
			User user = userService.getUserById(order.getUserId());
			TaskFacotry.instance().put(new SendSmsTask(user.getMobile(), "241412"));

			return new ResultVo.Builder<String>(true).setData("操作成功").build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResultVo.Builder<String>(false).setData("操作失败").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updateGoods", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updateGoods(@RequestParam(required = false, defaultValue = "0") int id,
			@RequestParam(required = false, defaultValue = "1") int categoryId,
			@RequestParam(required = false, defaultValue = "") String code,
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "0") int unit,
			@RequestParam(required = false, defaultValue = "0") double feilv_15,
			@RequestParam(required = false, defaultValue = "0") double feilv_30,
			@RequestParam(required = false, defaultValue = "0") int min_shou,
			@RequestParam(required = false, defaultValue = "") String danwei,
			@RequestParam(required = false, defaultValue = "") String feilv_15_time,
			@RequestParam(required = false, defaultValue = "") String feilv_30_time,

			GmUser user) {

		Goods goods = goodsService.getGoodsById(id);
		if (goods == null) {
			goods = new Goods();
		}
		if (id == 0) {
			Goods oldGoods = goodsService.getGoodsByCode(code);
			if (oldGoods != null) {
				return new ResultVo.Builder<String>(false).setData("更新失败，商品编码重复").build();
			}
		}
		goods.setCategoryId(categoryId);
		goods.setCode(code);
		goods.setName(name);
		goods.setUnit(unit);
		goods.setFeilv_15(feilv_15);
		goods.setFeilv_30(feilv_30);
		goods.setFeilv_15_time(feilv_15_time);
		goods.setFeilv_30_time(feilv_30_time);
		goods.setDanwei(danwei);
		goods.setMin_shou(min_shou);
		goods.setLastUpdateTime(
				DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()), DateFormater.datetimeFormat2));
		int ref = goodsService.updateGoods(goods);
		if (ref > 0) {
			return new ResultVo.Builder<String>(true).setData("更新成功").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("更新失败，请联系管理员").build();
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/dealOrderBatch", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> dealOrderBatch(@RequestParam String tmpKey) {
		//
		List<VOrder> rdata = CacheManager.instance.getCache(Constant.CacheGroup.IMPORT_TRADE, tmpKey);
		if (rdata != null && (!rdata.isEmpty())) {
			for (VOrder order : rdata) {
				Order od = tradeService.getOrderByOrderNo(order.getOrderNo());
				if (od != null) {
					if (order.getExecutivePrice() > 0 && od.getStatus() == 2) {
						try {
							//结算结算金额
							//买涨的单子：行权价格*手数*单位-名义本金
					        //买跌的单子：名义本金-行权价格*手数*单位
							
//							Goods goods = goodsService.getGoodsByCode(order.getProductCode());
//							if (goods==null) {
//								continue;
//							}
//							
//							
//							double balanceAmount = 0.00;
//							if (order.getBuyAndFall()==1) {
//								//买涨
//								balanceAmount = CommonUtils.floatFormat(order.getExecutivePrice() * (double)od.getShou() * (double)goods.getUnit() - order.getNotionalPrincipal());
//							}else {
//								balanceAmount = CommonUtils.floatFormat(order.getNotionalPrincipal() - order.getExecutivePrice() * (double)od.getShou() * (double)goods.getUnit());
//							}
//							
//							if (balanceAmount<0) {
//								balanceAmount = 0;
//							}
//							order.setBalanceAmount(balanceAmount);

							tradeService.balanceOrder(order, od.getUserId());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (order.getTradeAmount() > 0 && od.getStatus() == 0 && order.getAmount() > 0) {
						try {
							tradeService.dealOrder(order);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		}
		return new ResultVo.Builder<String>(true).setData("更新成功").build();
	}

	// dealOrder
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/dealOrder", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> dealOrder(@RequestParam String orderNo,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "1") int status,
			@RequestParam(required = false, defaultValue = "0") double balanceAmount, GmUser user) {
		VOrder order = tradeService.getVOrderByOrderNo(orderNo);
		if (order.getStatus() != 0 && order.getStatus() != 2) {
			return new ResultVo.Builder<String>(false).setData("当前状态不操作").build();
		}
		int ref = 0;
		if (status == 1) {
			// 交易成功
			if (CommonUtils.isBlankOrNull(startTime)) {
				Date cur = new Date(System.currentTimeMillis());
				startTime = DateFormater.simpleDateFormat(cur, DateFormater.dateFormat);

				String endTime = DateFormater.simpleDateFormat(DateFormater.dateAdd(cur, order.getPeriod() + 1),
						DateFormater.dateFormat);
				order.setOrderStartTime(startTime);
				order.setOrderEndTime(endTime);
				order.setStatus(status);
				try {
					ref = tradeService.dealOrder(order);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {

			if (balanceAmount < 0) {
				return new ResultVo.Builder<String>(false).setData("结算金额不能小于0").build();
			}

			// 已结算
			order.setBalanceAmount(balanceAmount);
			order.setStatus(status);
			try {
				ref = tradeService.balanceOrder(order, order.getUserId());
			} catch (Exception e) {
				e.printStackTrace();
				return new ResultVo.Builder<String>(false).setData("操作失败，请联系管理员").build();
			}
		}

		if (ref > 0) {
			return new ResultVo.Builder<String>(true).setData("操作成功").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("操作失败，请联系管理员").build();
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/feilvView", method = RequestMethod.GET)
	public String feilvView(@RequestParam String tmpKey, GmUser user, Model model) {

		List<GoodsCompare> rdata = CacheManager.instance.getCache(Constant.CacheGroup.IMPORT_FEILV, tmpKey);
		for (GoodsCompare c : rdata) {
			c.setFeilv_15ChangedFormat(CommonUtils.getAmountFormat(c.getFeilv_15Changed()));
			c.setFeilv_30ChangedFormat(CommonUtils.getAmountFormat(c.getFeilv_30Changed()));
		}
		model.addAttribute("goodsList", rdata);
		model.addAttribute("tmpKey", tmpKey);
		return "gm/feilvView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/importOrder", method = RequestMethod.GET)
	public String importOrder(@RequestParam String tmpKey, GmUser user,
			@RequestParam(required=false,defaultValue="0") int status,
			Model model) {
		List<VOrder> rdata = CacheManager.instance.getCache(Constant.CacheGroup.IMPORT_TRADE, tmpKey);
		model.addAttribute("orderList", rdata);
		model.addAttribute("tmpKey", tmpKey);
		if (status==2) {
			model.addAttribute("balance", true);
		}else {
			model.addAttribute("balance", false);
		}
		
		return "gm/importOrder";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updateFeiLv", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updateFeiLv(@RequestParam String tmpKey) {
		List<GoodsCompare> rdata = CacheManager.instance.getCache(Constant.CacheGroup.IMPORT_FEILV, tmpKey);
		if (rdata.isEmpty()) {
			return new ResultVo.Builder<String>(false).setData("没有数据更新").build();
		} else {
			for (GoodsCompare goodsCompare : rdata) {
				if (goodsCompare.getFeilv_15Changed() > 0) {
					goodsCompare.setFeilv_15(goodsCompare.getFeilv_15Changed());
				}

				if (goodsCompare.getFeilv_30Changed() > 0) {
					goodsCompare.setFeilv_30(goodsCompare.getFeilv_30Changed());
				}

				goodsCompare.setFeilv_15_time(goodsCompare.getFeilv_15_timeChanged());
				goodsCompare.setFeilv_30_time(goodsCompare.getFeilv_30_timeChanged());

				Goods goods = goodsService.getGoodsById(goodsCompare.getId());
				goodsCompare.setMin_shou(goods.getMin_shou());
				goodsCompare.setDanwei(goods.getDanwei());
				goodsService.updateGoods(goodsCompare);
			}
			return new ResultVo.Builder<String>(true).setData("操作成功").build();
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/exchangelist", method = RequestMethod.GET)
	public String exchangelist(GmUser user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index,
			@RequestParam(required = false, defaultValue = "0") int excel, HttpServletResponse response, Model model) {
		Page<VRechargeRecord> page = tradeService.listRechargeRecordByPartnerId(0, status, index, 9999);
		model.addAttribute("status", status);
		model.addAttribute("page", page);
		model.addAttribute("index", index);
		if (excel == 0) {
			return "gm/exchangelist";
		} else {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("充值记录");
			HSSFRow row = sheet.createRow((int) 0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("购买者");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("充值编号");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("金额（元）");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("创建时间");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("状态");
			cell.setCellStyle(style);

			cell = row.createCell(5);
			cell.setCellValue("代理商");
			cell.setCellStyle(style);
			List<VRechargeRecord> list = page.getList();
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow((int) i + 1);
				VRechargeRecord order = list.get(i);
				row.createCell(0).setCellValue(order.getBuyerName());
				row.createCell(1).setCellValue(order.getRechargeNo());
				row.createCell(2).setCellValue(order.getAmount());
				row.createCell(3).setCellValue(order.getCreateTimeFormat());
				row.createCell(4).setCellValue(order.getStatus() == 1 ? "已入账" : "未入账");
				row.createCell(5).setCellValue(order.getCompanyName());
			}
			OutputStream fOut = null;
			try {
				response.setContentType("application/vnd.ms-excel");
				String codedFileName = "充值记录_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
						DateFormater.datetimeFormatNoSplit);
				codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
				fOut = response.getOutputStream();
				wb.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					wb.close();
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/confirmchongzhi", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> confirmchongzhi(@RequestParam int id, GmUser user) {

		try {
			int ref = tradeService.chongzhi(id, user);
			if (ref <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo.Builder<String>(false).setData("操作失败，请联系管理员").build();
		}

		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}

	/**
	 * 充值驳回
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/bohuichongzhi", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> bohuichongzhi(@RequestParam int id, GmUser user) {

		try {
			int ref = tradeService.bohuiRecharge(id, user);
			if (ref <= 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVo.Builder<String>(false).setData("操作失败，请联系管理员").build();
		}

		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/managerUser", method = RequestMethod.GET)
	public String managerUser(GmUser user, Model model) {
		List<GmUser> gmUsers = gmService.listAllGmUser();
		model.addAttribute("gmUsers", gmUsers);
		return "gm/managerUser";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/gmUserView/{id}", method = RequestMethod.GET)
	public String gmUserView(@PathVariable int id, @RequestParam(required = false, defaultValue = "0") int edit,
			GmUser user, Model model) {
		GmUser gmUser = gmService.getUserById(id);
		if (gmUser == null) {
			gmUser = new GmUser();
			gmUser.setStatus(1);

		}
		if (id == 0) {
			model.addAttribute("edit", 1);
		} else {
			model.addAttribute("edit", edit);
		}

		model.addAttribute("gmUser", gmUser);
		model.addAttribute("gmPositions", Constant.GM_ROLE.values());
		return "gm/gmUserView";
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/resetGmUserPassword", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> resetGmUserPassword(
			@RequestParam(required = false, defaultValue = "0") int userId,
			@RequestParam(required = false, defaultValue = "") String newPass) {

		if (CommonUtils.isBlankOrNull(newPass) || newPass.length() < 4) {
			new ResultVo.Builder<String>(false).setData("密码不能为空或小于4个字符").build();
		}

		GmUser old = gmService.getUserById(userId);
		if (old != null) {

			gmService.updateColById("f_password_hash", CipherUtil.buildPasswordHash(newPass), userId);
		}

		return new ResultVo.Builder<String>(true).setData("操作成功").build();
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updategmUser", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> updategmUser(@RequestParam(required = false, defaultValue = "0") int id,
			@RequestParam(required = false, defaultValue = "0") int userId,
			@RequestParam(required = false, defaultValue = "") String name,
			@RequestParam(required = false, defaultValue = "") String realName,
			@RequestParam(required = false, defaultValue = "") String password,
			@RequestParam(required = false, defaultValue = "") int positionId,
			@RequestParam(required = false, defaultValue = "") int status, GmUser user) {

		int ref = -1;
		if (userId == 0) {
			GmUser old = gmService.getUserByName(name);
			if (old != null) {
				return new ResultVo.Builder<String>(false).setData("账号已存在，不能重复").build();
			}
			GmUser gmUser = new GmUser();
			gmUser.setIsAdmin(positionId == 0 ? 1 : 0);
			gmUser.setName(name);
			gmUser.setPassword_hash(CipherUtil.buildPasswordHash(password));
			gmUser.setPositionId(positionId);
			gmUser.setRealName(realName);
			gmUser.setStatus(1);
			gmUser.setRole("gm");
			ref = gmService.create(gmUser);

		} else {
			GmUser gmUser = gmService.getUserById(userId);
			gmUser.setIsAdmin(positionId == 0 ? 1 : 0);
			gmUser.setPositionId(positionId);
			gmUser.setRealName(realName);
			gmUser.setStatus(status);
			ref = gmService.update(gmUser);
		}

		if (ref > 0) {
			return new ResultVo.Builder<String>(true).setData("更新成功").build();
		} else {
			return new ResultVo.Builder<String>(false).setData("更新失败，请联系管理员").build();
		}

	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/withdrawcashlist", method = RequestMethod.GET)
	public String withdrawcashlist(GmUser user, @RequestParam(required = false, defaultValue = "0") int status,
			@RequestParam(required = false, defaultValue = "1") int index,
			@RequestParam(required = false, defaultValue = "0") int excel, HttpServletResponse response, Model model) {
		model.addAttribute("status", status);
		Page<CashRecord> pages = null;
		if (user.getIsAdmin() == 1 || user.getPositionId() == Constant.GM_ROLE.OP_MASTER.getId()) {
			pages = tradeService.listCashRecordByStatus(status, null, index, 9999);
		} else {
			// 普通管理员，先获取合作方信息
			Page<Partner> partners = partnerService.listPartnerByGmUserId(user.getUserId(), 1, 99999);
			List<String> partnerCodes = new ArrayList<>();
			for (Partner partner : partners.getList()) {
				partnerCodes.add(partner.getCode());
			}

			List<String> userIds = userService.listUserIdsByCodes(partnerCodes);
			pages = tradeService.listCashRecordByStatus(status, userIds, index, 9999);
		}

		model.addAttribute("page", pages);
		model.addAttribute("status", status);
		model.addAttribute("index", index);
		if (excel == 0) {
			DBService.instance().put(new UpdateUnReadMessage(user.getUserId(), MAINID.WITHDRAWCASH));

			return "gm/withdrawcashlist";
		} else {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("提现记录");
			HSSFRow row = sheet.createRow((int) 0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("申请提现时间");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("提现金额（元）");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("真实姓名");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("提现到账户");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("提现到卡号");
			cell.setCellStyle(style);

			List<Integer> idUp = new ArrayList<>();
			List<CashRecord> list = pages.getList();
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow((int) i + 1);
				CashRecord order = list.get(i);
				idUp.add(order.getId());
				row.createCell(0).setCellValue(order.getCreateTimeFormat());
				row.createCell(1).setCellValue(order.getAmount());
				row.createCell(2).setCellValue(order.getRealName());
				row.createCell(3).setCellValue(order.getBankOfDeposit());
				row.createCell(4).setCellValue(order.getBankCardNo());
			}

			tradeService.updateCashRecordColById("f_status", 1, idUp);

			OutputStream fOut = null;
			try {
				response.setContentType("application/vnd.ms-excel");
				String codedFileName = "提现记录_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
						DateFormater.datetimeFormatNoSplit);
				codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
				fOut = response.getOutputStream();
				wb.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					wb.close();
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/caiwureport", method = RequestMethod.GET)
	public String withdrawcashlist(GmUser user, Model model,
			@RequestParam(required = false, defaultValue = "0") int excel,
			@RequestParam(required = false, defaultValue = "") String startTime,
			@RequestParam(required = false, defaultValue = "") String endTime,
			@RequestParam(required = false, defaultValue = "") String searchText,
			HttpServletResponse response) {
		Map map = Maps.newHashMap();
		map.put("startTime",startTime);
		map.put("endTime",endTime);
		map.put("searchText",searchText);
		Page<VOrder> pages = tradeService.listVOrderByStatus(3, null, 1, 9999, "",map);
		if (excel == 0) {
			model.addAttribute("page", pages);
			return "gm/caiwureport";
		} else {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("财务报表");
			HSSFRow row = sheet.createRow((int) 0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
			HSSFCell cell = row.createCell(0);
			cell.setCellValue("账号");
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue("客户名称");
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue("代理商");
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellValue("订单号");
			cell.setCellStyle(style);
			cell = row.createCell(4);
			cell.setCellValue("权利金（元）");
			cell.setCellStyle(style);
			cell = row.createCell(5);
			cell.setCellValue("结算金额（元）");
			cell.setCellStyle(style);
			cell = row.createCell(6);
			cell.setCellValue("应返有限合伙（元）");
			cell.setCellStyle(style);
			cell = row.createCell(7);
			cell.setCellValue("应返客户（元）");
			cell.setCellStyle(style);

			List<VOrder> list = pages.getList();
			for (int i = 0; i < list.size(); i++) {
				row = sheet.createRow((int) i + 1);
				VOrder order = list.get(i);
				row.createCell(0).setCellValue(order.getBuyerMobile());
				row.createCell(1).setCellValue(order.getBuyerName());
				row.createCell(2).setCellValue(order.getBuyerPartnerCompanyName());
				row.createCell(3).setCellValue(order.getOrderNo());
				row.createCell(4).setCellValue(order.getAmount());
				row.createCell(5).setCellValue(order.getBalanceAmount());
				row.createCell(6).setCellValue(order.getHehuoAmount());
				row.createCell(7).setCellValue(order.getDiscount());
			}
			OutputStream fOut = null;
			try {
				response.setContentType("application/vnd.ms-excel");
				String codedFileName = "财务报表_" + DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
						DateFormater.datetimeFormatNoSplit);
				codedFileName = new String(codedFileName.getBytes("gb2312"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + codedFileName + ".xls");
				fOut = response.getOutputStream();
				wb.write(fOut);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					wb.close();
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

	}

	private void setUserSession(GmUser user, HttpServletRequest req, HttpServletResponse resp) {
		String token = CommonUtils.newToken();
		UserSession session = new UserSession(token, user.getUserId(), user.getName(), user.getPositionName(),
				user.getStatus());
		CacheManager.instance.addCacheEx(Constant.CacheGroup.GMSESSION, token, user.getUserId() + "",
				Constant.DATE.TIME_ONE_DAY_SECOND * 30);
		user.setToken(token);
		req.getSession().setAttribute(Constant.SESSION_KEY.currentGmUser, session);
		CookieUtils.addCookie(req, resp, Constant.SESSION_KEY.currentGmUser, token);
	}

	@SessionScope(Constant.SESSION_KEY.currentGmUser)
	@RegistAuthority(AuthorityType.MASTER)
	@RequestMapping(value = "/json/updateWithdrawStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResultVo<String> confirmWithdraw(@RequestParam Integer id){
		int result = tradeService.updateWithdrawStatus(id);
		if(result > 0){
			return new ResultVo.Builder<String>(true).setData("更新成功").build();
		}else{
			return new ResultVo.Builder<String>(false).setData("更新失败，请联系管理员").build();
		}
	}
}
