package com.github.doobo.conf;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ipfs常用基本配置
 */
@Data
@Component
@Accessors(chain = true)
@ConfigurationProperties(prefix = "ipfs")
public class IpfsConfig {

	private String[] bootstrap;

	private Integer port;

	private Integer adminPort;

	private Integer httpPort;

	private boolean privateNetwork;

	private boolean startDaemon;

	private List<Node> nodes;
}
