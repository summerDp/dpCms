package org.summer.dp.cms.support;

public class StateCode {
	public static final short OK = 1;//成功
	
	public static final short VALID_FAIL = 100;//表单提交验证失败
	
	public static final short LoginTwice = 101;//其他地方登录
	public static final short sidNo = 103;//此sid已经过期或不存在
	public static final short paramNull = 104;//请求参数不够
	
	public static final short LOGIN_TIMEOUT = -888;//登录超时
	public static final short LOGIN_FAIL = -999;//登录异常
	public static final short userExist = 1101;//此用户已经存在
	public static final short codeNo = 1102;//验证码不正确
	public static final short codeSms = 1103;//一分钟超过两条短信
	public static final short normal = -1;//普通错误,无数据,账号密码错误等
	public static final short keyNo = 3;//安全KEY错误
	
	public static final short FAIL = 900;//失败
	public static final short SYS = 999;//系统问题

}
