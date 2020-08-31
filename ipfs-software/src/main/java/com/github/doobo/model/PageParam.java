package com.github.doobo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Map;

/**
 * 分页参数
 */
@Data
@Accessors(chain = true)
public class PageParam {

	/**
	 * 每页显示的条数
	 */
    @Min(value = 0, message = "每页显示的条数最小为0")
	@Max(value = 10000, message = "每次查询不超过1W条")
    @JsonProperty("size")
    private Long size = 20L;

	/**
	 * 当前的页码
	 */
	@Min(value = 1, message = "当前的页码最小为1")
    private Long index = 1L;

	/**
	 * 排序字段,true-asc,false-desc
	 */
	private Map<String, Boolean> sortList;
}
