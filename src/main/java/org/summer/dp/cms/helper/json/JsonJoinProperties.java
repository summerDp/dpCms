package org.summer.dp.cms.helper.json;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 关联实体对象显示的Json属性
 * 
 * @author deli
 * 
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface JsonJoinProperties {

	/**
	 * Names of properties to join.
	 */
	public String[] value() default {};

}
