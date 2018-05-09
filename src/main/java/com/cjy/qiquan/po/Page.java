package com.cjy.qiquan.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分页
 * 
 * @author pc
 * 
 * @param <T>
 */
public class Page<E> {
	
	public void parseForm(HttpServletRequest request){
		
		if (StringUtils.hasText(request.getParameter("orderby"))){
			setOrderby(request.getParameter("orderby"));
		}
		
		if (StringUtils.hasText(request.getParameter("ordermode"))){
			setOrdermode(request.getParameter("ordermode"));
		}
		
		if (StringUtils.hasText(request.getParameter("index"))){
			setIndex(Integer.valueOf(request.getParameter("index")));
		}
		if (StringUtils.hasText(request.getParameter("size"))){
			setSize(Integer.valueOf(request.getParameter("size")));
		}else{
			setSize(Integer.valueOf(999));
		}
	}
	
	
	
	private List<E> list = new ArrayList<E>();
	private String orderby = "";
	private String ordermode = "desc";
	private int size = 10;
	private int index = 1;
	private int total;
	private int last;
	private int first;
	private int next;
	private int totalpage;
	private int prev;
	@JsonIgnore
	private Map<String, Object> searchkey = new HashMap<String, Object>();

	public void setPage(int total) {
		this.total = total;
		this.totalpage = (total + size - 1) / size;
		this.first = 1;
		this.last = this.totalpage;
		if (this.last < 0) {
			this.last = 0;
		}
		this.next = this.index + 1;
		if (this.next > this.last) {
			this.next = this.last;
		}
		this.prev = this.index - 1;
		if (this.prev < 1) {
			this.prev = 1;
		}

//		if (index > this.last) {
//			index = this.last;
//		}
		
		if (index==0){
			index = 1;
		}
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getOrdermode() {
		return ordermode;
	}

	public void setOrdermode(String ordermode) {
		this.ordermode = ordermode;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index>0){
			this.index = index;	
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
//		this.total = total;
		this.setPage(total);
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public Map<String, Object> getSearchkey() {
		return searchkey;
	}

	public void setSearchkey(Map<String, Object> searchkey) {
		this.searchkey = searchkey;
	}

}
