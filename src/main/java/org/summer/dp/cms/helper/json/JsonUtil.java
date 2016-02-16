package org.summer.dp.cms.helper.json;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.summer.dp.cms.support.ReduceJsonDataParam;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class JsonUtil {
	// 根据这些值赋予相应的策略
	public static final byte ENTITY = 0; // 说明是实体类
	public static final byte DATA = 6;      // 原始类型
	public static final byte DATA_NUMBER = 1; // 说明是数字类型数据
	public static final byte DATA_STRING = 25; // 说明是字符串类型数据
	public static final byte DATA_SQL_DATE = 9; // 认为调用他的getTime方法
	public static final byte DATA_SQL_TIME = 10; 
	public static final byte DATA_SQL_TIMESTMP = 11; 
	public static final byte DATA_UTIL_DATE = 12;
	public static final byte DATA_UTIL_CAL = 13;
	public static final byte DATA_UTIL_TIMEZONE = 13;
	public static final byte DATA_UTIL_CURR = 13;
	
	public static final byte OTHER = 4; // 说明是其他未知的数据类型
	
	public static final byte COLLECTION = 7;
	public static final byte NOT_JUDGED = Byte.MIN_VALUE;  // 未判断的
	public static final byte INTERFACE = 35;
	public static final int NOT_FOUND = -1;
	public static final String HIBERNTE_HANDLER_SETTER = "setHandler";
	
	public static final String HIBERNTE_HANDLER_GETTER = "getHandler";
	public static final Logger logger = Logger.getLogger(JsonUtil.class);
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);

	/**
	 * @see 获得该属性的getter方法
	 * @param propDesc
	 * @return
	 */
	private static Method getReadMethod(PropertyDescriptor propDesc) {
		Method getter = propDesc.getReadMethod();
		if(getter == null) {
			throw new IllegalArgumentException("\"" + propDesc.getName() + "\"没有相应的getter方法");
		}
		return getter;
	}
	
	@SuppressWarnings("rawtypes")
	private static void setCollectionNull(Object obj, Object emptyArray[], Object empty) throws Exception {
		Iterator valueIterator = ((Iterable) obj).iterator();
		byte strategy = NOT_JUDGED;
		if(valueIterator.hasNext()) {
			Object valueIteratorObj = valueIterator.next();
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				setCollectionOrEntityNull(valueIteratorObj, emptyArray, empty, strategy);
			}
		}
		while(valueIterator.hasNext()) {
			setCollectionOrEntityNull(valueIterator.next(), emptyArray, empty, strategy);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void reduceJsonCollectionDataHandler(Object obj, String fieldPathsSep[][], int depth, final int maxDepth) throws Exception {
		Iterator valueIterator = ((Iterable) obj).iterator();
		byte strategy = NOT_JUDGED;
		if(valueIterator.hasNext()) {
			Object valueIteratorObj = valueIterator.next();
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				reduceJsonDataHandler(valueIteratorObj, fieldPathsSep, depth, maxDepth, strategy);
			}
		}
		while(valueIterator.hasNext()) {
			reduceJsonDataHandler(valueIterator.next(), fieldPathsSep, depth, maxDepth, strategy);
		}
	}
	
	/**
	 * @see 检查数组
	 * @param name
	 * @param twoDimensionArray
	 * @param depth
	 * @return
	 */
	public static int contains(String name, String twoDimensionArray[][], int depth) {
		for(int i = 0, l = twoDimensionArray.length; i < l; ++i) {
			if(depth < twoDimensionArray[i].length) {
					if(name.equals(twoDimensionArray[i][depth])) { return i; }
			}
		}
		return NOT_FOUND;
	}
	
	public static byte judge2(Class<?> beanClass) {
//		if(beanClass.isPrimitive()) { return DATA; }  // 减少boolean char byte short int long float double的比较
		if(beanClass == boolean.class || beanClass == char.class || beanClass == Boolean.class 
				|| beanClass == String.class || beanClass == Character.class) { return DATA_STRING; }
		
		if(beanClass == Byte.class || beanClass == Long.class || beanClass == Float.class
				 || beanClass == Double.class || beanClass == Short.class || beanClass == Integer.class
				 || beanClass == byte.class || beanClass == short.class || beanClass == int.class
				 || beanClass == long.class || beanClass == float.class || beanClass == double.class) { return DATA_NUMBER; }
		
		
		if(beanClass == Class.class || beanClass == Blob.class || beanClass == Clob.class) { return OTHER; }
		
		if(beanClass == Date.class) { return DATA_STRING; }
		
		if(beanClass == java.sql.Date.class) { return DATA_STRING; }
		if(beanClass == java.sql.Time.class) { return DATA_STRING; }
		if(beanClass == java.sql.Timestamp.class) { return DATA_STRING; }
//		if(beanClass == java.sql..class) { return DATA_STRING; }
		
		if(beanClass == Calendar.class) { return DATA_STRING; }
		if(beanClass == TimeZone.class) { return DATA_STRING; }
		if(beanClass == Currency.class) { return DATA_STRING; }
		if(beanClass == Locale.class) { return OTHER; }
		
		if(beanClass == List.class || beanClass == ArrayList.class || beanClass == Vector.class 
				|| beanClass == LinkedList.class || beanClass == Stack.class ) { return COLLECTION; }
		
		if(beanClass == Set.class || beanClass == HashSet.class
				 || beanClass == TreeSet.class || beanClass == LinkedHashSet.class) { return COLLECTION; }
//		org.hibernate.collection.internal.PersistentBag a;
//		if(beanClass == PersistentBag.class || beanClass == PersistentList.class || beanClass == PersistentSet.class
//				|| beanClass == PersistentSortedSet.class || beanClass == PersistentIdentifierBag.class) { return COLLECTION; }
		
		// java.util.Collections.UnmodifiableRandomAccessList  不能识别这里的集合类
		String dataType = beanClass.getCanonicalName();
		String leftOfString = "java.";
		if(dataType.startsWith(leftOfString)) {  // 以java开头
			String middleOfString = "util.";
			if(dataType.length() > (leftOfString.length() + middleOfString.length() + 3) 
					&& dataType.regionMatches(leftOfString.length(), middleOfString, 0, middleOfString.length())) {
				if(dataType.endsWith("List") || dataType.endsWith("Set")) { return COLLECTION; }
			}
		}
//		System.err.println(beanClass.getCanonicalName());
		return ENTITY;
	}
	
	/**
	 * @see 通过数据类型判断出应该使用哪一种策略
	 * @param beanClass
	 * @return
	 */
	public static byte judge(Class<?> beanClass) {
		if(beanClass.isPrimitive()) { return DATA; }  // 减少boolean byte char short int long float double的比较
//		if(beanClass.isInterface()) { return INTERFACE; }
		String dataType = beanClass.getCanonicalName();
		// 使用startsWith比equals的性能更好
		String leftOfString = "java.";
		if(dataType.startsWith(leftOfString)) {  // 以java开头
			String middleOfString = "lang.";
			int lastIndex = leftOfString.length() + middleOfString.length();
			if(dataType.length() > (lastIndex + 3) 
					&& dataType.regionMatches(leftOfString.length(), middleOfString, 0, middleOfString.length())) {
				if(dataType.endsWith("Boolean")) { return DATA_STRING; }
				if(dataType.endsWith("String")) { return DATA_STRING; }
				if(dataType.endsWith("Character")) { return DATA_STRING; }
				if(dataType.endsWith("Byte")) { return DATA_NUMBER; }
				if(dataType.endsWith("Long")) { return DATA_NUMBER; }
				if(dataType.endsWith("Float")) { return DATA_NUMBER; }
				if(dataType.endsWith("Double")) { return DATA_NUMBER; }
				if(dataType.endsWith("Short")) { return DATA_NUMBER; }
				if(dataType.endsWith("Integer")) { return DATA_NUMBER; }
				if(dataType.endsWith("Class")) { return OTHER; }
			}
			middleOfString = "sql.";
			lastIndex = leftOfString.length() + middleOfString.length();
			if(dataType.length() > (lastIndex + 3) 
					&& dataType.regionMatches(leftOfString.length(), middleOfString, 0, middleOfString.length())) {
				if(dataType.endsWith("Date")) { return DATA_SQL_DATE; }
				if(dataType.endsWith("Time")) { return DATA_SQL_TIME; }
				if(dataType.endsWith("Timestamp")) { return DATA_SQL_TIMESTMP; }
				if(dataType.endsWith("Blob")) { return OTHER; }
				if(dataType.endsWith("Clob")) { return OTHER; }
			}
			
			middleOfString = "util.";
			lastIndex = leftOfString.length() + middleOfString.length();
			if(dataType.length() > (lastIndex + 3) 
					&& dataType.regionMatches(leftOfString.length(), middleOfString, 0, middleOfString.length())) {
				if(dataType.endsWith("Date")) { return DATA_UTIL_DATE; }
				if(dataType.endsWith("Calendar")) { return DATA_UTIL_CAL; }
				if(dataType.endsWith("TimeZone")) { return DATA_UTIL_TIMEZONE; }
				if(dataType.endsWith("Currency")) { return DATA_UTIL_CURR; }
				if(dataType.endsWith("Locale")) { return OTHER; }
				if(dataType.endsWith("List")) { return COLLECTION; }
				if(dataType.endsWith("Set")) { return COLLECTION; }
			}
			if(dataType.endsWith("io.Serializable")) { return OTHER; }
			if(dataType.endsWith("math.BigDecimal")) { return DATA_NUMBER; }
		}
		
		if(dataType.startsWith("org.hibernate.collection.")) { // org.hibernate.collection.internal.Persistent
			return COLLECTION;
		}
		return ENTITY;
	}
	
	/**
	 * @see 将一个Object序列化成字符串
	 * @param object
	 * @param features
	 * @return
	 */
	public static String toJSONString(Object object, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();
        try {
            SerializeConfig config = new SerializeConfig();
			JSONSerializer serializer = new JSONSerializer(out,config );
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }
	
	/**
	 *  将一个包含Date类型数据的Object序列化成字符串
	 * @param object
	 * @param dateFormat
	 * @param features
	 * @return
	 */
	public static String toJSONStringWithDateFormat(Object object, String dateFormat, SerializerFeature... features) {
        SerializeWriter out = new SerializeWriter();
        try {
            SerializeConfig config = new SerializeConfig();
			JSONSerializer serializer = new JSONSerializer(out,config );
            if(dateFormat != null) {
            	serializer.setDateFormat(dateFormat);
            }
            for (com.alibaba.fastjson.serializer.SerializerFeature feature : features) {
                serializer.config(feature, true);
            }
            serializer.write(object);
            return out.toString();
        } finally {
            out.close();
        }
    }
	
	/**
	 * @deprecated
	 * @param obj
	 * @param rjdParams
	 */
	public static void reduceJsonData2(Object obj, ReduceJsonDataParam rjdParams) {
		if(rjdParams != null) {
			reduceJsonData2(obj, rjdParams.getRjdParams());
		}
		else {
			String _fieldPaths[] = null;
			reduceJsonData(obj, _fieldPaths);
		}
	}
	
	/**
	 * @deprecated
	 * @see 根据路径保留数据，结合Hibernate的延迟数据策略，达到优化读取数据的目的
	 * @param obj, 为了最大限度加快计算速度， obj必须是类型明确的，例如 Person p; List<Person> ps; Set<Person> ps;
	 * @param fieldPaths
	 */
	public static void reduceJsonData2(Object obj, List<String> fieldPaths) {
		String _fieldPaths[] = null;
		if(fieldPaths == null || fieldPaths.size() == 0) {
			reduceJsonData(obj, _fieldPaths);
		}
		else {
			_fieldPaths = new String[fieldPaths.size()];
			fieldPaths.toArray(_fieldPaths);
			reduceJsonData(obj, _fieldPaths);
		}
	}
	
	/**
	 * @deprecated
	 * @see 根据路径保留数据，结合Hibernate的延迟数据策略，达到优化读取数据的目的
	 * @param obj, 为了最大限度加快计算速度， obj必须是类型明确的，例如 Person p; List<Person> ps; Set<Person> ps;
	 * @param fieldPaths
	 */
	public static void reduceJsonData(Object obj, String fieldPaths[]) {
		if (obj == null) { return; }
		Object emptyArray[] = new Object[0];  // 用于getter方法
		Object empty = null;
		Class<?> beanClass = obj.getClass();
		byte objStrategy = judge(beanClass);  // 根据数据类型判断应该使用什么策略
		if (fieldPaths == null || fieldPaths.length == 0) {
			try {
				if(objStrategy == COLLECTION ) {
					setCollectionNull(obj, emptyArray, empty);
				}
				else if(objStrategy == ENTITY) {
					setCollectionOrEntityNull(obj, emptyArray, empty, objStrategy);
				}
			}
			catch(Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			return; 
		}
		String fieldPathsSep[][] = new String[fieldPaths.length][];
		int mdepth = Integer.MIN_VALUE;
		int depth = 0;
		for(int i = 0, l = fieldPaths.length, t; i < l; ++i) {
			fieldPathsSep[i] = StringUtils.split(fieldPaths[i], '.');
			t = fieldPathsSep[i].length;
			if(mdepth < t) { mdepth = t; }
		}
		final int maxDepth = mdepth - 1;
		
		try {
			if(objStrategy == COLLECTION) {  // 使用LIST和SET的策略
				reduceJsonCollectionDataHandler(obj, fieldPathsSep, depth, maxDepth);
			}
			else if(objStrategy == ENTITY) {  // 使用实体类策略
				BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
				PropertyDescriptor propDescs[] = beanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
				for (PropertyDescriptor propDesc : propDescs) {
					int idx = contains(propDesc.getName(), fieldPathsSep, depth);
					if (idx != NOT_FOUND) {  // 在当前的深度下，找到该属性
						Object value = getReadMethod(propDesc).invoke(obj, emptyArray);
						if(value != null) {  // 把org.hibernate.collection.internal.PersistentSet当成实体，错误！
							byte valueStrategy = judge(value.getClass());
							if(fieldPathsSep[idx].length - 1 == depth) {  // 到达最后
								if(valueStrategy == COLLECTION ) {
									setCollectionNull(value, emptyArray, empty);
								}
								else if(valueStrategy == ENTITY) {
									setCollectionOrEntityNull(value, emptyArray, empty, valueStrategy);
								}
							}
							else {
								if(valueStrategy == COLLECTION ) {
									reduceJsonCollectionDataHandler(value, fieldPathsSep, depth + 1, maxDepth);
								}
								else if(valueStrategy == ENTITY) {
									reduceJsonDataHandler(value, fieldPathsSep, depth + 1, maxDepth, valueStrategy); 
								}
							}
						}
					}
					else {
						Method propSetter = propDesc.getWriteMethod();
						if(propSetter != null) {
							byte propStrategy = judge(propDesc.getPropertyType());
							if(propStrategy == COLLECTION || propStrategy == ENTITY) {  // 如果是集合或者实体，则将其置为null
								if(!HIBERNTE_HANDLER_SETTER.equals(propSetter.getName())) {
									propSetter.invoke(obj, empty);  // 该数据置为null
								}
							}
						}
					}
				}
			}
		}catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * @see 将value里面的LIST、SET和ENTITY设置为null
	 * @param value
	 * @param emptyArray
	 * @param empty
	 * @throws Exception
	 */
	private static void setCollectionOrEntityNull(Object value, Object emptyArray[], Object empty, byte strategy) throws Exception {
		if(strategy == NOT_JUDGED) {  // 当发现未执行判断策略时候
			strategy = judge(value.getClass());
		}
		if(strategy == COLLECTION) {
			setCollectionNull(value, emptyArray, empty);
		}
		else if(strategy == ENTITY){
			BeanInfo valueBeanInfo = Introspector.getBeanInfo(value.getClass());
			PropertyDescriptor valuePropDescs[] = valueBeanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
			for (PropertyDescriptor valuePropDesc : valuePropDescs) {
				Method valueSetter = valuePropDesc.getWriteMethod();
				Method valueGetter = valuePropDesc.getReadMethod();
				if (valueSetter != null && (!HIBERNTE_HANDLER_SETTER.equals(valueSetter.getName())) && valueGetter != null) {
					Object valuePropDescVal = valueGetter.invoke(value, emptyArray);
					if(valuePropDescVal != null) {
						byte valuePropDescValStrategy = judge(valuePropDescVal.getClass());
						if(valuePropDescValStrategy == COLLECTION || valuePropDescValStrategy == ENTITY ) {
							valueSetter.invoke(value, empty);  // 该数据置为null
						}
					}
				}
			}
		}
	}
	
	/**
	 * @see 依据dataPath给出的路径，将obj不必要的Hibernate延迟加载实体或者集合清除，建议尽量使用的Hibernate的延迟策略
	 * @see 遇到DATA*的数据保留，遇到ENTITY*, LIST*和SET*的基于路径
	 * @see
	 * @param obj
	 * @param dataPath
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static void reduceJsonDataHandler(Object obj, String fieldPaths[][], int depth, final int maxDepth, byte strategy) throws Exception {
		if(depth > maxDepth) { return; }  // depth大于maxDepth说明不能再遍历了
		if (obj == null) { return; }
		Class<?> objBeanClass = obj.getClass();
		byte objStrategy = strategy;
		if(strategy == NOT_JUDGED) {  // 目的在于减少类型的判断
			objStrategy = judge(objBeanClass);  // 根据数据类型判断应该使用什么策略
		}
		if(objStrategy == COLLECTION) {  // 使用LIST和SET的策略
			Iterator objIterator = ((Iterable) obj).iterator();
			while(objIterator.hasNext()) { // 把集合里的Object扔给reduceJsonDataHandler处理
				reduceJsonDataHandler(objIterator.next(), fieldPaths, depth, maxDepth, NOT_JUDGED);  
			} 
		}
		else if(objStrategy == ENTITY) {  // 使用实体类策略
			BeanInfo objBeanInfo = Introspector.getBeanInfo(objBeanClass);
			PropertyDescriptor objPropDescs[] = objBeanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
			Object empty = null;
			Object emptyArray[] = new Object[0];
			for (PropertyDescriptor objPropDesc : objPropDescs) {
				int idx = contains(objPropDesc.getName(), fieldPaths, depth);
				if (idx != NOT_FOUND) {  // 能够找到
					Object objPropValue = getReadMethod(objPropDesc).invoke(obj, emptyArray);
					if(objPropValue != null) {  // 如果不是空值，则要考虑
						if(fieldPaths[idx].length - 1 == depth) {// 路径的最后
							setCollectionOrEntityNull(objPropValue, emptyArray, empty, NOT_JUDGED);
						} 
						else {
							byte valueStrategy = judge(objPropValue.getClass());
							if(valueStrategy == COLLECTION) {
								Iterator objPropValueIterator = ((Iterable) objPropValue).iterator();
								while(objPropValueIterator.hasNext()) { // 把集合里的Object扔给reduceJsonDataHandler处理
									reduceJsonDataHandler(objPropValueIterator.next(), fieldPaths, depth + 1, maxDepth, NOT_JUDGED);  
								} 
							}
							else if(valueStrategy == ENTITY) {
								reduceJsonDataHandler(objPropValue, fieldPaths, depth + 1, maxDepth, valueStrategy);
							}
						}
					}
				}
				else {
					Method setter = objPropDesc.getWriteMethod();
					if (setter != null && (!HIBERNTE_HANDLER_SETTER.equals(setter.getName()))) {
						byte objPropDescStrategy = judge(objPropDesc.getPropertyType());
						
						if(objPropDescStrategy == COLLECTION || objPropDescStrategy == ENTITY) {
							setter.invoke(obj, empty);  // 该数据置为null
						}
					}
				}
			}
		}
	}
}
