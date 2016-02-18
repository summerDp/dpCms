package org.summer.dp.cms.helper.language;

import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


/**
 * 暂时为员工管理激活帐号服务
 * 如需要新的功能自行进行添加
 * @author dong
 *
 */
public class Pinyin4jUtil {
	private static HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
	private final static String abc[]={"A","B","C","D","E","F","G","H","I",
		"J","K","L","M","N","O","P","Q","R",
		"S","T","U","V","W","X","Y","Z",
		"a","b","c","d","e","f","g","h","i",
		"j","k","l","m","n","o","p","q","r",
		"s","t","u","v","w","x","y","z",
		"1","2","3","4","5","6","7","8","9","0"};

	static{
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	/**
	 * 
	 * @param inputString 赵宝东
	 * @return zhaobd
	 */
	public static String toHanyuPinyin(String inputString){
		StringBuffer outputString = new StringBuffer();
		try {
			char[] cs = inputString.toCharArray();
			for(byte i=0;i<cs.length;i++){

				String[] hanyuPinyin = PinyinHelper.toHanyuPinyinStringArray(cs[i], hanyuPinyinOutputFormat);
				String s = hanyuPinyin[0];
		
				if(i==0){	
					outputString.append(s);
				}else if(i>0){
					outputString.append(s.substring(0, 1));
				}


			}
		}
		catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}

		return outputString.toString();
	}


	public static String genRandomPassword(){
		int length = 6;

		Random rand = new Random();
		StringBuffer password = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			password.append(abc[rand.nextInt(abc.length)]);
		}
		return password.toString();
	}


}
