package org.summer.dp.cms.support;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @ClassName: Response
 * @Description: 接口封装JSON格式
 * @author 赵宝东
 * @date 2015年3月20日 下午3:44:47
 * 
 */
public class Response implements Serializable {

	private static final long serialVersionUID = -1154107344366063475L;
	private short stateCode;
	private String message;
	private Object data;
	private String sid;
	private String fieldPaths[];  // 设置可访问的带有延迟加载标记的
	private String datePattern;  // 日期模版

	// 验证错误信息
	private Map<String, String> validErrors = new LinkedHashMap<String, String>(
			4);

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public short getStateCode() {
		if (validErrors.size() > 0) {
			stateCode = StateCode.VALID_FAIL;
			message = "验证失败";
		}
		return stateCode;
	}

	public void setStateCode(short stateCode) {
		this.stateCode = stateCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
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
	
	public void setFieldPaths(List<String> fieldPaths) {
		if(fieldPaths != null && fieldPaths.size() > 0) {
			this.fieldPaths = new String[fieldPaths.size()];
			fieldPaths.toArray(this.fieldPaths);
		}
	}
	public String getDatePattern() {
		return datePattern;
	}
	
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	public Map<String, String> getValidErrors() {
		return validErrors;
	}

	public void setValidErrors(Map<String, String> validErrors) {
		this.validErrors = validErrors;
	}

	public void addValidError(String property, String message) {
		validErrors.put(property, message);
	}
}
