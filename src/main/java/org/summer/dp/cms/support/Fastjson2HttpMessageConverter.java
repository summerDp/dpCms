package org.summer.dp.cms.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.summer.dp.cms.helper.json.JsonData;
import org.summer.dp.cms.helper.json.JsonUtil;
import org.summer.dp.cms.helper.json.NewJsonUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class Fastjson2HttpMessageConverter extends AbstractHttpMessageConverter<Object> {
	public final static Charset UTF8  = Charset.forName("UTF-8");

    private Charset charset  = UTF8;

    private SerializerFeature[] features = new SerializerFeature[0];

    public Fastjson2HttpMessageConverter(){//SerializerFeature.QuoteFieldNames
        super(new MediaType("application", "json", UTF8), new MediaType("application", "*+json", UTF8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public SerializerFeature[] getFeatures() {
        return features;
    }

    public void setFeatures(SerializerFeature... features) {
        this.features = features;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,  HttpMessageNotReadableException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = inputMessage.getBody();
        byte[] buf = new byte[1024];
        for (;;) {
            int len = in.read(buf);
            if (len == -1) {
                break;
            }
            if (len > 0) {
                baos.write(buf, 0, len);
            }
        }
        byte[] bytes = baos.toByteArray();
        return JSON.parseObject(bytes, 0, bytes.length, charset.newDecoder(), clazz);
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        String text;
        if(obj instanceof Response) {
        	Response resp = (Response) obj;
        	if(resp.getStateCode() == StateCode.VALID_FAIL){
        		//验证错误信息直接输出JSON
        		text = JsonUtil.toJSONStringWithDateFormat(obj, JsonUtil.DATE_FORMAT, features);
        	}else{
	        	String paths[] = resp.getFieldPaths();
	        	String[] strs = null;
	        	resp.setFieldPaths(strs);
	        	boolean bool = paths != null && paths.length > 0;
	        	Object data = resp.getData();
	        	if(data instanceof Page) {
	        		if(bool) {
		        		String prefix = "data.content.";
		        		for(int i = 0, l = paths.length; i < l; ++i) {
		        			paths[i] = prefix + paths[i];
		        		}
	        		}
	        		else {
	        			
	        			paths = new String[]{"data.content"};
	        		}
	        	}
	        	else {
	        		if(bool) {
		        		String prefix = "data.";
		        		for(int i = 0, l = paths.length; i < l; ++i) {
		        			paths[i] = prefix + paths[i];
		        		}
	        		}
	        		else {
	        			paths = new String[]{"data"};
	        		}
	        	}
	        	boolean isPattern = resp.getDatePattern() != null;
	         	if(isPattern) { NewJsonUtil.SDF.applyPattern(resp.getDatePattern()); }
	        	text = NewJsonUtil.toString(resp, paths);
	        	if(isPattern) { NewJsonUtil.SDF.applyPattern(NewJsonUtil.DATE_FORMAT); }
	        }
        }
        else if(obj instanceof JsonData) {
	        JsonData jsonData = (JsonData) obj;
	        text = NewJsonUtil.toString(jsonData.getResponse(), 
	        		jsonData.getMainEntranceObj(), jsonData.getFieldPaths());
        }
        else {
        	text = JsonUtil.toJSONStringWithDateFormat(obj, JsonUtil.DATE_FORMAT, features);
        }
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }
    

    public static Fastjson2HttpMessageConverter makeFastjson2HttpMessageConverter(){
    	/*<bean class="com.yhwl.web.converter.Fastjson2HttpMessageConverter">
		<property name="supportedMediaTypes">
			<list>
				<value>application/json;charset=UTF-8</value>
				<value>text/html;charset=UTF-8</value><!-- 避免IE出现下载JSON文件的情况 -->
			</list>
		</property>
		<property name="features">
			<list>
				<!-- 
				<value>WriteMapNullValue</value>
				<value>WriteNullStringAsEmpty</value>
				 -->
				<value>QuoteFieldNames</value>
				<value>WriteDateUseDateFormat</value>
				
				
			</list>
		</property>
	</bean>*/
    	Fastjson2HttpMessageConverter converter = new Fastjson2HttpMessageConverter();
    	
//    	MediaType supportedMediaType1 = new MediaType("application/json;charset=UTF-8");
//    	MediaType supportedMediaType2 = new MediaType("text/html;charset=UTF-8");
//		converter.getSupportedMediaTypes().add(supportedMediaType1);
//		converter.getSupportedMediaTypes().add(supportedMediaType2);
		
		converter.setFeatures(SerializerFeature.QuoteFieldNames,
				SerializerFeature.WriteDateUseDateFormat);
    	
    	return converter;
    }
}
