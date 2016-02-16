package org.summer.dp.cms.helper.string;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public final class CodeUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 日期转字符串
	 * @Title: dateToString 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param date
	 * @param @param format
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @throws
	 */
	public static String dateToString(String format, Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
        return sdf.format(date);
	}
	
	/**
	 * 随机生产6位验证码
	 * @Title: getAuthCode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public static int getAuthCode(){
		Random random = new Random();
		return random.nextInt(999999);
	}
}
