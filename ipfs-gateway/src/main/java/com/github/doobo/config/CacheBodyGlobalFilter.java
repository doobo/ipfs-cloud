package com.github.doobo.config;


import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.*;

/**
 * 重写body
 */
@Component
public class CacheBodyGlobalFilter implements Ordered, GlobalFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		if (exchange.getRequest().getHeaders().getContentType() == null) {
			return chain.filter(exchange);
		}
		//微信小程序的GET方法默认会传ContentType过来,且不带body,导致处理失败
		if(exchange.getRequest().getMethod() == HttpMethod.GET){
			return chain.filter(exchange);
		}
		MediaType contentType = exchange.getRequest().getHeaders().getContentType();
		//文件类上传，直接转发
		if(contentType == null || contentType.includes(MULTIPART_FORM_DATA)){
			return chain.filter(exchange);
		}
		//非JSON和APPLICATION_FORM_URLENCODED格式,跳过body校验
		if(!contentType.includes(APPLICATION_FORM_URLENCODED)
			&& !contentType.includes(APPLICATION_JSON)
			&& !contentType.includes(APPLICATION_PROBLEM_JSON)){
			return chain.filter(exchange);
		}
		return DataBufferUtils.join(exchange.getRequest().getBody())
			.flatMap(dataBuffer -> {
				DataBufferUtils.retain(dataBuffer);
				Flux<DataBuffer> cachedFlux = Flux
					.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
				ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
					exchange.getRequest()) {
					@Override
					public Flux<DataBuffer> getBody() {
						return cachedFlux;
					}
				};
				return chain.filter(exchange.mutate().request(mutatedRequest).build());
			});
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
