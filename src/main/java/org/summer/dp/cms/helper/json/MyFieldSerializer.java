package org.summer.dp.cms.helper.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;

public abstract class MyFieldSerializer {
	protected final FieldInfo fieldInfo;
    private final String      double_quoted_fieldPrefix;
    private final String      single_quoted_fieldPrefix;
    private final String      un_quoted_fieldPrefix;
    private boolean           writeNull = false;

    public MyFieldSerializer(FieldInfo fieldInfo){
        
        this.fieldInfo = fieldInfo;
        fieldInfo.setAccessible(true);

        this.double_quoted_fieldPrefix = '"' + fieldInfo.getName() + "\":";

        this.single_quoted_fieldPrefix = '\'' + fieldInfo.getName() + "\':";

        this.un_quoted_fieldPrefix = fieldInfo.getName() + ":";

        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);
        if (annotation != null) {
            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteMapNullValue) {
                    writeNull = true;
                }
            }
        }
    }

    public boolean isWriteNull() {
        return writeNull;
    }

    public Field getField() {
        return fieldInfo.getField();
    }

    public String getName() {
        return fieldInfo.getName();
    }

    public Method getMethod() {
        return fieldInfo.getMethod();
    }
    
    public String getLabel() {
        return fieldInfo.getLabel();
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.getWriter();

        if (serializer.isEnabled(SerializerFeature.QuoteFieldNames)) {
            if (serializer.isEnabled(SerializerFeature.UseSingleQuotes)) {
                out.write(single_quoted_fieldPrefix);
            } else {
                out.write(double_quoted_fieldPrefix);
            }
        } else {
            out.write(un_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValue(Object object) throws Exception {
        try {
            return fieldInfo.get(object);
        } catch (Exception ex) {
            throw new JSONException("get property error。 " + fieldInfo.gerQualifiedName(), ex);
        }
    }

    public abstract void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception;

    public abstract void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception;
}
