package com.cjy.qiquan.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cjy.qiquan.dao.GoodsDao;
import com.cjy.qiquan.model.Goods;
import com.cjy.qiquan.utils.SpringApplicationContext;

@Service
public class GoodsService {

	@Autowired
	private GoodsDao goodsDao;

	public static GoodsService instance() {
		return SpringApplicationContext.getBean("goodsService");
	}

	private List<Goods> GOODSLIST = new ArrayList<>();

	public void loadOnStart() {
		GOODSLIST.clear();
		GOODSLIST.addAll(goodsDao.listAllGoods());
	}

	public int updateGoods(Goods goods) {
		if (goods.getId() == 0) {
			int ref = goodsDao.createGoods(goods);
			if (ref > 0) {
				goods.setId(ref);
				GOODSLIST.add(goods);
			}
		} else {
			for (Goods goods2 : GOODSLIST) {
				int ref = goodsDao.updateGoods(goods);
				if (ref > 0) {
					if (goods2.getId() == goods.getId()) {
						goods2.setCategoryId(goods.getCategoryId());
						goods2.setCode(goods.getCode());
						goods2.setFeilv_15(goods.getFeilv_15());
						goods2.setFeilv_30(goods.getFeilv_30());
						goods2.setLastUpdateTime(goods.getLastUpdateTime());
						goods2.setFeilv_15_time(goods.getFeilv_15_time());
						goods2.setFeilv_30_time(goods.getFeilv_30_time());
						goods2.setMin_shou(goods.getMin_shou());
						goods.setDanwei(goods.getDanwei());
					}
				}
			}
		}

		return 1;
	}

	public List<Goods> listGoodsByCategoryId(final int categoryId) {
		List<Goods> list = new ArrayList<>(GOODSLIST.size());
		for (Goods goods : GOODSLIST) {
			if (goods.getCategoryId() == categoryId || categoryId == 0) {
				list.add(goods);
			}
		}
		return list;
	}

	
	public List<Goods> listGoodsAll(){
		return GOODSLIST;
	}
	
	public Goods getGoodsByCode(final String code) {
		for (Goods goods : GOODSLIST) {
			if (goods.getCode().equalsIgnoreCase(code)) {
				return goods;
			}
		}

		return null;
	}
	
	
	public Goods getGoodsByName(final String name) {
		for (Goods goods : GOODSLIST) {
			if (goods.getName().equalsIgnoreCase(name)) {
				return goods;
			}
		}

		return null;
	}
	
	public Goods getGoodsById(final int id) {
		for (Goods goods : GOODSLIST) {
			if (goods.getId() == id) {
				return goods;
			}
		}

		return null;
	}

	public List<Goods> listGoodsByName(String searchValue) {
		List<Goods> list = goodsDao.listGoodsByName(searchValue);
		return list;
	}
}
