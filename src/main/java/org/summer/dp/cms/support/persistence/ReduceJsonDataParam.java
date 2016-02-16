package org.summer.dp.cms.support.persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * @category 这个类用于保存前端传过来减少Hibernate发送SQL
 * @author Administrator
 *
 */
public class ReduceJsonDataParam {
	private List<String> rjdParams = new ArrayList<String>(3);

	public List<String> getRjdParams() {
		return rjdParams;
	}

	public void setRjdParams(List<String> rjdParams) {
		this.rjdParams = rjdParams;
	}
}
