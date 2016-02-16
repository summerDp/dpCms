package org.summer.dp.cms.helper.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.FieldInfo;

public class MyNumberFieldSerializer  extends MyFieldSerializer {

    public MyNumberFieldSerializer(FieldInfo fieldInfo){
        super(fieldInfo);
    }

    public void writeProperty(JSONSerializer serializer, Object propertyValue) throws Exception {
        writePrefix(serializer);
        this.writeValue(serializer, propertyValue);
    }

    @Override
    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        SerializeWriter out = serializer.getWriter();

        Object value = propertyValue;

        if (value == null) {
            if (out.isEnabled(SerializerFeature.WriteNullNumberAsZero)) {
                out.write('0');
            } else {
                out.writeNull();
            }
            return;
        }

        out.append(value.toString());
    }
}
