package com.github.doobo.config;

import com.github.doobo.bo.IpfsProperties;
import com.github.doobo.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * IPFS默认配置
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-30 21:27
 */
@Configuration
@PropertySource(value = "classpath:ipfs-config-default.yml"
	, encoding = "utf-8", factory = YamlPropertySourceFactory.class)
public class IpfsInitDefaultConfig {

	@Bean
	@ConfigurationProperties("ipfs.default")
	@ConditionalOnMissingBean(name = "defaultIpfsProperties")
	public IpfsProperties defaultIpfsProperties() {
		return new IpfsProperties();
	}

	public static IpfsProperties getDefaultIpfsProperties() {
		return (IpfsProperties)SpringUtil.getBean("defaultIpfsProperties");
	}

	private static class CustomForkJoinWorkerThread extends ForkJoinWorkerThread {
		CustomForkJoinWorkerThread(ForkJoinPool pool) {
			super(pool);
			setContextClassLoader(Thread.currentThread().getContextClassLoader());
		}
	}

	/**
	 * 创建线程池
	 */
	public static ForkJoinPool createForkJoinPool() {
		return new ForkJoinPool(
			ForkJoinPool.getCommonPoolParallelism(),
			CustomForkJoinWorkerThread::new,
			null,
			true
		);
	}

}
