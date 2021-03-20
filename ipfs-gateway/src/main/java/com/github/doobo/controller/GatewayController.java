package com.github.doobo.controller;

import com.github.doobo.conf.IpfsConfig;
import com.github.doobo.model.IpfsPubVO;
import com.github.doobo.service.IpfsConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
public class GatewayController implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;

	@Resource
	private IpfsConfigService ipfsConfigService;

	private RouteDefinitionLocator routeDefinitionLocator;
	private List<GlobalFilter> globalFilters;
	private List<GatewayFilterFactory> GatewayFilters;
	private RouteDefinitionWriter routeDefinitionWriter;
	private RouteLocator routeLocator;

	public GatewayController(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters,
									 List<GatewayFilterFactory> GatewayFilters, RouteDefinitionWriter routeDefinitionWriter,
									 RouteLocator routeLocator) {
		this.routeDefinitionLocator = routeDefinitionLocator;
		this.globalFilters = globalFilters;
		this.GatewayFilters = GatewayFilters;
		this.routeDefinitionWriter = routeDefinitionWriter;
		this.routeLocator = routeLocator;
	}

	/**
	 * 获取Ipfs的基础配置
	 */
	@GetMapping("/next")
	public Mono<IpfsConfig> getIpfsConfig2(){
		return Mono.just(ipfsConfigService.queryIpfsConfig());
	}


	/*@PostMapping("/sendMsg")
	public Mono<ResultTemplate<Boolean>> save(@RequestBody Mono<IpfsPubVO> route) {
		return this.routeDefinitionWriter.save(route.map(r ->  {
			ipfsConfigService.pubMsg(r);
			return Mono.empty(ipfsConfigService.pubMsg(r));
		})).then(Mono.defer(() ->
			Mono.just(ResponseEntity.created(URI.create("/sendMsg")).build())
		));
	}*/

	@PostMapping("/routes/{id}")
	public Mono<ResponseEntity<Void>> save(@PathVariable String id, @RequestBody Mono<RouteDefinition> route) {
		return this.routeDefinitionWriter.save(route.map(r ->  {
			r.setId(id);
			log.info("Saving route: " + route);
			return r;
		})).then(Mono.defer(() ->
			Mono.just(ResponseEntity.created(URI.create("/routes/"+id)).build())
		));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = publisher;
	}
}
