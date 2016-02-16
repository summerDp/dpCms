package org.summery.dp.cms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
public class SampleDataJpaApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SampleDataJpaApplication.class, args);
	}

}