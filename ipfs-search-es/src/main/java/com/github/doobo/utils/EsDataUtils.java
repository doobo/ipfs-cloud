package com.github.doobo.utils;

import com.github.doobo.model.PageParam;
import com.github.doobo.params.ErrorInfo;
import com.github.doobo.params.ResultTemplate;
import lombok.experimental.PackagePrivate;
import org.frameworkset.elasticsearch.entity.ESDatas;

import java.util.Collections;
import java.util.List;

/**
 * es数据处理工具类
 */
@PackagePrivate
public class EsDataUtils {

	public static <T> ResultTemplate<List<T>> ofESDataAndPage(ESDatas<T> esData, PageParam pageParam) {
		return of(esData.getDatas(), null, null, pageParam.getSize(), pageParam.getIndex(), esData.getTotalSize());
	}

	public static <T> ResultTemplate<List<T>> of(List<T> data, ErrorInfo errorInfo, Long changes, Long size, Long pageIndex, Long pageTotal) {
		ResultTemplate<List<T>> tResultTemplate = new ResultTemplate<>();
		if (errorInfo == null) {
			List<T> list = data == null ? Collections.emptyList() : data;
			tResultTemplate.setOk(true)
				.setData(list);
		} else {
			tResultTemplate.setOk(false)
				.setErr(errorInfo)
				.setData(Collections.emptyList());
		}
		tResultTemplate
			.setChanges(changes)
			.setSize(size)
			.setIndex(pageIndex)
			.setTotal(pageTotal)
			.setPages(pages(pageTotal, size));
		return tResultTemplate;
	}

	/**
	 * 计算总页数
	 * @param total
	 * @param size
	 */
	public static Long pages(Long total, Long size){
		if(total == null || total <= 0){
			return 0L;
		}
		size = size == null?10L:size;
		long pageCount = total / size;
		if (total % size != 0){
			pageCount ++;
		}
		return pageCount;
	}

}
