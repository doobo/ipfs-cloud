package com.github.doobo.vbo;

import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 多个返回结果合并
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2022-09-02 12:01
 */
public class ResultMergeBo extends PageBaseQuery {

    private static final long serialVersionUID = 1L;

    private List<ResultTemplate<?>> resultList;

    public static ResultMergeBo of(){
        return new ResultMergeBo();
    }

    /**
     * 添加结果数据
     */
    public <T> void addResult(ResultTemplate<T> result){
        resultList = Optional.ofNullable(resultList).orElseGet(ArrayList::new);
        Optional.ofNullable(result).ifPresent(resultList::add);
    }

    /**
     * 信息返回日志
     */
    @Data
    public static class MsgData implements java.io.Serializable {

        private static final long serialVersionUID = 1L;

        private int result;

		private String message;

		private String code;

		private String subCode;

		private String subMsg;

        public MsgData() {
        }

		public MsgData(int result, String message, String code, String subCode, String subMsg) {
			this.result = result;
			this.message = message;
			this.code = code;
			this.subCode = subCode;
			this.subMsg = subMsg;
		}
	}

    /**
     * 装换成功数据列表
     */
    public List<MsgData> convertList(){
        if(Objects.isNull(resultList) || resultList.isEmpty()){
            return Collections.emptyList();
        }
        return resultList.stream().filter(Objects::nonNull)
                .map(m -> new MsgData(m.getResult(), m.getMessage(), m.getCode()
					, m.getSubCode(), m.getSubMsg()))
                .collect(Collectors.toList());
    }
}
