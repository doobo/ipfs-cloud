package com.github.doobo.config;

import com.github.doobo.bo.IpfsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	@ConfigurationProperties("ipfs")
	@ConditionalOnMissingBean(IpfsProperties.class)
	public IpfsProperties ipfsProperties() {
		return new IpfsProperties();
	}

	public static IpfsProperties getIpfsProperties() {
		return SpringUtil.getBean(IpfsProperties.class);
	}
}
