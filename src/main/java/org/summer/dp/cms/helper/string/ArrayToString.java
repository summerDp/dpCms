package org.summer.dp.cms.helper.string;

import java.util.ArrayList;




public class ArrayToString<T>{
	
	

	private static final String LEFT_BRACKET = "(";
	private static final String RIGHT_BRACKET = ")";

	public static String toString(Object ...array){
		StringBuffer stringBuffer = new StringBuffer(array.length*4);
		//stringBuffer.append(" in ");
		stringBuffer.append(LEFT_BRACKET);
		
		boolean isString = array[0] instanceof String;
		for(int i = 0, l = array.length - 1; i < l; ++i){
			if(isString) {
				stringBuffer.append('\'');
				stringBuffer.append(String.valueOf(array[i]));
				stringBuffer.append('\'');
			}
			else {
				stringBuffer.append(String.valueOf(array[i]));
			}
			stringBuffer.append(',');
		}
		if(isString) {
			stringBuffer.append('\'');
			stringBuffer.append(String.valueOf(array[array.length - 1]));
			stringBuffer.append('\'');
		}
		else {
			stringBuffer.append(String.valueOf(array[array.length - 1]));
		}
		
		stringBuffer.append(RIGHT_BRACKET);
		return stringBuffer.toString();
	}
	public static String toString(Object[] array,String prefix){
		String arrStr = toString(array);
		StringBuffer stringBuffer = new StringBuffer(arrStr.length() + prefix.length());
		stringBuffer.append(prefix).append(arrStr);
		return stringBuffer.toString(); 
	}


//
//	public static void main(String[] args) {
//		String[] strArray = {"1","2"};
//		Long[] longArray = {1l,2l,3l,4l};
//		ArrayList<String> list = new ArrayList<String>();
//		list.add("a");
//		list.add("b");
//		list.add("c");
//		System.out.println(ArrayToString.toString(strArray));
//		System.out.println(ArrayToString.toString(longArray));
//		System.out.println(ArrayToString.toString(list));
//		System.out.println(ArrayToString.toString(longArray,"in "));
//	}

}
