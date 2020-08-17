package com.github.doobo.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ipfs常用基本配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "ipfs")
public class IpfsConfig {

	private String[] bootstrap;

	private Integer port;

	private Integer adminPort;

	private Integer httpPort;

	private boolean privateNetwork;
}
