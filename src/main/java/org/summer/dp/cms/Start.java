package org.summer.dp.cms;


import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.summer.dp.cms.support.Fastjson2HttpMessageConverter;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@EnableTransactionManagement
public class Start extends WebMvcConfigurerAdapter{

		
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Start.class);
	}
	
	 @Override
	 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	  converters.add(Fastjson2HttpMessageConverter.makeFastjson2HttpMessageConverter());
	 }
	 
    public static void main(String[] args) {
		SpringApplication.run(Start.class, args);
	} 
    
    /**
     * 配置拦截器
     * @param registry
     */
    public void addInterceptors(InterceptorRegistry registry) {
    	//registry.addInterceptor(new UserSecurityInterceptor()).addPathPatterns("/user/**");
	}
    
    /**
     * spring boot 定时任务
     */
    @Scheduled(cron="0 0 22 * * ?")
    public void doSth() {

    }
}