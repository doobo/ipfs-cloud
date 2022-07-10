package com.github.doobo.vbo;

import java.io.Serializable;

public class ResultTemplate<T> implements Serializable {

    private Boolean success = Boolean.TRUE;

    private int result = 1;

    private String message;

    private T data;

    private String errorCode;

    private String errorMessage;

    /**
     * 分页参数,无分页时为空
     */
    private PageBaseQuery page;

    private String clueId;

    public static ResultTemplate<String> fail(String msg) {
        ResultTemplate<String> response = new ResultTemplate<>();
        response.setSuccess(Boolean.FALSE);
        response.setResult(500);
        response.setMessage(msg);
        response.setErrorMessage(msg);
        return response;
    }

    public static <K> ResultTemplate<K> success(K data) {
        ResultTemplate<K> response = new ResultTemplate<>();
        response.setData(data);
        response.setMessage("操作成功");
        return response;
    }

    public static <K> ResultTemplate<K> successMsg(String msg) {
        ResultTemplate<K> response = new ResultTemplate<>();
        response.setMessage(msg);
        return response;
    }

    /**
     * 执行状态
     */
    public boolean isSuccess(){
        if(success == null || !success || result != 1){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private static final long serialVersionUID = 1L;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public PageBaseQuery getPage() {
        return page;
    }

    public void setPage(PageBaseQuery page) {
        this.page = page;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }
}
