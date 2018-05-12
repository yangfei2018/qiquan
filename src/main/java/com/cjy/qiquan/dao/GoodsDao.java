package com.cjy.qiquan.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cjy.qiquan.dbhelper.JdbcHelper;
import com.cjy.qiquan.model.Goods;

@Repository
public class GoodsDao {

	@Autowired
	private JdbcHelper jdbcHelper;

	private RowMapper<Goods> GOODS = new RowMapper<Goods>() {
		@Override
		public Goods mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			Goods user = new Goods(resultSet);
			return user;
		}
	};

	public List<Goods> listAllGoods() {
		return jdbcHelper.query("SELECT * from t_goods", GOODS);
	}
	
	
	public int createGoods(Goods goods) {
		return jdbcHelper.executeUpdateReturnKeyGenerate("INSERT INTO `t_goods`(f_categoryId,f_name,f_code,f_feilv_15,f_feilv_30,f_lastupdateTime,f_unit,f_feilv_15_time,f_feilv_30_time,f_min_shou,f_danwei) value(?,?,?,?,?,?,?,?,?,?,?)", new Object[] {
				goods.getCategoryId(),goods.getName(),goods.getCode(),goods.getFeilv_15(),goods.getFeilv_30(),goods.getLastUpdateTime(),goods.getUnit(),goods.getFeilv_15_time(),goods.getFeilv_30_time(),goods.getMin_shou(),goods.getDanwei()
				
		});
	}
	
	
	public int updateGoods(Goods goods) {
		return jdbcHelper.update("update `t_goods` SET f_categoryId=?,f_name=?,f_code=?,f_feilv_15=?,f_feilv_30=?,f_lastupdateTime=?,f_unit=?,f_feilv_15_time=?,f_feilv_30_time=?,f_min_shou=?,f_danwei=? WHERE f_id=?",new Object[] {
				goods.getCategoryId(),goods.getName(),goods.getCode(),goods.getFeilv_15(),goods.getFeilv_30(),goods.getLastUpdateTime(),goods.getUnit(),
				goods.getFeilv_15_time(),goods.getFeilv_30_time(),goods.getMin_shou(),goods.getDanwei(),
				goods.getId()
		});
	}

	public List<Goods> listGoodsByName(String searchValue) {
		StringBuilder sql = new StringBuilder("");
		sql.append("select * from t_goods where 1=1 ");
		if (StringUtils.isNotBlank(searchValue)) {
			try {
				searchValue= new String(searchValue.getBytes("iso-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sql.append("and (f_name like concat('%','").append(searchValue).append("','%')")
					.append(" or f_code = '").append(searchValue).append("')");
		}
		return jdbcHelper.query(sql.toString(), GOODS);
	}

}
