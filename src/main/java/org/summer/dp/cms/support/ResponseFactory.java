package org.summer.dp.cms.support;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * @ClassName: ResponseFactory
 * @Description: 返回对象工厂
 * @author 赵宝东
 * @date 2015年3月20日 下午3:49:21
 * 
 */
public class ResponseFactory {

	private static Validator validator;

	public static Response getResponse() {
		Response response = new Response();
		response.setStateCode(StateCode.OK);
		response.setMessage("SUCCESS");
		return response;
	}

	/**
	 * 对象验证
	 * 
	 * @param object
	 * @return
	 */
	public static <T> Response getValidatorResponse(T object) {
		initValidator();
		Set<ConstraintViolation<T>> cvs = validator.validate(object);
		if (cvs == null || cvs.size() == 0) {
			return getResponse();
		}
		Response response = new Response();
		for (ConstraintViolation<T> cv : cvs) {
			response.addValidError(cv.getPropertyPath().toString(),
					cv.getMessage());
		}
		return response;
	}

	private static void initValidator() {
		if (validator == null) {
			WebApplicationContext wac = ContextLoader
					.getCurrentWebApplicationContext();
			ValidatorFactory validatorFactory = (ValidatorFactory) wac
					.getBean("validator");
			validator = validatorFactory.getValidator();
		}
	}
}
