package org.summer.dp.cms.support;

import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.AbstractFileConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * 动态配置文件,感谢APACHE ,到1.10中文有BUG	 VALUE别放中文
 * @author 赵宝东
 *
 */
public class PlatformConfiguration {

	public static AbstractFileConfiguration config = null ;
	private static PlatformConfiguration smsConfiguration = new PlatformConfiguration(); 


	private PlatformConfiguration(){
		try {
			config = new PropertiesConfiguration("platform.properties");
			config.setReloadingStrategy(new FileChangedReloadingStrategy());			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}  
	}

	public static PlatformConfiguration getSmsConfiguration(){
		return smsConfiguration ; 
	}

//	public static void main(String[] args){
//		while ( true ){
//			System.out.println(PlatformConfiguration.config.getString("basePath"));
//			try {
//				TimeUnit.SECONDS.sleep(8);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
}
