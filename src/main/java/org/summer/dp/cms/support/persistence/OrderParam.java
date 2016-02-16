package org.summer.dp.cms.support.persistence;

public class OrderParam {
	private String field;
	private String type;
	
	public OrderParam() {
	}
	
	public OrderParam(String field) {
		this(field, null);
	}
	
	public OrderParam(String field, String type) {
		this.field = field;
		this.type = type;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
