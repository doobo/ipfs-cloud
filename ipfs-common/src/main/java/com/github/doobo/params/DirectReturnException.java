package com.github.doobo.params;

import lombok.Getter;

/**
 * 直接返回处理
 */
public class DirectReturnException extends RuntimeException {

    @Getter
    private Object data;

    @Getter
    private int code;

    public DirectReturnException() {
    }

    public DirectReturnException(Object data) {
    	this.data = data;
    }

	public DirectReturnException(Object data, int code) {
		this.code = code;
	}

    public DirectReturnException(String message) {
        super(message);
    }

    public DirectReturnException(int code, String message) {
        super(message);
        this.code = code;
    }

    public DirectReturnException(ErrorInfo errorInfo) {
        super(errorInfo.getMsg());
        this.code = errorInfo.getCode();
    }

}
