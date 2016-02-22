package org.summer.dp.cms.vo;
/**
 * DPCMS当前登录人能访问的请求列表
 * @author 赵宝东
 *
 */
public class Permission {
	private String requestUri;//不匹配requestUri的请求就是没有配置该权限

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public boolean equals(String currentRequestUri){
		if(this.requestUri.equals(currentRequestUri))
			return true;
			
		return false;

	}
}
