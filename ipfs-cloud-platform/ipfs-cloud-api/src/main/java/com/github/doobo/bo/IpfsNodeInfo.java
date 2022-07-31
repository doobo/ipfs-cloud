package com.github.doobo.bo;

import lombok.Data;

import java.util.List;

/**
 * 节点信息
 *
 * @Description: ipfs-cloud
 * @User: doobo
 * @Time: 2022-07-29 18:00
 */
@Data
public class IpfsNodeInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	private String ip;

	private String port;

	private String cid;

	private List<String> address;

	private String agentVersion;

	private String protocolVersion;
}
