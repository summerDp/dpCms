package org.summer.dp.cms.helper.servlet;

import java.util.Properties;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
/**
 * GOOGLE 图片验证码代理器
 * @author 赵宝东
 *
 */
public class KaptchaProducerAgency {
	private static DefaultKaptcha kaptchaProducer = null;
	
    private static void init(){
    	kaptchaProducer = new DefaultKaptcha();
    	Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.textproducer.font.color", "black");
		properties.setProperty("kaptcha.noise.color", "black");
		properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
		properties.setProperty("kaptcha.textproducer.char.string", "1234567890");
		properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
		properties.setProperty("kaptcha.image.width", "100");
		properties.setProperty("kaptcha.textproducer.font.size", "21");
		properties.setProperty("kaptcha.image.height", "40");
		properties.setProperty("kaptcha.session.key", "loginCode");
		properties.setProperty("kaptcha.textproducer.char.length", "5");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		kaptchaProducer.setConfig(config);
    }
    
	public static DefaultKaptcha getKaptchaProducerExample() {
		if (kaptchaProducer == null) {
			synchronized (DefaultKaptcha.class) {
				if (null == kaptchaProducer) {
					init();
				}
			}
		}
		return kaptchaProducer;
	}
	






}
