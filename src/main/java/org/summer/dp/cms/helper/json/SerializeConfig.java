package org.summer.dp.cms.helper.json;

import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.serializer.AppendableSerializer;
import com.alibaba.fastjson.serializer.ArraySerializer;
import com.alibaba.fastjson.serializer.AutowiredObjectSerializer;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharsetCodec;
import com.alibaba.fastjson.serializer.ClobSeriliazer;
import com.alibaba.fastjson.serializer.DateSerializer;
import com.alibaba.fastjson.serializer.EnumSerializer;
import com.alibaba.fastjson.serializer.EnumerationSeriliazer;
import com.alibaba.fastjson.serializer.ExceptionSerializer;
import com.alibaba.fastjson.serializer.JSONAwareSerializer;
import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializableSerializer;
import com.alibaba.fastjson.serializer.JSONStreamAwareSerializer;
import com.alibaba.fastjson.serializer.MapSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
/*
 * Copyright 1999-2101 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.alibaba.fastjson.serializer.TimeZoneCodec;
import com.alibaba.fastjson.util.ServiceLoader;

/**
 * circular references detect
 * 
 * @author wenshao[szujobs@hotmail.com]
 */

/**
 
 obj = {
 "name": "String",
 "age": 34,
 "family": {
 	"dad": {
 		name: "dad",
 		age: 56
 	},
 	"mom": {
 		name: "mom",
 		age: 53
 	}
 }
 }
 */
public class SerializeConfig extends com.alibaba.fastjson.serializer.SerializeConfig {
	
	public SerializeConfig() {
		super();
	}

	public ObjectSerializer createJavaBeanSerializer(Class<?> clazz) {
		if (!Modifier.isPublic(clazz.getModifiers())) {
			MyJavaBeanSerializer bean = new MyJavaBeanSerializer(clazz);
			return bean;
		}
		return new MyJavaBeanSerializer(clazz);
	}

	public ObjectSerializer getObjectWriter(Class<?> clazz) {
		ObjectSerializer writer = get(clazz);

		if (writer == null) {
			try {
				final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
				for (Object o : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {
					if (!(o instanceof AutowiredObjectSerializer)) {
						continue;
					}
					AutowiredObjectSerializer autowired = (AutowiredObjectSerializer) o;
					for (Type forType : autowired.getAutowiredFor()) {
						put(forType, autowired);
					}
				}
			} catch (ClassCastException ex) {
				// skip
			}
			writer = get(clazz);
		}

		if (writer == null) {
			final ClassLoader classLoader = JSON.class.getClassLoader();
			if (classLoader != Thread.currentThread().getContextClassLoader()) {
				try {
					for (Object o : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {

						if (!(o instanceof AutowiredObjectSerializer)) {
							continue;
						}

						AutowiredObjectSerializer autowired = (AutowiredObjectSerializer) o;
						for (Type forType : autowired.getAutowiredFor()) {
							put(forType, autowired);
						}
					}
				} catch (ClassCastException ex) {
					// skip
				}

				writer = get(clazz);
			}
		}

		if (writer == null) {
			if (Map.class.isAssignableFrom(clazz)) {
				put(clazz, MapSerializer.instance);
			} 
			else if (List.class.isAssignableFrom(clazz)) {
				put(clazz, MyListSerializer.instance);
			} 
			else if (Collection.class.isAssignableFrom(clazz)) {
				put(clazz, MyCollectionSerializer.instance);
			} 
			else if (Date.class.isAssignableFrom(clazz)) {
				put(clazz, DateSerializer.instance);
			} 
			else if (JSONAware.class.isAssignableFrom(clazz)) {
				put(clazz, JSONAwareSerializer.instance);
			} 
			else if (JSONSerializable.class.isAssignableFrom(clazz)) {
				put(clazz, JSONSerializableSerializer.instance);
			} 
			else if (JSONStreamAware.class.isAssignableFrom(clazz)) {
				put(clazz, JSONStreamAwareSerializer.instance);
			} 
			else if (clazz.isEnum() || (clazz.getSuperclass() != null && clazz.getSuperclass().isEnum())) {
				put(clazz, EnumSerializer.instance);
			} 
			else if (clazz.isArray()) {
				Class<?> componentType = clazz.getComponentType();
				ObjectSerializer compObjectSerializer = getObjectWriter(componentType);
				put(clazz, new ArraySerializer(componentType, compObjectSerializer));
			} 
			else if (Throwable.class.isAssignableFrom(clazz)) {
				put(clazz, new ExceptionSerializer(clazz));
			} 
			else if (TimeZone.class.isAssignableFrom(clazz)) {
				put(clazz, TimeZoneCodec.instance);
			}
			else if (Appendable.class.isAssignableFrom(clazz)) {
				put(clazz, AppendableSerializer.instance);
			} 
			else if (Charset.class.isAssignableFrom(clazz)) {
				put(clazz, CharsetCodec.instance);
			} 
			else if (Enumeration.class.isAssignableFrom(clazz)) {
				put(clazz, EnumerationSeriliazer.instance);
			} 
			else if (Calendar.class.isAssignableFrom(clazz)) {
				put(clazz, CalendarCodec.instance);
			} 
			else if (Clob.class.isAssignableFrom(clazz)) {
				put(clazz, ClobSeriliazer.instance);
			} 
			else {
				boolean isCglibProxy = false;
				boolean isJavassistProxy = false;
				for (Class<?> item : clazz.getInterfaces()) {
					if (item.getName().equals("net.sf.cglib.proxy.Factory") || item.getName().equals("org.springframework.cglib.proxy.Factory")) {
						isCglibProxy = true;
						break;
					} 
					else if (item.getName().equals("javassist.util.proxy.ProxyObject")) {
						isJavassistProxy = true;
						break;
					}
				}

				if (isCglibProxy || isJavassistProxy) {
					Class<?> superClazz = clazz.getSuperclass();

					ObjectSerializer superWriter = getObjectWriter(superClazz);
					put(clazz, superWriter);
					
					return superWriter;
				}

				if (Proxy.isProxyClass(clazz)) {
					put(clazz, createJavaBeanSerializer(clazz));
				} 
				else {
					put(clazz, createJavaBeanSerializer(clazz));
				}
				
			}

			writer = get(clazz);
		}
		return writer;
	}
}
