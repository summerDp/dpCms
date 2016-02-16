package org.summer.dp.cms.helper.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author wenshao[szujobs@hotmail.com]
 */
public class MyJavaBeanSerializer extends JavaBeanSerializer {

	public MyJavaBeanSerializer(Class<?> clazz) {
		super(clazz);
	}

	@Override
	public boolean writeReference(JSONSerializer serializer, Object object,
			int fieldFeatures) {

		SerialContext context = serializer.getContext();
		{

			if (context != null
					&& SerializerFeature.isEnabled(context.getFeatures(),
							fieldFeatures,
							SerializerFeature.DisableCircularReferenceDetect)) {
				return false;
			}
		}

		if (!serializer.containsReference(object)) {
			return false;
		}

		SerialContext parentCtx = context;
		while ((parentCtx = parentCtx.getParent()) != null) {
			Object parentObj = parentCtx.getObject();
			if (object.equals(parentObj)) {
				serializer.writeNull();
				return true;
			}
		}
		return false;
	}

}
