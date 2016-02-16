/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.summer.dp.cms.support.persistence;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.summer.dp.cms.helper.Constants;
import org.summer.dp.cms.helper.collection.Collections3;
import org.summer.dp.cms.support.persistence.SearchFilter.Operator;

import com.google.common.collect.Lists;

import freemarker.template.utility.StringUtil;




public class DynamicSpecifications {
	public static final Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);
	
	/**
	 * @see 通过HASH值判断是否日期类型
	 * @see 前端传过来的参数必然是byte,short,int,float,double, String, Byte, Short, Integer, Float, Double,Date 这些类型的hash值都是互不相同的
	 * @param type java.util.Date
	 * @return
	 */
	public static boolean isDateType(Class<?> clazz) {
		return clazz == Date.class;
	}
	
	public static Date parseDate(Object source, Operator oper) throws ParseException {
		String str = source.toString();
		// 2010-08-09
		if(str.length() == 10) {
			switch(oper) {
			case LT:
			case LTE:
				str += " 23:59:59";
				break;
			default:
				str += " 00:00:00";
				break;
			}
		}
		return Constants.DF.parse(str);
	}
	
	/**
	 * @see 动态生成JPA的规范
	 * @param filters
	 * @param entityClazz
	 * @return
	 */
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz) {
		return bySearchFilter(filters, entityClazz, false, null);
	}
	
	/**
	 * @see 动态生成JPA的规范
	 * @param filters
	 * @param entityClazz
	 * @param isDistinct  true表示本次SQL将会加上distinct，false表示不加
	 * @return
	 */
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> entityClazz, final boolean isDistinct, 
			final List<OrderParam> orderParams) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if(orderParams != null && orderParams.size() > 0) {
					/*
					CriteriaQuery<Foo> criteriaQuery = criteriaBuilder.createQuery(Foo.class);
					Root<Foo> from = criteriaQuery.from(Foo.class);
					CriteriaQuery<Foo> select = criteriaQuery.select(from);
					criteriaQuery.orderBy(criteriaBuilder.asc(from.get("name")));
					*/
					List<Order> orders = new ArrayList<Order>(orderParams.size());
					for(OrderParam orderParam : orderParams) {
						if(orderParam != null && orderParam.getField() != null) {
							String fields[] = StringUtil.split(orderParam.getField(), '.');
							Path expression = (fields.length > 1)? root.join(fields[0]) : root.get(fields[0]);
							for(int i = 1, len = fields.length; i < len; ++i) {
								expression = expression.get(fields[i]);
							}
							if(expression != null) {
								Order order = (orderParam.getType() == null || orderParam.getType().equalsIgnoreCase("asc"))? 
										builder.asc(expression) : builder.desc(expression);
								orders.add(order);
//								query.orderBy(order);
							}
						}
					}
					query.orderBy(orders);
				}
				query.distinct(isDistinct);
				if (Collections3.isNotEmpty(filters)) {
					List<Predicate> predicates = Lists.newArrayList();
					for (SearchFilter filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, '.');
						Path expression = null;
						
						//////  修正路径不正确下的情形
						boolean hasJoin = names[0].startsWith("[join]");
						String fn = hasJoin? names[0].substring(6) : names[0];
						boolean isNotDateType = !(filter.value instanceof Date);
						try {
							expression = hasJoin? root.join(fn) : root.get(fn);
							if(isNotDateType && isDateType(expression.getJavaType())) {
								// filter.value不可能有
								filter.value = parseDate(filter.value.toString(), filter.operator);
							}
						}catch(Exception e) {
//							logger.error(e.getMessage(), e);
							continue;  // 抛异常的话就抛弃该表达式
						}
						boolean isPropertyNotValid = false;
						for (int i = 1; i < names.length; i++) {
							try {
								expression = expression.get(names[i]);
								if(isNotDateType && isDateType(expression.getJavaType())) {
									filter.value = parseDate(filter.value.toString(), filter.operator);
								}
							} catch(Exception e) {
//								logger.error(e.getMessage(), e);
								isPropertyNotValid = true;  // 该属性的确无效
								break;  // 抛异常的话就抛弃该表达式
							}
						}
						if(expression == null || isPropertyNotValid) { continue; }
						///////
						
						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case ORLIKE:
							if(filter.value instanceof String){
								String value = (String) filter.value;
								String[] values = value.split(",");
								Predicate[] like = new Predicate[values.length];
								for(int i=0;i< values.length;i++){
									like[i]=builder.like(expression, "%" + values[i] + "%");
								}
								predicates.add(builder.or(like));
							}
							
							break;
						case ANDLIKE:
							if(filter.value instanceof String){
								String value = (String) filter.value;
								String[] values = value.split(",");
								Predicate[] like = new Predicate[values.length];
								for(int i=0;i< values.length;i++){
									like[i]=builder.like(expression, "%" + values[i] + "%");
								}
								predicates.add(builder.and(like));
							}
							break;
						case ANDNOTLIKE:
							if(filter.value instanceof String){
								String value = (String) filter.value;
								String[] values = value.split(",");
								Predicate[] like = new Predicate[values.length];
								for(int i=0;i< values.length;i++){
									like[i]=builder.notLike(expression, "%" + values[i] + "%");
								}
								predicates.add(builder.and(like));
							}
							break;
						case OREQ:
							if(filter.value instanceof String){
								String value = (String) filter.value;
								String[] values = value.split(",");
								Predicate[] like = new Predicate[values.length];
								for(int i=0;i< values.length;i++){
									like[i]=builder.equal(expression, values[i]);
								}
								predicates.add(builder.or(like));
							}
							break;
							
							
						case ANDNOTEQ:
							if(filter.value instanceof String){
								String value = (String) filter.value;
								String[] values = value.split(",");
								Predicate[] like = new Predicate[values.length];
								for(int i=0;i< values.length;i++){
									like[i]=builder.notEqual(expression, values[i]);
								}
								predicates.add(builder.and(like));
							}
							break;
						case NOTEQ :
							predicates.add(builder.notEqual(expression, (Comparable) filter.value));
							break;
						case NOTLIKE :
							predicates.add(builder.notLike(expression, "%" + filter.value + "%"));
							break;
						case NULL:
							predicates.add(builder.isNull(expression));
							break;
						case NOTNULL:
							predicates.add(builder.isNotNull(expression));
							break;
//						case IN:
//							predicates.add(builder.in(expression).in(values));
//							break;
						}
					}

					// 将所有条件用 and 联合起来
					if (!predicates.isEmpty()) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}
}
