package org.summer.dp.cms.support.persistence;

import java.util.ArrayList;
import java.util.List;

public class OrderByList {
	private List<OrderParam> orderParams = new ArrayList<OrderParam>(0);

	public List<OrderParam> getOrderParams() {
		return orderParams;
	}

	public void setOrderParams(List<OrderParam> orderParams) {
		this.orderParams = orderParams;
	}
	
	public void setOrderParam(OrderParam orderParam) {
		this.orderParams.add(orderParam);
	}
}
