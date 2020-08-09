package com.github.doobo.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MyAppHealthIndicator implements HealthIndicator {

	/**
	 * 自定义的检查方法
	 * @return
	 */
	@Override
	public Health health() {
		return Health.up().build();
		//return Health.down().withDetail("msg", "服务异常").build();
	}
}

