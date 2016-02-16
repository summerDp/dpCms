/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.summer.dp.cms.support.persistence;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.google.common.collect.Maps;

public class SearchFilter {

	private static final Logger logger = LoggerFactory.getLogger(SearchFilter.class);

	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("yyyy-MM-dd");

	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		// 使ConvertUtils支持java.util.Date的转换
		ConvertUtils.register(new DateLocaleConverter(), Date.class);
	}

	public enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE, OREQ, ORLIKE, ANDLIKE , NOTEQ, ANDNOTEQ, NOTLIKE, ANDNOTLIKE,
		NULL, NOTNULL, IN, QUERY
		//OREQ, ORLIKE, ANDLIKE 后面必须带的是String
	}

	/** 属性数据类型. */
	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class), E(Enum.class), M(BigDecimal.class);

		private Class<?> clazz;

		private PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为LIKES_name
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();
		String filedName;
		Operator operator;
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();

			Object value = entry.getValue();
			if (key == null || value == null || StringUtils.isBlank(value.toString())) {
				continue;
			}
			// http:\\192.168.1.1:8080\bigdata\tt.do?db_sum_alias=carCount,busCount&db_query=a=
			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2 && names.length != 3) {
				// throw new IllegalArgumentException(key + " is not a valid search filter name");
				logger.warn(key + " is not a valid search filter name");
				continue;
			}

			Class<?> propertyClass = null;
			if (names.length == 3) {
				try {
					propertyClass = Enum.valueOf(PropertyType.class, names[2]).getValue();
					logger.debug(key + ":" + propertyClass.getName());
					if (propertyClass != null) {
						if (propertyClass.getName().equals("java.util.Date")) {
							String str = value.toString();
							if (str.length() == 10) {
								if (names[0].equals("GT") || names[0].equals("GTE")) {
									str += " 00:00:00";
								}
								else if (names[0].equals("LT") || names[0].equals("LTE")) {
									str += " 23:59:59";
								}
							}
							value = dateTimeFormat.parseObject(str);
						} else {
							value = ConvertUtils.convert(value);
						}
					}

				} catch (RuntimeException e) {
					logger.warn(key + " PropertyType is not a valid type!", e);
				} catch (ParseException e) {
					logger.warn(key + " PropertyType is not a valid type!", e);
				}
			}

			filedName = names[1];
			operator = Operator.valueOf(names[0]);
			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}

	/**
	 * @author 何高才
	 * searchParams中key的格式为LIKES_name
	 * 
	 * @see 属性[操作符]=值
	 * @see 属性.属性[操作符]=值
	 * 
	 * http://192.168.1.92:8080/yhwlsys_base/jobs/find.do?Employees.employeeId_eq=15
	 *  http://192.168.1.92:8080/yhwlsys_base/jobs/find.do?Employees.employeeId=15
	 *  http://192.168.1.92:8080/yhwlsys_base/jobs/find.do?Employees.name_like=123
	 */
	public static Map<String, SearchFilter> parse2Filter(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = Maps.newHashMap();
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			// 过滤掉空值
			if (value != null && StringUtils.isBlank(value.toString())) { continue; }
			
			// count和page都是用于分页的，建议前端表单提交不要使用这两个单词
			if("count".equals(key) || "page".equals(key) || "_".equals(key)) { continue; }
			
			// 这个orderParam用于由前端传入排序的值
			if(key.startsWith("orderParams")) { continue; }
			// 这个rjdParam用于由前端传入减少Json数据量的值
			if(key.startsWith("rjdParams")) { continue; }
			
			// rjd用于由前端传入减少Json数据量的值
			if(key.equals("rjd")) { continue; }
			
			String lowerCase = key.toLowerCase();
			if(lowerCase.equalsIgnoreCase("db_query")) {
				/*String strs[] = value.toString().split("@|\\|");
				String fields[];
				Map<String, Object> _searchParams = new HashMap<String, Object>(strs.length);
				for(String str : strs) {
					fields = StringUtil.split(str, '=');
					if(fields != null && fields.length == 2) {
						_searchParams.put(fields[0], fields[1]);
					}
				}
				filters.putAll(parse2Filter(_searchParams));*/
				SearchFilter filter = new SearchFilter(key, Operator.QUERY, value);
				filters.put(key, filter);
				continue;
			}
			
			String filedName = null;
			String oper = null;
			int leftLast = key.lastIndexOf('_');
			if(leftLast > -1) {  // 如果存在
				// 拆分operator与filedAttribute
				filedName = key.substring(0, leftLast);
				oper = key.substring(leftLast + 1).toUpperCase();
			}
			else {
				filedName = key;
				oper = "EQ";
			}

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(
					// 说明是一个集合类
					filedName.indexOf('.') > -1? "[join]" + filedName : filedName, 
							Operator.valueOf( oper ), 
							value);
			filters.put(key, filter);
		}

		return filters;
	}
	
	
	/**
	 * @see Page<BJob> jobs = SearchFilter.query("jName[like]=sma&jId=3&BEmployees.employeeId=2", pageable, BJob.class, jobsRepository);
	 * @param query  -->  "jName[like]=sma&jId=3&BEmployees.employeeId=2"
	 * @param pageable --> PageRequest pageable;
	 * @param entity --> BJob.class
	 * @param repository --> jobsRepository 继承JpaSpecificationExecutor
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Page query(String query, PageRequest pageable , Class<?> entity, JpaSpecificationExecutor repository) {
		String strs[] = StringUtils.split(query, '&');
		if(strs == null || strs.length == 0) {
			throw new IllegalArgumentException("query 不能为null或者空字符串");
		}
		else {
			Map<String, Object> searchParams = new HashMap<String, Object>(strs.length);  // TreeMap不用排序
			for(String str : strs) {
				String keyValue[] = StringUtils.split(str, '=');
				if(keyValue.length != 2) {
					throw new IllegalArgumentException("\"" + query + "\"包含不正确的键值对");
				}
				searchParams.put(StringUtils.trim(keyValue[0]), keyValue[1]);
			}
			Specification spec = DynamicSpecifications.bySearchFilter(SearchFilter.parse2Filter(searchParams).values(), entity);
			return repository.findAll(spec, pageable);
		}
	}
	
	/**
	 * @see List<BJob> jobs = SearchFilter.query("jName[like]=sma&jId=3&BEmployees.employeeId=2", BJob.class, jobsRepository);
	 * @param query  -->  "jName[like]=sma&jId=3&BEmployees.employeeId=2"
	 * @param entity --> BJob.class
	 * @param repository --> jobsRepository 继承JpaSpecificationExecutor
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List query(String query, Class<?> entity, JpaSpecificationExecutor repository) {
		String strs[] = StringUtils.split(query, '&');
		if(strs == null || strs.length == 0) {
			throw new IllegalArgumentException("query不能为null或者空字符串");
		}
		else {
			Map<String, Object> searchParams = new HashMap<String, Object>(strs.length);  // TreeMap不用排序
			for(String str : strs) {
				String keyValue[] = StringUtils.split(str, '=');
				if(keyValue.length != 2) {
					throw new IllegalArgumentException("\"" + query + "\"包含不正确的键值对");
				}
				searchParams.put(StringUtils.trim(keyValue[0]), keyValue[1]);
			}
			Specification spec = DynamicSpecifications.bySearchFilter(SearchFilter.parse2Filter(searchParams).values(), entity);
			return repository.findAll(spec);
		}
	}
	
}
