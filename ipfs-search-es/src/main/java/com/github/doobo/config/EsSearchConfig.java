package com.github.doobo.config;

import lombok.extern.slf4j.Slf4j;
import org.frameworkset.elasticsearch.boot.BBossESStarter;
import org.frameworkset.elasticsearch.client.ClientInterface;
import org.frameworkset.elasticsearch.client.ClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

@Configuration
@Slf4j
public class EsSearchConfig {

	@Resource
	private BBossESStarter bbossESStarter;

	@Bean("ipfsSearch")
	@Primary
	public ClientInterface getClientInterface(){
		return bbossESStarter.getConfigRestClient("mapping/IpfsSearchDSL.xml");
	}

	@Bean
	public ClientOptions clientOptions(){
		ClientOptions clientOptions = new ClientOptions();
		clientOptions.setRefreshOption("refresh=true");
		return clientOptions;
	}

}
