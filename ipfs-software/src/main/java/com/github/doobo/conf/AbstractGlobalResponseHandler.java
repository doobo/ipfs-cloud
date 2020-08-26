package com.github.doobo.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
/**
 * 全局返回处理
 **/
@Order
@Slf4j
@RestControllerAdvice
public abstract class AbstractGlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String uri = request.getURI().toString();
        if (notNecessaryWrapperURI(uri))
            return body;
        ResultTemplate result;
        //本身就是 ResultTemplate 直接返回
        if (body instanceof ResultTemplate) {
            result = (ResultTemplate) body;
        } else {
            result = ResultTemplateUtil.of(body);
        }

        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException ignored) {
            	log.error("beforeBodyWriteError", ignored);
            }
        }
        return result;
    }

	/**
	 * 配置不需要进行统一返回处理的URL
	 * uri.contains("/v2/api-docs") || uri.contains("/swagger-resources") || uri.contains("/actuator");
	 * @param uri
	 */
	public abstract boolean notNecessaryWrapperURI(String uri);
}
