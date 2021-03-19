package com.github.doobo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

@Component
@Slf4j
public class WebSocketFilter implements GlobalFilter, Ordered {

	private final static String DEFAULT_FILTER_PATH = "/ipfs/sub";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);
		String scheme = requestUrl.getScheme();
		if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
			return chain.filter(exchange);
		} else if (DEFAULT_FILTER_PATH.equals(requestUrl.getPath())) {
			String wsScheme = convertWsToHttp(scheme);
			URI wsRequestUrl = UriComponentsBuilder.fromUri(requestUrl).scheme(wsScheme).build().toUri();
			exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, wsRequestUrl);
		}
		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 2;
	}

	private static String convertWsToHttp(String scheme) {
		scheme = scheme.toLowerCase();
		return "ws".equals(scheme) ? "http" : "wss".equals(scheme) ? "https" : scheme;
	}
}
