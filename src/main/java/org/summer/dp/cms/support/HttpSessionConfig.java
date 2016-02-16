package org.summer.dp.cms.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableRedisHttpSession 
public class HttpSessionConfig {

        @Bean
        public JedisConnectionFactory connectionFactory() {
        	System.out.println("---------------------");
                return new JedisConnectionFactory();  
        }

        @Bean
        public HttpSessionStrategy httpSessionStrategy() {
                return new HeaderHttpSessionStrategy(); 
        }
}