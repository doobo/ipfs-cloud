package com.github.doobo.vbo;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * 分页参数
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2021-11-04 10:44
 */
public class PageBaseQuery implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 每页显示的条数
     */
    @Min(value = 0, message = "每页显示的条数最小为0")
    @Max(value = 10000, message = "每次查询不超过1W条")
    private long pageSize = 10;

    /**
     * 当前的页码
     */
    @Min(value = 1, message = "当前的页码最小为1")
    private long pageNo = 1;

    /**
     * 排序字段,true-asc,false-desc
     */
    private Map<String, Boolean> sortList;

    /**
     * 偏移位置
     */
    private long offset;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 分页数
     */
    private long pages;

    /**
     * 游标
     */
    private String cursor;

	/**
	 * 额外扩展参数
	 */
	private List<Object> sort;

    /**
     * 获取数据偏移量
     */
    public long getOffset() {
        if(this.pageSize < 1){
            pageSize = 10;
        }
        offset = (pageNo - 1) * pageSize;
        return offset;
    }

    /**
     * 获取分页总数
     */
    public long getPages() {
        this.pages = this.total % this.pageSize == 0 ? this.total / this.pageSize : this.total / this.pageSize + 1;
        return pages;
    }

    public int intSize(){
        return Long.valueOf(pageSize).intValue();
    }

    public int intIndex(){
        return Long.valueOf(pageNo).intValue();
    }

    public int intPage(){
        return Long.valueOf(getPages()).intValue();
    }

    public int intTotal(){
        return Long.valueOf(total).intValue();
    }

    public boolean hasNext(){
        return getPages() > pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public Map<String, Boolean> getSortList() {
        return sortList;
    }

    public void setSortList(Map<String, Boolean> sortList) {
        this.sortList = sortList;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setPages(long pages) {
        this.pages = pages;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

	public List<Object> getSort() {
		return sort;
	}

	public void setSort(List<Object> sort) {
		this.sort = sort;
	}
}
