package com.github.doobo.conf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.doobo.params.ErrorInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
	@SuppressWarnings("rawtypes")
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String uri = request.getURI().toString();
        if (notNecessaryWrapperURI(uri))
            return body;
        ResultTemplate result;
        //本身就是 ResultTemplate 直接返回
        if (body instanceof ResultTemplate) {
            result = (ResultTemplate) body;
        } else {
            result = ResultUtils.of(body);
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

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ResultTemplate handle(Exception e) {
		//参数异常
		if (e instanceof BindException) {
			return ResultUtils.of(null, new ErrorInfo(0,
				getErrorResultMessage(((BindException) e).getAllErrors())));
		}
		if (e instanceof MethodArgumentNotValidException) {
			return ResultUtils.of(null, new ErrorInfo(0,
				getErrorResultMessage(((MethodArgumentNotValidException) e).getBindingResult().getAllErrors())));
		}
		if (e instanceof CustomException) {
			CustomException customException = (CustomException) e;
			return ResultUtils.of(null, new ErrorInfo(customException.getCode(), e.getMessage()));
		}
		return ResultUtils.of(null, new ErrorInfo(0, e.getMessage()));
	}

	/**
	 * 获取错误集信息
	 *
	 * @param allErrors 所有错误
	 * @return
	 */
	private String getErrorResultMessage(List<ObjectError> allErrors) {
		if (allErrors == null || allErrors.isEmpty()) {
			return null;
		}
		return allErrors.stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(" | "));
	}

}
