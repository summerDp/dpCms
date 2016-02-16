package org.summer.dp.cms.helper.json;

import java.util.List;

import org.summer.dp.cms.support.ReduceJsonDataParam;
import org.summer.dp.cms.support.Response;
import org.summer.dp.cms.support.StateCode;

public class JsonData {
	private Response response;
	private Object mainEntranceObj = null;
	private String fieldPaths[];
	
	public JsonData() {
		response = new Response();
		response.setStateCode(StateCode.OK);
		response.setMessage("SUCCESS");
	}
	
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}
	public Object getMainEntranceObj() {
		return mainEntranceObj;
	}
	public void setMainEntranceObj(Object mainEntranceObj) {
		this.mainEntranceObj = mainEntranceObj;
	}
	public String[] getFieldPaths() {
		return fieldPaths;
	}
	public void setFieldPaths(String[] fieldPaths) {
		this.fieldPaths = fieldPaths;
	}
	
	public void setFieldPaths(ReduceJsonDataParam rjdParam) {
		if(rjdParam != null) {
			List<String> fieldPaths = rjdParam.getRjdParams();
			if(fieldPaths != null && fieldPaths.size() > 0) {
				this.fieldPaths = new String[fieldPaths.size()];
				fieldPaths.toArray(this.fieldPaths);
			}
		}
	}
	
}
