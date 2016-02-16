package org.summer.dp.cms.helper.json;

import java.util.Collection;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;

public class MyObjectFieldSerializer extends MyFieldSerializer {
	private String                format;
    boolean                       writeNumberAsZero       = false;
    boolean                       writeNullStringAsEmpty  = false;
    boolean                       writeNullBooleanAsFalse = false;
    boolean                       writeNullListAsEmpty    = false;
    boolean                       writeEnumUsingToString  = false;
    boolean                       writeEnumUsingName      = false;

    private RuntimeSerializerInfo runtimeInfo;

    public MyObjectFieldSerializer(FieldInfo fieldInfo){
        super(fieldInfo);

        JSONField annotation = fieldInfo.getAnnotation(JSONField.class);

        if (annotation != null) {
            format = annotation.format();

            if (format.trim().length() == 0) {
                format = null;
            }

            for (SerializerFeature feature : annotation.serialzeFeatures()) {
                if (feature == SerializerFeature.WriteNullNumberAsZero) {
                    writeNumberAsZero = true;
                } else if (feature == SerializerFeature.WriteNullStringAsEmpty) {
                    writeNullStringAsEmpty = true;
                } else if (feature == SerializerFeature.WriteNullBooleanAsFalse) {
                    writeNullBooleanAsFalse = true;
                } else if (feature == SerializerFeature.WriteNullListAsEmpty) {
                    writeNullListAsEmpty = true;
                } else if (feature == SerializerFeature.WriteEnumUsingToString) {
                    writeEnumUsingToString = true;
                }else if(feature == SerializerFeature.WriteEnumUsingName){
                    writeEnumUsingName = true;
                }
            }
        }
    }

    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        writePrefix(serializer);
        writeValue(serializer, propertyValue);
    }

    @Override
    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (format != null) {
            serializer.writeWithFormat(propertyValue, format);
            return;
        }

        if (runtimeInfo == null) {

            Class<?> runtimeFieldClass;
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.getFieldClass();
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }

            ObjectSerializer fieldSerializer = serializer.getObjectWriter(runtimeFieldClass);
            runtimeInfo = new RuntimeSerializerInfo(fieldSerializer, runtimeFieldClass);
        }
        
        final RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        
        final int fieldFeatures = fieldInfo.getSerialzeFeatures();

        if (propertyValue == null) {
            if (writeNumberAsZero && Number.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write('0');
                return;
            } else if (writeNullStringAsEmpty && String.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write("\"\"");
                return;
            } else if (writeNullBooleanAsFalse && Boolean.class == runtimeInfo.runtimeFieldClass) {
                serializer.getWriter().write("false");
                return;
            } else if (writeNullListAsEmpty && Collection.class.isAssignableFrom(runtimeInfo.runtimeFieldClass)) {
                serializer.getWriter().write("[]");
                return;
            }

            runtimeInfo.fieldSerializer.write(serializer, null, fieldInfo.getName(), null, fieldFeatures);
            return;
        }

        if(runtimeInfo.runtimeFieldClass.isEnum()){
            if(writeEnumUsingName){
                serializer.getWriter().writeString(((Enum<?>) propertyValue).name());
                return;
            }
            if(writeEnumUsingToString){
                serializer.getWriter().writeString(((Enum<?>) propertyValue).toString());
                return;
            }
        }

        Class<?> valueClass = propertyValue.getClass();
        if (valueClass == runtimeInfo.runtimeFieldClass) {
            runtimeInfo.fieldSerializer.write(serializer, propertyValue, fieldInfo.getName(), fieldInfo.getFieldType(), fieldFeatures);
            return;
        }

        ObjectSerializer valueSerializer = serializer.getObjectWriter(valueClass);
        valueSerializer.write(serializer, propertyValue, fieldInfo.getName(), fieldInfo.getFieldType(), fieldFeatures);
    }

    static class RuntimeSerializerInfo {

        ObjectSerializer fieldSerializer;

        Class<?>         runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass){
            super();
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
        
        

    }
}
