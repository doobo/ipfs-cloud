package com.github.doobo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class ExecutorConfig implements AsyncConfigurer {

	@Bean
	public Executor customServiceExecutor(){
		ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
		//线程核心数目
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setAllowCoreThreadTimeOut(true);
		//最大线程数
		threadPoolTaskExecutor.setMaxPoolSize(10);
		//配置队列大小
		threadPoolTaskExecutor.setQueueCapacity(50);
		//配置线程池前缀
		threadPoolTaskExecutor.setThreadNamePrefix("custom-service-");
		//配置拒绝策略
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		//数据初始化
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}

	/**
	 * 配置异常处理机制
	 * @return
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex,method,params)->{
			log.error("异步线程执行失败。方法：[{}],异常信息[{}] : ", method, ex.getMessage(),ex);
		};
	}
}
