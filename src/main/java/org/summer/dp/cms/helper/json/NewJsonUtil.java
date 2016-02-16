package org.summer.dp.cms.helper.json;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.summer.dp.cms.helper.json.node.TreeNode;
import org.summer.dp.cms.helper.json.node.TreePath;

public class NewJsonUtil {
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
	public static final byte DATA_UTIL_TIMEZONE = 14;
	public static final byte DATA_UTIL_CURR = 15;
	public static final byte OTHER = 4; // 说明是其他未知的数据类型
	
	public static final byte COLLECTION = 7;
	public static final byte ARRAY = 8;

	public static final byte NOT_JUDGED = Byte.MIN_VALUE;  // 未判断的
	
	public static final String HIBERNTE_HANDLER = "Handler";
	public static final String JAVA = "java.";
	public static final String SQL = "sql.";
	public static final String UTIL = "util.";
	public static final String LANG = "lang.";
	public static final int HIBERNTE_HANDLER_LENGTH = HIBERNTE_HANDLER.length() + 3;
	
	public static final Logger logger = Logger.getLogger(JsonUtil.class);
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat SDF = new SimpleDateFormat(DATE_FORMAT);
	
	public static final char LEFT_ARRAY = '[';
	public static final char RIGHT_ARRAY = ']';
	public static final char LEFT_OBJECT = '{';
	public static final char RIGHT_OBJECT = '}';
	public static final char COMMA = ',';
	public static final char QUOTE = '"';
	public static final char COLON = ':';
	
	@SuppressWarnings("rawtypes")
	public static boolean isEmptyCollection(Object obj) {
		return obj == null || ((Collection) obj).size() == 0;
	}
	
	public static boolean isEmptyArray(Object obj) {
		return obj == null || ((Object[]) obj).length == 0;
	}
	
	private static boolean isHibernateHandler(Method getter) {
		String getterName = getter.getName();
		if(getterName.length() == HIBERNTE_HANDLER_LENGTH) {
			return getterName.endsWith(HIBERNTE_HANDLER);
		}
		return false;
	}
	
	private static void setPropertyName(StringBuilder sb, PropertyDescriptor propDesc) {
		sb.append(QUOTE);
		sb.append(propDesc.getName());
		sb.append(QUOTE);
		sb.append(COLON);
	}
	
	private static Method getReadMethod(PropertyDescriptor propDesc) {
		Method getter = propDesc.getReadMethod();
		if(getter == null) {
			throw new IllegalArgumentException("\"" + propDesc.getName() + "\"没有相应的getter方法");
		}
		return getter;
	}
	
	public static byte judge(Class<?> beanClass) {
//		if(beanClass.isMemberClass()) {
//			return OTHER;
//		}
		if(beanClass.isArray()) {
			return ARRAY;
		}
		if(beanClass.isPrimitive()) {
			return DATA;
		}  // 减少boolean byte char short int long float double的比较
//		if(beanClass.isInterface()) { return INTERFACE; }
		String dataType = beanClass.getCanonicalName();
		// 使用startsWith比equals的性能更好
//		String leftOfString = "java.";
		if(dataType.startsWith(JAVA)) {  // 以java开头
//			String middleOfString = "lang.";
			int javaLen = JAVA.length();
			int lastIndex = javaLen + LANG.length();
			if(dataType.length() > (lastIndex + 3) 
					&& dataType.regionMatches(javaLen, LANG, 0, LANG.length())) {
				if(dataType.endsWith("Boolean")) { return DATA; }
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
//			middleOfString = "sql.";
			lastIndex = javaLen + SQL.length();
			if(dataType.length() > (lastIndex + 3) 
					&& dataType.regionMatches(javaLen, SQL, 0, SQL.length())) {
				if(dataType.endsWith("Date")) { return DATA_SQL_DATE; }
				if(dataType.endsWith("Time")) { return DATA_SQL_TIME; }
				if(dataType.endsWith("Timestamp")) { return DATA_SQL_TIMESTMP; }
				if(dataType.endsWith("Blob")) { return OTHER; }
				if(dataType.endsWith("Clob")) { return OTHER; }
			}
			
//			middleOfString = "util.";
			lastIndex = javaLen + UTIL.length();
			if(dataType.length() > (lastIndex + 2) 
					&& dataType.regionMatches(javaLen, UTIL, 0, UTIL.length())) {
				if(dataType.endsWith("Date")) { return DATA_UTIL_DATE; }
				if(dataType.endsWith("Calendar")) { return DATA_UTIL_CAL; }
				if(dataType.endsWith("TimeZone")) { return DATA_UTIL_TIMEZONE; }
				if(dataType.endsWith("Currency")) { return DATA_UTIL_CURR; }
				if(dataType.endsWith("Locale")) { return OTHER; }
				if(dataType.endsWith("List")) { return COLLECTION; }
				if(dataType.endsWith("Set")) { return COLLECTION; }
			}
			if(dataType.endsWith("io.Serializable")) { return DATA_STRING; }
			if(dataType.endsWith("math.BigDecimal")) { return DATA_NUMBER; }
		}
		
		if(dataType.startsWith("org.hibernate.collection.")) {
			return COLLECTION;
		}
		return ENTITY;
	}
		
	public static void convertObject2String(Object obj, StringBuilder sb, byte strategy) {
		if(strategy == DATA || strategy == DATA_NUMBER){ sb.append(obj); return; }
		sb.append(QUOTE);
		if(strategy == DATA_SQL_DATE) { sb.append(SDF.format(new Date(((java.sql.Date)obj).getTime()))); }
		else if(strategy == DATA_SQL_TIME) { sb.append(SDF.format(new Date(((java.sql.Time)obj).getTime()))); }
		else if(strategy == DATA_SQL_TIMESTMP) { sb.append(SDF.format(new Date(((java.sql.Timestamp)obj).getTime()))); }
		else if(strategy == DATA_UTIL_DATE) { sb.append(SDF.format(((Date)obj).getTime())); }
		else if(strategy == DATA_UTIL_CAL) { sb.append(SDF.format(((java.util.Calendar)obj).getTime())); }
		else if(strategy == DATA_STRING){ sb.append(obj); }
		sb.append(QUOTE);
	}
	
	/**
	 * @see 忽略Collection和Entity
	 * @param entity
	 * @param sb
	 * @param emptyArray
	 * @throws Exception
	 */
	public static void convertEntity2String(Object entity, StringBuilder sb, Object emptyArray[])  throws Exception {
		BeanInfo entityBeanInfo = Introspector.getBeanInfo(entity.getClass());
		PropertyDescriptor entityPropDescs[] = entityBeanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
		sb.append(LEFT_OBJECT);
		for (PropertyDescriptor entityPropDesc : entityPropDescs) {
			Method entityGetter = entityPropDesc.getReadMethod();
			if (entityGetter != null && (!isHibernateHandler(entityGetter))) {
				if(!entityGetter.isAccessible()) {
					entityGetter.setAccessible(true);  // 使这个getter方法可以访问
				}
				Object entityPropDescVal = entityGetter.invoke(entity, emptyArray);
				if(entityPropDescVal != null) {
					byte strategy = judge(entityPropDescVal.getClass());
					if(strategy == COLLECTION || strategy == ENTITY || strategy == ARRAY || strategy == OTHER) { } // || strategy == INTERFACE 
					else {
						setPropertyName(sb, entityPropDesc);
						convertObject2String(entityPropDescVal, sb, strategy);
						sb.append(COMMA);
					}
				}
			}
		}
		deleteLastChar(sb);
		sb.append(RIGHT_OBJECT);
	}
	
	@SuppressWarnings("rawtypes")
	public static void convertCollection2String(Object collection, StringBuilder sb, Object emptyArray[])  throws Exception {
		byte strategy = NOT_JUDGED;
		Iterator valueIterator = ((Iterable)collection).iterator();
		sb.append(LEFT_ARRAY);
		if(valueIterator.hasNext()) {
			Object valueIteratorObj = valueIterator.next();
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				ignoreCollectionOrEntity(valueIteratorObj, sb, emptyArray, strategy);
			}
			else { sb.append("null"); }
		}
		while(valueIterator.hasNext()) {
			sb.append(COMMA);
			ignoreCollectionOrEntity(valueIterator.next(), sb, emptyArray, strategy);
		}
		sb.append(RIGHT_ARRAY);
	}

	public static void convertArray2String(Object arrayObj, StringBuilder sb, Object emptyArray[])  throws Exception {
		byte strategy = NOT_JUDGED;
		Object array[] = (Object[])arrayObj;
		sb.append(LEFT_ARRAY);
		if(array.length > 0) {
			Object valueIteratorObj = array[0];
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				ignoreCollectionOrEntity(valueIteratorObj, sb, emptyArray, strategy);
			}
			else { sb.append("null"); }
			for(int i = 1, l = array.length; i < l; ++i) {
				sb.append(COMMA);
				ignoreCollectionOrEntity(array[i], sb, emptyArray, strategy);
			}
		}
		sb.append(RIGHT_ARRAY);
	}
	
	private static void ignoreCollectionOrEntity(Object value, StringBuilder sb, Object emptyArray[], byte strategy) throws Exception {
		// 当发现未执行判断策略时候
		if(strategy == NOT_JUDGED) { 
			strategy = judge(value.getClass()); 
		}
		if(strategy == COLLECTION) {
			convertCollection2String(value, sb, emptyArray);
		}
		if(strategy == ARRAY) {
			convertArray2String(value, sb, emptyArray);
		}
		else if(strategy == ENTITY){
			convertEntity2String(value, sb, emptyArray);
		}
		else if(strategy != OTHER) {
			convertObject2String(value, sb, strategy);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void entityHandler(Object obj, IdentityHashMap<Object, String> references, StringBuilder sb, 
			Map<String, TreeNode> nodes, Object emptyArray[], byte strategy) throws Exception {
		if (obj == null) { return; }		
		Class<?> objBeanClass = obj.getClass();
		byte objStrategy = strategy;
		if(strategy == NOT_JUDGED) {  // 目的在于减少类型的判断
			objStrategy = judge(objBeanClass);  // 根据数据类型判断应该使用什么策略
		}
		if(objStrategy == ARRAY) {
			Object array[] = (Object[])obj;
			sb.append(LEFT_ARRAY);
			if(array.length > 0) {
				Object valueIteratorObj = array[0];
				byte objIteratorValueStra = NOT_JUDGED;
				if(valueIteratorObj != null) {
					objIteratorValueStra = judge(valueIteratorObj.getClass());
					entityHandler(valueIteratorObj, references, sb, nodes, emptyArray, objIteratorValueStra);  
				}
				else { sb.append("null"); }
				for(int i = 1, l = array.length; i < l; ++i) {
					sb.append(COMMA);
					entityHandler(array[i], references, sb, nodes, emptyArray, objIteratorValueStra);  
				}
			}
			sb.append(RIGHT_ARRAY);
		}
		else if(objStrategy == COLLECTION) {  // 使用LIST和SET的策略
			Iterator objIterator = ((Iterable) obj).iterator();
			byte objIteratorValueStra = NOT_JUDGED;
			sb.append(LEFT_ARRAY);
			if(objIterator.hasNext()){
				Object objIteratorValue = objIterator.next();
				if(objIteratorValue != null) {
					objIteratorValueStra = judge(objIteratorValue.getClass());
					entityHandler(objIteratorValue, references, sb, nodes, emptyArray, objIteratorValueStra);  
				}
				else { 
					sb.append("null");
				}
			}			
			while(objIterator.hasNext()) { // 把集合里的Object扔给entityHandler处理
				sb.append(COMMA);
				entityHandler(objIterator.next(), references, sb, nodes, emptyArray, objIteratorValueStra);  
			}
			sb.append(RIGHT_ARRAY);
		}
		else if(objStrategy == ENTITY) {  // 使用实体类策略
			BeanInfo objBeanInfo = Introspector.getBeanInfo(objBeanClass);
			PropertyDescriptor objPropDescs[] = objBeanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
			sb.append(LEFT_OBJECT);
			for (PropertyDescriptor objPropDesc : objPropDescs) {
				Method getter = getReadMethod(objPropDesc);
				if(!getter.isAccessible()) {
					getter.setAccessible(true);  // 使这个getter方法可以访问
				}
				Object objPropValue = getter.invoke(obj, emptyArray);
//				if(objPropDesc.getName().equals("account")) {
//					System.err.println();
//				}
				if(objPropValue == null) { continue; } // 如果是空值，则不要考虑
				byte objPropDescStrategy = judge(objPropValue.getClass());
				if (objPropDescStrategy == COLLECTION || objPropDescStrategy == ENTITY || objPropDescStrategy == ARRAY) {  // 能够找到
					TreeNode node = nodes.get(objPropDesc.getName());
					if(node == null) { continue; }
//					if(references.containsKey(objPropValue)) {
//						continue;   // 发生循环引用
//					} 
//					else {
//						references.put(objPropValue, objPropDesc.getName());
//					}
					
					if(node.isLeaf()) {// 路径的最后
						if(objPropDescStrategy == COLLECTION) {
							if(isEmptyCollection(objPropValue)) { continue; }
							setPropertyName(sb, objPropDesc);
//							StringBuilder mySb = new StringBuilder();
							convertCollection2String(objPropValue, sb, emptyArray);
//							sb.append(mySb);
							sb.append(COMMA);
						}
						else if(objPropDescStrategy == ARRAY) {
							if(isEmptyArray(objPropValue)) { continue; }
							setPropertyName(sb, objPropDesc);
//							StringBuilder mySb = new StringBuilder();
							convertArray2String(objPropValue, sb, emptyArray);
//							sb.append(mySb);
							sb.append(COMMA);
						}
						else {
							setPropertyName(sb, objPropDesc);
							convertEntity2String(objPropValue, sb, emptyArray);
							sb.append(COMMA);
						}
					} 
					else {
						if(objPropDescStrategy == COLLECTION) {
							Collection coll = (Collection)objPropValue;
							if(coll.size() == 0) { continue; }
							setPropertyName(sb, objPropDesc);
							Iterator objPropValueIterator = coll.iterator();
							byte myStra = NOT_JUDGED;
							sb.append(LEFT_ARRAY);
							if(objPropValueIterator.hasNext()) {
								Object myObj = objPropValueIterator.next();
								if(myObj != null) {
									myStra = judge(myObj.getClass());
//									StringBuilder mySb = new StringBuilder();
									entityHandler(myObj, references, sb, node.children, emptyArray, myStra); 
//									sb.append(mySb);
								}
								else {
									sb.append("null");
								}
							}
							while(objPropValueIterator.hasNext()) {
								sb.append(COMMA);
//								StringBuilder mySb = new StringBuilder();
								entityHandler(objPropValueIterator.next(), references, sb, node.children, emptyArray, myStra); 
//								sb.append(mySb);
							}
							sb.append(RIGHT_ARRAY);
							sb.append(COMMA);
						}
						else if(objPropDescStrategy == ARRAY) {
							Object[] coll = (Object[])objPropValue;
							if(coll.length == 0) { continue; }
							setPropertyName(sb, objPropDesc);
							byte myStra = NOT_JUDGED;
							sb.append(LEFT_ARRAY);
							Object myObj = coll[0];
							if(myObj != null) {
								myStra = judge(myObj.getClass());
//								StringBuilder mySb = new StringBuilder();
								entityHandler(myObj, references, sb, node.children, emptyArray, myStra); 
//								sb.append(mySb);
							}
							else {
								sb.append("null");
							}
							for(int i = 1, l = coll.length; i < l; ++i) {
								sb.append(COMMA);
//								StringBuilder mySb = new StringBuilder();
								entityHandler(coll[i], references, sb, node.children, emptyArray, myStra);
//								sb.append(mySb);
							}
							sb.append(RIGHT_ARRAY);
							sb.append(COMMA);
						}
						else if(objPropDescStrategy == ENTITY) {
							setPropertyName(sb, objPropDesc);
//							StringBuilder mySb = new StringBuilder();
							entityHandler(objPropValue, references, sb, node.children, emptyArray, objPropDescStrategy);
//							sb.append(mySb);
							sb.append(COMMA);
						}
					}
					
				}
				else {
					if (!isHibernateHandler(getter)) {
						// 这些值都是要忽略的
						if(objPropDescStrategy == OTHER) { }  //  || objPropDescStrategy == INTERFACE
						else {
							setPropertyName(sb, objPropDesc);
							convertObject2String(objPropValue, sb, objPropDescStrategy);
							sb.append(COMMA);
						}
					}
				}
			}
			deleteLastChar(sb);
			sb.append(RIGHT_OBJECT);
		}
	}
	
	/**
	 * @see 用检测入口
	 * @param obj
	 * @param sb
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @param emptyArray
	 * @throws Exception
	 */
	private static void arrayIterator(Object obj, StringBuilder sb, Object mainEntranceObj, String fieldPaths[], 
			Object emptyArray[])  throws Exception {
		if(obj.equals(mainEntranceObj)) {
			sb.append(NewJsonUtil.toString(mainEntranceObj, fieldPaths));
			return;
		}
		Object array[] = (Object[])obj;
		sb.append(LEFT_ARRAY);
		if(array.length > 0) {
			byte strategy = NOT_JUDGED;
			Object value = array[0];
			if(value != null) {
				strategy = judge(value.getClass());
				entityHandler(value, sb, mainEntranceObj, fieldPaths, emptyArray, strategy);  
			}
			else { sb.append("null"); }
			for(int i = 1, l = array.length; i < l; ++i) {
				sb.append(COMMA);
				entityHandler(array[i], sb, mainEntranceObj, fieldPaths, emptyArray, strategy);  
			}
		}
		sb.append(RIGHT_ARRAY);
	}
	
	/**
	 * @see 用检测入口
	 * @param obj
	 * @param sb
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @param emptyArray
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static void collectionIterator(Object obj, StringBuilder sb, Object mainEntranceObj, String fieldPaths[], 
			Object emptyArray[])  throws Exception {
		if(obj.equals(mainEntranceObj)) {
			sb.append(NewJsonUtil.toString(mainEntranceObj, fieldPaths));
			return;
		}
		Iterator iterator = ((Iterable) obj).iterator();
		byte strategy = NOT_JUDGED;
		sb.append(LEFT_ARRAY);
		if(iterator.hasNext()){
			Object value = iterator.next();
			if(value != null) {
				strategy = judge(value.getClass());
				entityHandler(value, sb, mainEntranceObj, fieldPaths, emptyArray, strategy);  
			}
			else { 
				sb.append("null");
			}
		}			
		while(iterator.hasNext()) { // 把集合里的Object扔给entityHandler处理
			sb.append(COMMA);
			entityHandler(iterator.next(), sb, mainEntranceObj, fieldPaths, emptyArray, strategy);  
		}
		sb.append(RIGHT_ARRAY);
	}
	
	/**
	 * @see 用检测入口
	 * @param obj
	 * @param sb
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @param emptyArray
	 * @param objStrategy
	 * @throws Exception
	 */
	private static void entityHandler(Object obj, StringBuilder sb, Object mainEntranceObj, String fieldPaths[], 
			Object emptyArray[], byte objStrategy) throws Exception {
		if (obj == null) { return; }
		if(obj.equals(mainEntranceObj)) {  // 找到入口了使用
			sb.append(NewJsonUtil.toString(mainEntranceObj, fieldPaths));
			return;
		}
		Class<?> objBeanClass = obj.getClass();
		if(objStrategy == NOT_JUDGED) {  // 目的在于减少类型的判断
			objStrategy = judge(objBeanClass);  // 根据数据类型判断应该使用什么策略
		}
		if(objStrategy == ARRAY) {
			arrayIterator(obj, sb, mainEntranceObj, fieldPaths, emptyArray);
		}
		else if(objStrategy == COLLECTION) {  // 使用LIST和SET的策略
			collectionIterator(obj, sb, mainEntranceObj, fieldPaths, emptyArray);
		}
		else if(objStrategy == ENTITY) {  // 使用实体类策略
			BeanInfo objBeanInfo = Introspector.getBeanInfo(objBeanClass);
			PropertyDescriptor objPropDescs[] = objBeanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
			sb.append(LEFT_OBJECT);
			for (PropertyDescriptor objPropDesc : objPropDescs) {
				Method getter = getReadMethod(objPropDesc);
				if(!getter.isAccessible()) {
					getter.setAccessible(true);  // 使这个getter方法可以访问
				}
//				if(objPropDesc.getName().equals("account")) {
//					System.err.println();
//				}
				Object objPropValue = getter.invoke(obj, emptyArray);
				if(objPropValue == null) { continue; } // 如果是空值，则不要考虑
				byte objPropDescStrategy = judge(objPropValue.getClass());
//				if(objPropDesc.getName().equals("content")) {
//					System.err.println();
//				}
				if (objPropDescStrategy == COLLECTION || objPropDescStrategy == ENTITY || objPropDescStrategy == ARRAY) {  // 能够找到
					setPropertyName(sb, objPropDesc);
					if(objPropDescStrategy == ARRAY) {
						arrayIterator(objPropValue, sb, mainEntranceObj, fieldPaths, emptyArray);
						sb.append(COMMA);
					}
					else if(objPropDescStrategy == COLLECTION) {
						collectionIterator(objPropValue, sb, mainEntranceObj, fieldPaths, emptyArray);
						sb.append(COMMA);
					}
					else {
//						StringBuilder mySb = new StringBuilder();
						entityHandler(objPropValue, sb, mainEntranceObj, fieldPaths, emptyArray, objPropDescStrategy);
//						sb.append(mySb);
						sb.append(COMMA);
					}
				}
				else {
					if (!isHibernateHandler(getter)) {
						// 这些值都是要忽略的
						if(objPropDescStrategy == OTHER) { }  //  || objPropDescStrategy == INTERFACE
						else {
							setPropertyName(sb, objPropDesc);
							convertObject2String(objPropValue, sb, objPropDescStrategy);
							sb.append(COMMA);
						}
					}
				}
			}
			deleteLastChar(sb);
			sb.append(RIGHT_OBJECT);
		}
		else if(objStrategy != OTHER){
			NewJsonUtil.convertObject2String(obj, sb, objStrategy);			
		}
	}
	
	/**
	 * @see 删除某个位于最后位置的字符
	 * @param sb
	 * @param ch
	 */
	
	private static void deleteLastChar(StringBuilder sb) {
		int lastIdx = sb.length() - 1;
		if(lastIdx >= 0 && sb.charAt(lastIdx) == COMMA) {
			sb.deleteCharAt(lastIdx);
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private static void collectionHandler(Object obj, IdentityHashMap<Object, String> references, StringBuilder sb, 
			Map<String, TreeNode> nodes, Object emptyArray[]) throws Exception {
		Iterator iterator = ((Iterable) obj).iterator();
		byte strategy = NOT_JUDGED;
		sb.append(LEFT_ARRAY);
		if(iterator.hasNext()) {
			Object value = iterator.next();
			if(value != null) {
				strategy = judge(value.getClass());
				entityHandler(value, references, sb, nodes, emptyArray, strategy);
			}
			else {
				sb.append("null");
			}
		}
		while(iterator.hasNext()) {
			sb.append(COMMA);
			entityHandler(iterator.next(), references, sb, nodes, emptyArray, strategy);
		}
		sb.append(RIGHT_ARRAY);
	}
	
	private static void arrayHandler(Object obj, IdentityHashMap<Object, String> references, 
			StringBuilder sb, Map<String, TreeNode> nodes, Object emptyArray[]) throws Exception {
		Object[] array = (Object[]) obj;
		byte strategy = NOT_JUDGED;
		sb.append(LEFT_ARRAY);
		Object valueIteratorObj = array[0];
		if(valueIteratorObj != null) {
			strategy = judge(valueIteratorObj.getClass());
			entityHandler(valueIteratorObj, references, sb, nodes, emptyArray, strategy);
		}
		else {
			sb.append("null");
		}
		for(int i = 1, l = array.length; i < l; ++i) {
			sb.append(COMMA);
			entityHandler(array[i], references, sb, nodes, emptyArray, strategy);
		}
		sb.append(RIGHT_ARRAY);
	}
	
	/**
	 * @see 用于检查入口
	 * @param obj
	 * @param sb
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @param emptyArray
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static void collectionHandler(Object obj, StringBuilder sb, Object mainEntranceObj,
			String fieldPaths[], Object emptyArray[]) throws Exception {
		if(obj.equals(mainEntranceObj)) {  // 找到入口了使用
			sb.append(NewJsonUtil.toString(mainEntranceObj, fieldPaths));
			return;
		}
		Iterator valueIterator = ((Iterable) obj).iterator();
		byte strategy = NOT_JUDGED;
		sb.append(LEFT_ARRAY);
		if(valueIterator.hasNext()) {
			Object valueIteratorObj = valueIterator.next();
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				entityHandler(valueIteratorObj, sb, mainEntranceObj, fieldPaths, emptyArray, strategy);
			}
			else {
				sb.append("null");
			}
		}
		while(valueIterator.hasNext()) {
			sb.append(COMMA);
			entityHandler(valueIterator.next(), sb, mainEntranceObj, fieldPaths, emptyArray, strategy);
		}
		sb.append(RIGHT_ARRAY);
	}
	
	private static void arrayHandler(Object obj, StringBuilder sb, Object mainEntranceObj, 
			String fieldPaths[], Object emptyArray[]) throws Exception {
		if(obj.equals(mainEntranceObj)) {  // 找到入口了使用
			sb.append(NewJsonUtil.toString(mainEntranceObj, fieldPaths));
			return;
		}
		Object[] values = (Object[]) obj;
		byte strategy = NOT_JUDGED;
		sb.append(LEFT_ARRAY);
		if(values.length > 0) {
			Object valueIteratorObj = values[0];
			if(valueIteratorObj != null) {
				strategy = judge(valueIteratorObj.getClass());
				entityHandler(valueIteratorObj, sb, mainEntranceObj, fieldPaths, emptyArray, strategy);
			}
			else {
				sb.append("null");
			}
			for(int i = 1, l = values.length; i < l; ++i) {
				sb.append(COMMA);
				entityHandler(values[i], sb, mainEntranceObj, fieldPaths, emptyArray, strategy);
			}
		}
		sb.append(RIGHT_ARRAY);
	}
	
	/**
	 * @see 查找入口
	 * @param obj
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @return
	 */
	public static String toString(Object obj, Object mainEntranceObj, String fieldPaths[]) {
		if (obj == null) { return ""; }
		Object emptyArray[] = new Object[0];  // 用于getter方法
		Class<?> beanClass = obj.getClass();
		byte objStrategy = judge(beanClass);  // 根据数据类型判断应该使用什么策略
		StringBuilder jsonStr = new StringBuilder();

		try {
			if(objStrategy == ARRAY && !isEmptyArray(obj)) {
				arrayHandler(obj, jsonStr, mainEntranceObj, fieldPaths, emptyArray);
			}
			else if(objStrategy == COLLECTION && !isEmptyCollection(obj)) {  // 使用LIST和SET的策略
				collectionHandler(obj, jsonStr, mainEntranceObj, fieldPaths, emptyArray);
			}
			else if(objStrategy == ENTITY) {  // 使用实体类策略
				jsonStr.append(LEFT_OBJECT);
				
				BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
				PropertyDescriptor propDescs[] = beanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
				for (PropertyDescriptor propDesc : propDescs) {
					Method getter = getReadMethod(propDesc);
					if(!getter.isAccessible()) {
						getter.setAccessible(true);  // 使这个getter方法可以访问
					}
					
					Object value = getter.invoke(obj, emptyArray);
					if(value == null) { continue; }
//					if(propDesc.getName().equals("data")) {
//						System.err.println();
//					}
					byte propStrategy = judge(value.getClass());
					if (propStrategy == COLLECTION || propStrategy == ENTITY || propStrategy == ARRAY) {  // 在当前的深度下，找到该属性
						 if(propStrategy == ARRAY ) {
							 if(isEmptyArray(value)) { continue; }
							setPropertyName(jsonStr, propDesc);
//							StringBuilder newSb = new StringBuilder();
							arrayHandler(value, jsonStr, mainEntranceObj, fieldPaths, emptyArray);
//							jsonStr.append(newSb);
							jsonStr.append(COMMA);
						}
						else if(propStrategy == COLLECTION ) {
							if(isEmptyCollection(value)) { continue; }
							setPropertyName(jsonStr, propDesc);
//							StringBuilder newSb = new StringBuilder();
							collectionHandler(value, jsonStr, mainEntranceObj, fieldPaths, emptyArray);
//							jsonStr.append(newSb);
							jsonStr.append(COMMA);
						} 
						else {
							setPropertyName(jsonStr, propDesc);
//							StringBuilder newSb = new StringBuilder();
							entityHandler(value, jsonStr, mainEntranceObj, fieldPaths, emptyArray, propStrategy);
//							jsonStr.append(newSb);
							jsonStr.append(COMMA);
						}
					}
					else if(propStrategy != OTHER ) {  // &&  propStrategy != INTERFACE
						if(!isHibernateHandler(getter)) {
							setPropertyName(jsonStr, propDesc); 
							convertObject2String(value, jsonStr, propStrategy);
							jsonStr.append(COMMA);
						}
					}
				}
				deleteLastChar(jsonStr);
				jsonStr.append(RIGHT_OBJECT);
			}
		}catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return ex.getMessage();
		}
		return jsonStr.toString();
	}
	
	/**
	 * @see 针对入口对象进行序列化
	 * @param mainEntranceObj
	 * @param fieldPaths
	 * @return
	 */
	public static String toString(Object mainEntranceObj, String fieldPaths[]) {
		if (mainEntranceObj == null) { return ""; }
		Object emptyArray[] = new Object[0];  // 用于getter方法
		Class<?> beanClass = mainEntranceObj.getClass();
		byte objStrategy = judge(beanClass);  // 根据数据类型判断应该使用什么策略
		StringBuilder jsonStr = new StringBuilder();
		if (fieldPaths == null || fieldPaths.length == 0) {
			try {
				ignoreCollectionOrEntity(mainEntranceObj, jsonStr, emptyArray, objStrategy);
			}
			catch(Exception ex) {
				logger.error(ex.getMessage(), ex);
				ex.printStackTrace();
			}
			return jsonStr.toString(); 
		}
		
		TreePath tp = new TreePath(fieldPaths);  // 将所给的路径转化为一个树
//		System.err.println(JSON.toJSONString(tp));
		try {
			/**
			 * 只有实体和集合才会产生循环引用
			 * 
			 * 不做循环引用检查，因为
			 */
			IdentityHashMap<Object, String> references = new IdentityHashMap<Object, String>();
			references.put(mainEntranceObj, "");  // obj是根，是转化的入口
//			Set<Object> references = new HashSet<Object>();
//			references.add(mainEntranceObj);
			
			if(objStrategy == COLLECTION) {  // 使用LIST和SET的策略
				if(!isEmptyCollection(mainEntranceObj)) {
					collectionHandler(mainEntranceObj, references, jsonStr, tp.roots, emptyArray);
				}
			}
			else if(objStrategy == ENTITY) {  // 使用实体类策略
				jsonStr.append(LEFT_OBJECT);
				BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
				PropertyDescriptor propDescs[] = beanInfo.getPropertyDescriptors();  // 得到实体类的Bean信息
				for (PropertyDescriptor propDesc : propDescs) {
					Method getter = getReadMethod(propDesc);
					if(!getter.isAccessible()) {
						getter.setAccessible(true);  // 使这个getter方法可以访问
					}
					Object value = getter.invoke(mainEntranceObj, emptyArray);
					if(value == null) { continue; }
//					if(propDesc.getName().equals("addUser")) {
//						System.err.println();
//					}
					byte propStrategy = judge(value.getClass());
					if (propStrategy == COLLECTION || propStrategy == ENTITY || propStrategy == ARRAY) {  // 在当前的深度下，找到该属性
						TreeNode node = tp.roots.get(propDesc.getName());
						if(node == null) { continue; }  // 不是想要的实体或者集合
//						if(references.containsKey(value)) {
//							continue;  // 检测到循环引用，则跳过该属性
//						}
//						else {
//							references.put(value, propDesc.getName());
//						}
						
						if(node.isLeaf()) {  // 到达最后
							if(propStrategy == COLLECTION ) {
								if(!isEmptyCollection(value)) {
									setPropertyName(jsonStr, propDesc);
									convertCollection2String(value, jsonStr, emptyArray);
									jsonStr.append(COMMA);
								}
							}
							else if(propStrategy == ARRAY) {
								if(!isEmptyArray(value)) {
									setPropertyName(jsonStr, propDesc);
									convertArray2String(value, jsonStr, emptyArray);
									jsonStr.append(COMMA);
								}
							}
							else {
								setPropertyName(jsonStr, propDesc);
								convertEntity2String(value, jsonStr, emptyArray);
								jsonStr.append(COMMA);
							}
						}
						else {
							if(propStrategy == COLLECTION ) {
								if(isEmptyCollection(value)) { continue; }
								setPropertyName(jsonStr, propDesc);
//								StringBuilder newSb = new StringBuilder();
								collectionHandler(value, references, jsonStr, node.children, emptyArray);
//								jsonStr.append(newSb);
								jsonStr.append(COMMA);
							}
							else if(propStrategy == ARRAY) {
								if(isEmptyArray(value)) { continue; }
								setPropertyName(jsonStr, propDesc);
//								StringBuilder newSb = new StringBuilder();
								arrayHandler(value, references, jsonStr, node.children, emptyArray);
//								jsonStr.append(newSb);
								jsonStr.append(COMMA);
							}
							else {
								setPropertyName(jsonStr, propDesc);
//								StringBuilder newSb = new StringBuilder();
								entityHandler(value, references, jsonStr, node.children, emptyArray, propStrategy);
//								jsonStr.append(newSb);
								jsonStr.append(COMMA);
							}
						}
					}
					else if(propStrategy != OTHER ) {
						if((!isHibernateHandler(getter))) {
							setPropertyName(jsonStr, propDesc);
							convertObject2String(value, jsonStr, propStrategy);
							jsonStr.append(COMMA);
						}
					}
				}
				deleteLastChar(jsonStr);
				jsonStr.append(RIGHT_OBJECT);
			}
		}catch(Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return ex.getMessage();
		}
		return jsonStr.toString();
	}
}
