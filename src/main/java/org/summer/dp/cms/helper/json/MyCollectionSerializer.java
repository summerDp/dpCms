package org.summer.dp.cms.helper.json;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerialContext;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MyCollectionSerializer implements ObjectSerializer {
	public final static MyCollectionSerializer instance = new MyCollectionSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            if (out.isEnabled(SerializerFeature.WriteNullListAsEmpty)) {
                out.write("[]");
            } 
            else {
                out.writeNull();
            }
            return;
        }

        Type elementType = null;
        if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType param = (ParameterizedType) fieldType;
                elementType = param.getActualTypeArguments()[0];
            }
        }

        Collection<?> collection = (Collection<?>) object;

        SerialContext context = serializer.getContext();
        serializer.setContext(context, object, fieldName, 0);

        if (serializer.isEnabled(SerializerFeature.WriteClassName)) {
            if (HashSet.class == collection.getClass()) {
                out.append("Set");
            } 
            else if (TreeSet.class == collection.getClass()) {
                out.append("TreeSet");
            }
        }

        try {
            int i = 0;
            out.append('[');
            
            for (Object item : collection) {

                if (i++ != 0) {
                    out.append(',');
                }

                if (item == null) {
                    out.writeNull();
                    continue;
                }

                Class<?> clazz = item.getClass();

                if (clazz == Integer.class) {
                    out.writeInt(((Integer) item).intValue());
                    continue;
                }

                if (clazz == Long.class) {
                    out.writeLong(((Long) item).longValue());

                    if (out.isEnabled(SerializerFeature.WriteClassName)) {
                        out.write('L');
                    }
                    continue;
                }

                ObjectSerializer itemSerializer = serializer.getObjectWriter(clazz);
                itemSerializer.write(serializer, item, i - 1, elementType, 0);
            }
            out.append(']');
        } 
        finally {
            serializer.setContext(context);
        }
    }
}
