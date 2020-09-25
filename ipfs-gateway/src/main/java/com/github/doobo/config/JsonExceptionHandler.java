package com.github.doobo.config;

import com.github.doobo.params.ResultTemplate;
import com.github.doobo.soft.ExceptionHandlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.Map;

/**
 * webFlux自定义异常
 */
@Slf4j
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {

	public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resourceProperties, errorProperties, applicationContext);
	}

	@Override
	protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		Throwable error = super.getError(request);
		ResultTemplate<?> resultTemplate = response(error);
		if(resultTemplate.getErr() != null){
			resultTemplate.setCode(resultTemplate.getErr().getCode());
		}
		return BeanMap.create(resultTemplate);
	}

	/**
	 * 指定响应处理方法为JSON处理的方法
	 * @param errorAttributes
	 */
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}


	/**
	 * 根据code获取对应的HttpStatus
	 * @param errorAttributes
	 */
	@Override
	protected int getHttpStatus(Map<String, Object> errorAttributes) {
		Object statusCode = errorAttributes.get("code");
		if(statusCode == null){
			return HttpStatus.OK.value();
		}
		int code = (int)statusCode;
		if(code == 0){
			return HttpStatus.OK.value();
		}
		for(HttpStatus item : HttpStatus.values()){
			if(item.value() == code){
				return code;
			}
		}
		return HttpStatus.OK.value();
	}

	/**
	 * 构建返回的JSON数据格式
	 */
	public static ResultTemplate<?> response(Throwable e) {
		return ExceptionHandlerUtils.convertResultTemplate(e);
	}


}
