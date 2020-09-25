package com.github.doobo.soft;

import com.github.doobo.params.CustomException;
import com.github.doobo.params.DirectReturnException;
import com.github.doobo.params.ErrorInfo;
import com.github.doobo.params.ResultTemplate;
import com.github.doobo.utils.ResultUtils;
import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PackagePrivate
public class ExceptionHandlerUtils {

	/**
	 * 通用异常处理
	 * @param e
	 */
	public static ResultTemplate<?> convertResultTemplate(Throwable e){
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
		if (e instanceof DirectReturnException) {
			DirectReturnException directReturnException = (DirectReturnException) e;
			if(directReturnException.getData() == null){
				return ResultUtils.of(null, new ErrorInfo(directReturnException.getCode(), e.getMessage()));
			}
			if(directReturnException.getData() instanceof ResultTemplate){
				return (ResultTemplate<?>) directReturnException.getData();
			}
			return ResultUtils.of(directReturnException.getData());
		}
		if(e instanceof ResponseStatusException){
			ResponseStatusException rse = (ResponseStatusException) e;
			return ResultUtils.of(null, new ErrorInfo(rse.getStatus().value(),
				rse.getMessage()));
		}
		return ResultUtils.of(null, new ErrorInfo(0, e.getMessage()));
	}


	/**
	 * 获取错误集信息
	 * @param allErrors 所有错误
	 */
	public static String getErrorResultMessage(List<ObjectError> allErrors) {
		if (allErrors == null || allErrors.isEmpty()) {
			return null;
		}
		return allErrors.stream()
			.map(ObjectError::getDefaultMessage)
			.collect(Collectors.joining(" | "));
	}
}
