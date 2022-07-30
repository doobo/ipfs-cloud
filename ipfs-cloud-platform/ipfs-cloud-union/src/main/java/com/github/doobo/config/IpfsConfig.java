package com.github.doobo.config;

import com.github.doobo.handler.PlatformInitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * IPFS基本配置
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-07-30 21:27
 */
@Configuration
@ConditionalOnMissingBean(IpfsProperties.class)
@AutoConfigureOrder(PlatformInitHandler.DEFAULT_PHASE)
@EnableConfigurationProperties({IpfsProperties.class})
public class IpfsConfig {

	private static IpfsProperties ipfsProperties;

	public static IpfsProperties getIpfsProperties() {
		return ipfsProperties;
	}

	@Autowired
	public void setIpfsProperties(IpfsProperties ipfsProperties) {
		IpfsConfig.ipfsProperties = ipfsProperties;
	}
}
