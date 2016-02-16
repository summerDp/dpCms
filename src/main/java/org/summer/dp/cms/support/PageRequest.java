package org.summer.dp.cms.support;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;



public class PageRequest implements Pageable{
	/**
	 * @author mumu
	 */
	private static final long serialVersionUID = 6180499674530995632L;

	private int page = 0;
	private int count =20;
	
	private org.springframework.data.domain.PageRequest core = null;
	
	private ServletRequest request = null;
	

//	String[] sortFields = null;
//	String[] sortTypes = null;
	
	List<String> sortFields = new ArrayList<String>();
	List<String> sortTypes = new ArrayList<String>();
	
	private String test;
	
	public PageRequest(){
		core = null;
	}
	
	private void initCore(){
		if(null == core){
			core = new org.springframework.data.domain.PageRequest(page,count, getSort());
		}
	}
	
	public Sort getSort(){
		Sort sort = null;
		if(sortFields!= null && sortFields.size()>0){
			List<Sort.Order> orders = new ArrayList<Sort.Order>();
			for(int i=0;i<sortFields.size();i++){
				if(sortTypes!=null && (sortTypes.size()+1) >= i){
					orders.add(new Sort.Order(Direction.fromString(sortTypes.get(i)), sortFields.get(i)));
				}else{
					orders.add(new Sort.Order(sortTypes.get(i)));
				}
			}
			sort  = new Sort(orders);
		}
		return sort;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page - 1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<String> getSortFields() {
		return sortFields;
	}

	public void setSortFields(List<String> sortFields) {
		this.sortFields = sortFields;
	}

	public List<String> getField() {
		return sortFields;
	}

	public void setField(List<String> field) {
		this.sortFields = field;
	}

	public List<String> getDirection() {
		return sortTypes;
	}

	public void setDirection(List<String> direction) {
		this.sortTypes = direction;
	}

	public List<String> getSortTypes() {
		return sortTypes;
	}

	public void setSortTypes(List<String> sortTypes) {
		this.sortTypes = sortTypes;
	}

	@Override
	public int getPageNumber() {
		initCore();
		return core.getPageNumber();
	}

	@Override
	public int getPageSize() {
		initCore();
		return core.getPageSize();
	}

	@Override
	public int getOffset() {
		initCore();
		return core.getOffset();
	}

	@Override
	public Pageable next() {
		initCore();
		return core.next();
	}

	@Override
	public Pageable previousOrFirst() {
		initCore();
		return core.previousOrFirst();
	}

	@Override
	public Pageable first() {
		initCore();
		return core.previousOrFirst();
	}

	@Override
	public boolean hasPrevious() {
		initCore();
		return core.hasPrevious();
	}

	public ServletRequest getServletRequest() {
		return request;
	}

	public void setServletRequest(ServletRequest request) {
		this.request = request;
	}
	
}