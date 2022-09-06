package com.github.doobo.utils;

import com.github.doobo.vbo.ExchangeModel;
import com.github.doobo.vbo.PageBaseQuery;
import com.github.doobo.vbo.ResultTemplate;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 返回模板工具类
 */
public abstract class ResultUtils {

    public static ResultTemplate<Boolean> ofThrowable(Throwable throwable) {
        if (throwable == null) {
            return of(Boolean.TRUE);
        }
        return of(Boolean.FALSE, throwable.getMessage());
    }

    public static <T extends Serializable> ResultTemplate<T> ofError(Throwable throwable, T t) {
        if (throwable == null) {
            return of(t);
        }
        return of(t, throwable.getMessage());
    }

    public static ResultTemplate<Boolean> of(int effectedNum) {
        if (effectedNum > 0) {
            return of(Boolean.TRUE);
        }
        return of(Boolean.FALSE, "操作失败");
    }

    public static <T extends Serializable> ResultTemplate<T> of(T data) {
        return of(data, null);
    }

    public static <T extends Serializable> ResultTemplate<List<T>> ofList(List<T> data) {
        ResultTemplate<List<T>> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setMessage("操作成功");
        tResultTemplate.setData(data);
        return tResultTemplate;
    }

    public static <T extends Serializable> ResultTemplate<T> of(T data, String errorInfo) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        if (errorInfo == null) {
            tResultTemplate.setSuccess(true);
            tResultTemplate.setMessage("操作成功");
            tResultTemplate.setData(data);
        } else {
            tResultTemplate.setSuccess(false);
            tResultTemplate.setResult(500);
            tResultTemplate.setData(data);
            tResultTemplate.setMessage(errorInfo);
            tResultTemplate.setSubMsg(errorInfo);
        }
        return tResultTemplate;
    }

    /**
     * 对象转单例集合
     */
    public static <T extends Serializable> ResultTemplate<List<T>> singleList(T data) {
        List<T> rs = data != null ? Collections.singletonList(data) : Collections.emptyList();
        return ofList(rs);
    }

    /**
     * 分页对象返回
     */
    public static <T extends Serializable> ResultTemplate<List<T>> ofPage(List<T> data, PageBaseQuery page) {
        ResultTemplate<List<T>> of = ofList(data);
        if(page != null){
            PageBaseQuery query = new PageBaseQuery();
            query.setPageNo(page.getPageNo());
            query.setPageSize(page.getPageSize());
            query.setTotal(page.getTotal());
            query.setCursor(page.getCursor());
            query.setSortList(page.getSortList());
            query.setSort(page.getSort());
            of.setPage(query);
        }
        return of;
    }

    /**
     * 带消息返回带成功结果
     */
    public static <T extends Serializable> ResultTemplate<T> ofMsg(T data, String msg) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setData(data);
        tResultTemplate.setMessage(msg);
        return tResultTemplate;
    }

    /**
     * 列表返回,可带错误信息
     */
    public static <T extends Serializable> ResultTemplate<List<T>> ofList(List<T> data, String errorMsg) {
        ResultTemplate<List<T>> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setData(data);
        if(errorMsg != null && !errorMsg.isEmpty()){
            tResultTemplate.setSuccess(false);
            tResultTemplate.setResult(500);
            tResultTemplate.setMessage(errorMsg);
            tResultTemplate.setSubMsg(errorMsg);
        }
        return tResultTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> ResultTemplate<T> ofFail(String msg) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(false);
        tResultTemplate.setResult(500);
        tResultTemplate.setMessage(msg);
        tResultTemplate.setSubMsg(msg);
        return tResultTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> ResultTemplate<T> ofFail(String msg, String code) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(false);
        tResultTemplate.setResult(500);
        tResultTemplate.setMessage(msg);
        tResultTemplate.setCode(code);
        tResultTemplate.setSubCode(code);
        tResultTemplate.setSubMsg(msg);
        return tResultTemplate;
    }

    /**
     * 错误信息返回
     */
    public static <T> ResultTemplate<T> ofFail(String msg, int code) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(false);
        tResultTemplate.setResult(code);
        tResultTemplate.setMessage(msg);
        tResultTemplate.setSubMsg(msg);
        return tResultTemplate;
    }

	/**
	 * 错误信息返回
	 */
	public static <T> ResultTemplate<T> ofSuccess(String msg, String code) {
		ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
		tResultTemplate.setSuccess(true);
		tResultTemplate.setResult(1);
		tResultTemplate.setMessage(msg);
		tResultTemplate.setCode(code);
		tResultTemplate.setSubMsg(msg);
		tResultTemplate.setSubCode(code);
		return tResultTemplate;
	}

    /**
     * 列表返回,可带提示信息
     */
    public static <T extends Serializable> ResultTemplate<List<T>> ofListMsg(List<T> data, String msg) {
        ResultTemplate<List<T>> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setData(data);
        tResultTemplate.setMessage(msg);
        return tResultTemplate;
    }

    /**
     * 列表返回
     */
    public static <T> ResultTemplate<List<T>> ofUnsafeList(List<T> data) {
        ResultTemplate<List<T>> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setMessage("操作成功");
        tResultTemplate.setData(data);
        return tResultTemplate;
    }

    /**
     * 分页对象返回
     */
    public static <T> ResultTemplate<List<T>> ofUnsafePage(List<T> data, PageBaseQuery page) {
        ResultTemplate<List<T>> of = ofUnsafeList(data);
        if(page != null){
            PageBaseQuery query = new PageBaseQuery();
            query.setPageNo(page.getPageNo());
            query.setPageSize(page.getPageSize());
            query.setTotal(page.getTotal());
            query.setCursor(page.getCursor());
            query.setSortList(page.getSortList());
            of.setPage(query);
        }
        return of;
    }

    /**
     * 单对象返回
     */
    public static <T> ResultTemplate<T> ofUnsafe(T data) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        tResultTemplate.setSuccess(true);
        tResultTemplate.setMessage("操作成功");
        tResultTemplate.setData(data);
        return tResultTemplate;
    }

    /**
     * 添加执行结果,顺序递增
     */
    public static <T> ExchangeModel<T> addResult(ExchangeModel<T> bo, ExchangeModel.ResultDo<?> rs){
        if(Objects.isNull(bo)){
            bo = ExchangeModel.of(null);
        }
        bo.addResultDo(String.valueOf(SequenceUtils.nextId()), rs);
        return bo;
    }
}
