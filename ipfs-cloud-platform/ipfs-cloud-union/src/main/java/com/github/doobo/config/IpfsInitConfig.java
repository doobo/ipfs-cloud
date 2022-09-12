package com.github.doobo.config;

import com.github.doobo.bo.IpfsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * IPFS基本配置
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-07-30 21:27
 */
@Configuration
public class IpfsInitConfig {

	@Bean
	@Primary
	@ConfigurationProperties("ipfs")
	@ConditionalOnProperty(name = "ipfs.startConfig", havingValue = "true")
	@ConditionalOnMissingBean(name = "ipfsProperties")
	public IpfsProperties ipfsProperties() {
		return new IpfsProperties();
	}

	/**
	 * 获取默认配置
	 */
	public static IpfsProperties getIpfsProperties() {
		return SpringUtil.getBean(IpfsProperties.class);
	}
}
