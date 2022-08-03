package com.github.doobo.config;

import com.github.doobo.bo.IpfsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * IPFS基本配置
 *
 * @Description: ipfs-cloud
 * @User: diding
 * @Time: 2022-07-30 21:27
 */
@Configuration
public class IpfsInitConfig {

	private static boolean isFirst;

	@Bean
	@ConfigurationProperties("ipfs")
	@SuppressWarnings("ConfigurationProperties")
	@ConditionalOnMissingBean(name = "ipfsProperties")
	public IpfsProperties ipfsProperties() {
		return new IpfsProperties();
	}

	/**
	 * 获取默认配置
	 */
	public static IpfsProperties getIpfsProperties() {
		IpfsProperties properties = (IpfsProperties) SpringUtil.getBean("ipfsProperties");
		if(isFirst){
			return properties;
		}
		IpfsProperties defaultIpfsProperties = IpfsInitDefaultConfig.getDefaultIpfsProperties();
		properties.setPort(Optional.ofNullable(properties.getPort()).orElse(defaultIpfsProperties.getPort()));
		properties.setAdminPort(Optional.ofNullable(properties.getAdminPort()).orElse(defaultIpfsProperties.getAdminPort()));
		properties.setHttpPort(Optional.ofNullable(properties.getHttpPort()).orElse(defaultIpfsProperties.getHttpPort()));
		properties.setBindIp(Optional.ofNullable(properties.getBindIp()).orElse(defaultIpfsProperties.getBindIp()));
		properties.setInitDir(Optional.ofNullable(properties.getInitDir()).orElse(defaultIpfsProperties.getInitDir()));
		properties.setPrivateNetwork(Optional.ofNullable(properties.getPrivateNetwork()).orElse(defaultIpfsProperties.getPrivateNetwork()));
		properties.setStartDaemon(Optional.ofNullable(properties.getStartDaemon()).orElse(defaultIpfsProperties.getStartDaemon()));
		properties.setSwarmKey(Optional.ofNullable(properties.getSwarmKey()).orElse(defaultIpfsProperties.getSwarmKey()));
		properties.setWriteFile(Optional.ofNullable(properties.getWriteFile()).orElse(defaultIpfsProperties.getWriteFile()));
		properties.setTopic(Optional.ofNullable(properties.getTopic()).orElse(defaultIpfsProperties.getTopic()));
		properties.setPrivateKey(Optional.ofNullable(properties.getPrivateKey()).orElse(defaultIpfsProperties.getPrivateKey()));
		properties.setPublicKey(Optional.ofNullable(properties.getPublicKey()).orElse(defaultIpfsProperties.getPublicKey()));
		properties.setOwnPrivateKey(Optional.ofNullable(properties.getOwnPrivateKey()).orElse(defaultIpfsProperties.getOwnPrivateKey()));
		properties.setOwnPublicKey(Optional.ofNullable(properties.getOwnPublicKey()).orElse(defaultIpfsProperties.getOwnPrivateKey()));
		properties.setBootstrap(Optional.ofNullable(properties.getBootstrap()).orElse(defaultIpfsProperties.getBootstrap()));
		properties.setCron(Optional.ofNullable(properties.getCron()).orElse(defaultIpfsProperties.getCron()));
		properties.setDelay(Optional.ofNullable(properties.getDelay()).orElse(defaultIpfsProperties.getDelay()));
		properties.setFixedDelay(Optional.ofNullable(properties.getFixedDelay()).orElse(defaultIpfsProperties.getFixedDelay()));
		isFirst = true;
		return properties;
	}
}
