package com.github.doobo.conf;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * ipfs节点配置
 */
@Data
@Accessors(chain = true)
public class Node {

	private String ip;

	private String port;

	@JSONField(name = "ID")
	private String cid;

	@JSONField(name = "Addresses")
	private List<String> ipfs;

	@JSONField(name = "AgentVersion")
	private String agentVersion;

	@JSONField(name = "ProtocolVersion")
	private String protocolVersion;
}
