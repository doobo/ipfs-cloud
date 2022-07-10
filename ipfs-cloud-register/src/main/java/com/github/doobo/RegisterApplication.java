package com.github.doobo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 添加默认注册中心
 */
@EnableEurekaServer
@SpringBootApplication
public class RegisterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterApplication.class, args);
	}

	@EnableWebSecurity
	static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().authorizeRequests()
				.anyRequest()
				.authenticated()
				.and()
				.httpBasic();
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring()
				.antMatchers(
					HttpMethod.POST,
					"/actuator/**"
				)
				.and()
				.ignoring()
				.antMatchers(
					HttpMethod.GET,
					"/actuator/**",
					"/*.html",
					"/favicon.ico",
					"/**/*.html",
					"/**/*.css",
					"/**/*.js"
				);
		}
	}
}
