package com.github.doobo.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

	@Bean
	public Retryer feignRetryer(){
		return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1),5);
	}
}
