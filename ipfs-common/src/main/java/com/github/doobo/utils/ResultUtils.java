package com.github.doobo.utils;

import com.github.doobo.params.ErrorInfo;
import com.github.doobo.params.ResultTemplate;
import java.util.Collections;
import java.util.List;

/**
 * 返回模板工具类
 *
 */
public final class ResultUtils {

    public static ResultTemplate<Boolean> ofThrowable(Throwable throwable) {
        if (throwable == null) {
            return of(Boolean.TRUE);
        }
        return of(Boolean.FALSE, new ErrorInfo(throwable.getMessage()));
    }

	public static <T> ResultTemplate<T> ofError(Throwable throwable, T t) {
		if (throwable == null) {
			return of(t);
		}
		return of(t, new ErrorInfo(throwable.getMessage()));
	}

    public static ResultTemplate<Boolean> of(int effectedNum) {
        if (effectedNum > 0) {
            return of(Boolean.TRUE);
        }
        return of(Boolean.FALSE, new ErrorInfo("操作失败"));
    }

    public static <T> ResultTemplate<T> of(T data) {
        return of(data, null);
    }

    public static <T> ResultTemplate<T> of(T data, ErrorInfo errorInfo) {
        return of(data, errorInfo, null, null, null, null);
    }

    public static <T> ResultTemplate<T> of(T data, ErrorInfo errorInfo, Long changes, Long PageCount, Long pageIndex, Long pageTotal) {
        ResultTemplate<T> tResultTemplate = new ResultTemplate<>();
        if (errorInfo == null) {
            tResultTemplate.setOk(true)
                    .setData(data);
        } else {
            tResultTemplate.setOk(false)
					.setData(data)
                    .setErr(errorInfo);
        }
        tResultTemplate
                .setChanges(changes)
                .setPages(PageCount)
                .setIndex(pageIndex)
                .setTotal(pageTotal);
        return tResultTemplate;
    }

	/**
	 * 对象转单例集合
	 * @param data
	 * @param <T>
	 */
    public static <T> List<T> singleList(T data) {
        return data != null ? Collections.singletonList(data) : Collections.emptyList();
    }

	/**
	 * 定义空数组类型
	 * @param <T>
	 */
	public static <T> List<T> singleList(Class<T> cls) {
		return Collections.emptyList();
	}
}
