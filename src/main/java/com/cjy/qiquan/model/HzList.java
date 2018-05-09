package com.cjy.qiquan.model;

import java.util.ArrayList;
import java.util.List;

public class HzList {

	private List<VOrder> list;

	public void add(VOrder vOrder) {

		VOrder order = findHz(vOrder);
		
		if (order==null) {
			this.list.add(vOrder);
		}else {
			order.setShou(order.getShou()+vOrder.getShou());
			order.setAmount(order.getAmount() + vOrder.getAmount());
			order.setNotionalPrincipal(order.getNotionalPrincipal() + vOrder.getNotionalPrincipal());
		}
	}
	
	
	private VOrder findHz(VOrder vOrder) {
		
		
		for(VOrder order:list) {
			
			
			if (order.getBuyAndFall() == vOrder.getBuyAndFall() 
					&& order.getBuyType() == vOrder.getBuyType() 
					&& order.getProductName().equals(vOrder.getProductName())
					&& order.getPeriod() == vOrder.getPeriod()
					) {
				return order;
			}
		}
		
		return null;
	}

	public HzList() {
		list = new ArrayList<>();
	}

	public List<VOrder> getList() {
		return list;
	}

	public void setList(List<VOrder> list) {
		
//		this.list = list;
		
		for(VOrder order :list) {
			
			
			add(order);
		}
	}

	public int getSize() {
		return this.list.size();
	}

	public VOrder get(final int index) {
		return this.list.get(index);
	}
}
