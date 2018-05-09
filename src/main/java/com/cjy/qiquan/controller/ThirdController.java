package com.cjy.qiquan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.cjy.qiquan.cache.CacheManager;
import com.cjy.qiquan.exception.AppServiceException;
import com.cjy.qiquan.model.Cl;
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.model.GoodsCompare;
import com.cjy.qiquan.model.PartnerBankInfo;
import com.cjy.qiquan.model.UploadResultVo;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.model.VUser;
import com.cjy.qiquan.po.ResultVo;
import com.cjy.qiquan.service.ClService;
import com.cjy.qiquan.service.GoodsService;
import com.cjy.qiquan.service.PartnerService;
import com.cjy.qiquan.service.TradeService;
import com.cjy.qiquan.service.Uploader;
import com.cjy.qiquan.service.UserService;
import com.cjy.qiquan.task.TaskFacotry;
import com.cjy.qiquan.task.UpdateSignAccount;
import com.cjy.qiquan.utils.CommonUtils;
import com.cjy.qiquan.utils.Constant;
import com.cjy.qiquan.utils.DateFormater;
import com.cjy.qiquan.utils.ImportExcel;
import com.cjy.qiquan.utils.RandomUtil;
import com.cjy.qiquan.utils.StatesUtils;
import com.cjy.qiquan.utils.WDWUtil;

@Controller
@RequestMapping("/third")
public class ThirdController {

	@Autowired
	private ClService clService;

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		UploadResultVo vo = new UploadResultVo();
		vo.setState("上传错误");
		String callback = request.getParameter("callback");
		if (multipartResolver.isMultipart(request)) {
			Uploader uploader = new Uploader(request);
			uploader.setmFile("upfile");
			// Constant.APP_UPLOAD_PATH
			uploader.setSavePath(Constant.APP_UPLOAD_PATH);
			uploader.setMaxSize(15111808);
			try {
				uploader.uploadOrign();

				String result = "{\"name\":\"" + uploader.getFileName() + "\", \"originalName\": \""
						+ uploader.getOriginalName() + "\", \"size\": " + uploader.getSize() + ", \"state\": \""
						+ uploader.getState() + "\", \"type\": \"" + uploader.getType() + "\", \"url\": \""
						+ uploader.getUrl() + "\"}";
				result = result.replaceAll("\\\\", "\\\\");
				response.setContentType("text/html;charset=UTF-8");
				if (CommonUtils.isBlankOrNull(callback)) {
					response.getWriter().print(result);
				} else {
					response.getWriter().print("<script>" + callback + "(" + result + ")</script>");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@RequestMapping(value = "/imoprtFeilv", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> importFeiLv(HttpServletRequest request, HttpServletResponse response)
			throws AppServiceException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("upfile");
		if (file.isEmpty()) {
			throw new AppServiceException(StatesUtils.States.params_error, "没有上传文件");
		}

		try {
			boolean isExcel2003 = true;

			if (WDWUtil.isExcel2007(file.getOriginalFilename())) {

				isExcel2003 = false;

			}
			InputStream in = file.getInputStream();
			ImportExcel poi = new ImportExcel();
			List<List<String>> excelData = poi.read(in, isExcel2003);
			List<GoodsCompare> rdata = new ArrayList<>();
			for (Goods goods : GoodsService.instance().listGoodsAll()) {
				GoodsCompare c = new GoodsCompare();
				c.setCategoryId(goods.getCategoryId());
				c.setCode(goods.getCode() + "");
				c.setUnit(goods.getUnit());
				c.setFeilv_15(goods.getFeilv_15());
				c.setFeilv_30(goods.getFeilv_30());
				c.setLastUpdateTime(goods.getLastUpdateTime());
				c.setId(goods.getId());
				c.setName(goods.getName());
				rdata.add(c);
			}

			System.out.println("excelData.size():"+excelData.size());
			for (int i = 1; i < excelData.size(); i++) {
				try {
					List<String> row = excelData.get(i);
					String name = (String) row.get(1);
					if (CommonUtils.hasText(name)) {
						String code = CommonUtils.convertNumToText(row.get(2));
						double feilv15 = Double.valueOf(row.get(3));
						double feilv30 = Double.valueOf(row.get(4));
						String feilv15Time = row.get(5);
						String feilv30Time = row.get(6);
						System.out.println("name:"+name);
						updateGoodsRow(rdata, name, code, feilv15, feilv30, feilv15Time, feilv30Time);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			String tmpKey = RandomUtil.getRandomLetter(30);
			CacheManager.instance.addCache(Constant.CacheGroup.IMPORT_FEILV, tmpKey, rdata);
			return new ResultVo.Builder<String>(true).setData(tmpKey).build();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppServiceException(StatesUtils.States.params_error, "上传失败");
		}

	}

	@RequestMapping(value = "/calGoods", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> calGoods(@RequestParam String id, @RequestParam(required=false,defaultValue="30") int period,
			@RequestParam(required=false,defaultValue="0") int shou) {

		Map<String, Object> res = new HashMap<>();
		Goods goods = GoodsService.instance().getGoodsByCode(id);
		if (goods != null) {
			res.put("min_shou", goods.getMin_shou());
			double feilv = period == 15 ? goods.getFeilv_15() : goods.getFeilv_30();
			if (goods.getCategoryId() == Constant.GoodsCategory.SHANGPIN.getId()) {
				Cl cl = clService.getRecentPriceByszlable(goods.getCode());
				if (cl != null) {
					// 起投权利金
					double min_amount =  CommonUtils.floatFormat(cl.getFnewprice()>0?CommonUtils.floatFormat((double)goods.getMin_shou() * (double)goods.getUnit() * cl.getFnewprice() / 100.00 * feilv * Constant.BS):0.00);
					double per_amount =  CommonUtils.floatFormat(goods.getMin_shou()>0?CommonUtils.floatFormat(min_amount / (double)goods.getMin_shou()):0.00);
					double amount = CommonUtils.floatFormat((double)shou * per_amount);
					double notionalPrincipal =  CommonUtils.floatFormat(feilv>0?CommonUtils.floatFormat(amount * 100.00 / feilv):0.00);
					res.put("min_amount", min_amount);
					res.put("per_amount", per_amount);
					res.put("amount", amount);
					res.put("notionalPrincipal", notionalPrincipal);

				}
			} else if (goods.getCategoryId() == Constant.GoodsCategory.ZHISHU.getId()) {
				double min_amount =  CommonUtils.floatFormat(goods.getUnit()>0?CommonUtils.floatFormat((double)goods.getMin_shou() * (double)goods.getUnit() / 100.00 * feilv * Constant.BS):0.00);
				double per_amount =  CommonUtils.floatFormat(goods.getMin_shou()>0?CommonUtils.floatFormat(min_amount / (double)goods.getMin_shou()):0.00);
				double amount =  CommonUtils.floatFormat((double)shou * per_amount);
				double notionalPrincipal =  CommonUtils.floatFormat(feilv>0?CommonUtils.floatFormat(amount * 100.00 / feilv):0.00);
				res.put("min_amount", min_amount);
				res.put("per_amount", per_amount);
				res.put("amount", amount);
				res.put("notionalPrincipal", notionalPrincipal);
			}

		}
		return res;
	}

	/**
	 * 导入交易记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws AppServiceException
	 */
	@RequestMapping(value = "/importTradeExcel", method = RequestMethod.POST)
	public @ResponseBody ResultVo<String> importTradeExcel(HttpServletRequest request, HttpServletResponse response)
			throws AppServiceException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("upfile");
		if (file.isEmpty()) {
			throw new AppServiceException(StatesUtils.States.params_error, "没有上传文件");
		}
		try {
			boolean isExcel2003 = true;
			if (WDWUtil.isExcel2007(file.getOriginalFilename())) {
				isExcel2003 = false;
			}
			InputStream in = file.getInputStream();
			ImportExcel poi = new ImportExcel();
			List<List<String>> excelData = poi.read(in, isExcel2003);
			List<VOrder> orders = new ArrayList<>();
			for (int i = 1; i < excelData.size(); i++) {
				try {
					List<String> row = excelData.get(i);
					String orderSN = row.get(1);
					String realName = row.get(3);
					String productName = row.get(4);
					String productCode = CommonUtils.convertNumToText(row.get(5));
					double tradeAmount = Double.valueOf(row.get(12));
					double amount = Double.valueOf(row.get(7));
					double notionalPrincipal = Double.valueOf(row.get(11));
					String orderStartTime = CommonUtils.convertNumToText(row.get(9));
					String orderEndTime = CommonUtils.convertNumToText(row.get(10));
					String balanceAmount = row.get(13);
					VOrder order = new VOrder();
					order.setOrderNo(orderSN);
					order.setBuyerName(realName);
					order.setProductName(productName);
					order.setProductCode(productCode);
					order.setTradeAmount(tradeAmount);
					order.setAmount(amount);
					order.setNotionalPrincipal(notionalPrincipal);
					order.setOrderStartTime(orderStartTime);
					order.setOrderEndTime(orderEndTime);
					
					
					if (CommonUtils.hasText(balanceAmount)) {
						
						order.setExecutivePrice(Double.valueOf(balanceAmount));
						
						VOrder oldOrder = TradeService.instance().getVOrderByOrderNo(orderSN);
						double nbalanceAmount = 0.00;
						if (oldOrder.getBuyAndFall()==1) {
							//买涨
							nbalanceAmount = CommonUtils.floatFormat(order.getExecutivePrice() * (double)oldOrder.getShou() * (double)oldOrder.getUnit() - oldOrder.getNotionalPrincipal());
						}else {
							nbalanceAmount = CommonUtils.floatFormat(oldOrder.getNotionalPrincipal() - order.getExecutivePrice() * (double)oldOrder.getShou() * (double)oldOrder.getUnit());
						}
						order.setBalanceAmount(Double.valueOf(Math.max(nbalanceAmount,0)));
					}else {
						VOrder oldOrder = TradeService.instance().getVOrderByOrderNo(orderSN);
						if (oldOrder!=null) {
							//如果没有结算金额
							Goods goods = GoodsService.instance().getGoodsByCode(productCode);
							if (goods!=null && goods.getCategoryId()==Constant.GoodsCategory.SHANGPIN.getId()) {
								notionalPrincipal = CommonUtils.floatFormat(tradeAmount * (double)goods.getUnit() * (double)oldOrder.getShou());
								double feilv = oldOrder.getPeriod()==15?goods.getFeilv_15():goods.getFeilv_30();
								amount = CommonUtils.floatFormat(notionalPrincipal /100.00 * feilv);
								order.setAmount(amount);
								order.setNotionalPrincipal(notionalPrincipal);
								order.setDifAmount(oldOrder.getAmount() - order.getAmount());
							}
						}
					}
					orders.add(order);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			String tmpKey = RandomUtil.getRandomLetter(30);
			CacheManager.instance.addCache(Constant.CacheGroup.IMPORT_TRADE, tmpKey, orders);
			return new ResultVo.Builder<String>(true).setData(tmpKey).build();
		} catch (IOException e) {
			e.printStackTrace();
			throw new AppServiceException(StatesUtils.States.params_error, "上传失败");
		}

	}

	private void updateGoodsRow(List<GoodsCompare> rdata, String name, String code, double feilv15, double feilv30,
			String feilv15Time, String feilv30Time) {
		for (GoodsCompare goodsCompare : rdata) {
			if (goodsCompare.getName().equalsIgnoreCase(name)) {
				goodsCompare.setCode(code);
				goodsCompare.setFeilv_15Changed(feilv15);
				goodsCompare.setFeilv_30Changed(feilv30);
				goodsCompare.setFeilv_15_timeChanged(feilv15Time);
				goodsCompare.setFeilv_30_timeChanged(feilv30Time);
				goodsCompare.setLastUpdateTime(DateFormater.simpleDateFormat(new Date(System.currentTimeMillis()),
						DateFormater.datetimeFormat2));
			}
		}
	}
	
	
	@RequestMapping(value = "/updateSign", method = RequestMethod.GET)
	public @ResponseBody String updateSign() {
		List<PartnerBankInfo> partners = PartnerService.instance().listPartnerBankByPartnerId(0);
		for(PartnerBankInfo partner:partners) {
			if (CommonUtils.isBlankOrNull(partner.getSignAccountId())) {
				TaskFacotry.instance().put(new UpdateSignAccount(1, partner.getId(), 1));
			}
		}
		
		List<VUser> users = UserService.instance().listUserPagedByStatusId(0, null, 1, 9999).getList();
		for(VUser user:users) {
			if (CommonUtils.isBlankOrNull(user.getSignAccountId())) {
				TaskFacotry.instance().put(new UpdateSignAccount(0, user.getUserId(), 1));
			}
		}
		
		return "success";
	}

	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String test() {
		VOrder order = TradeService.instance().getVOrderByOrderNo("1111180413003");
		System.out.println(order.getYj());
		return "success";
	}
}
