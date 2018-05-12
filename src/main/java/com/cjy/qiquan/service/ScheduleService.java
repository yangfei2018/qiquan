package com.cjy.qiquan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.model.Partner;
import com.cjy.qiquan.model.User;
import com.cjy.qiquan.model.VOrder;
import com.cjy.qiquan.po.Page;
import com.cjy.qiquan.task.SystemLogHandler;
import com.cjy.qiquan.utils.Constant.MAINID;
import com.cjy.qiquan.utils.DateFormater;

@Service
public class ScheduleService {

	@Autowired
	private TradeService tradeService;

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private UserService userService;

	public void run_on_24() {

		Page<VOrder> pages = tradeService.listVOrderByStatus(2, null, 1, 99999,"",null);
		List<VOrder> list = pages.getList();
		int today = DateFormater.getDayInt(System.currentTimeMillis());
		for (VOrder order : list) {
			try {
				int orderEndtime = Integer.valueOf(order.getOrderEndTime());

				User user = userService.getUserById(order.getUserId());

				if (today >= orderEndtime) {
					// 自动平仓
					int ref = tradeService.pingcang(order.getId(),2,0);
					if (ref > 0) {
						StringBuilder body = new StringBuilder();
						int belongUserId = 0;
						Partner partner = partnerService.getPartnerByCode(user.getPartnerCode());
						if (partner != null) {
							belongUserId = partner.getBelongtogmsuer();
						}
						body.append("客户“").append(user.getRealName()).append("”")
								.append("进行了一次平仓操作，订单号：" + order.getOrderNo());
						DBService.instance().put(new SystemLogHandler(body.toString(), MAINID.PINGCANG, belongUserId));
					}

				}

			} catch (Exception e) {
				StringBuilder errBody = new StringBuilder();
				errBody.append("订单号：").append(order.getOrderNo()).append(",自动平仓失败。");
				DBService.instance().put(new SystemLogHandler(errBody.toString(), MAINID.PINGCANG, 0));
			}
		}

	}
}
