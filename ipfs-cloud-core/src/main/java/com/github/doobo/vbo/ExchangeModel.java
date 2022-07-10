package com.github.doobo.vbo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 入参和执行结果集合
 *
 * @Description: ipfs-cloud-core
 * @User: doobo
 * @Time: 2021-07-13 13:43
 */
@Data
@Accessors(chain = true)
public class ExchangeModel<T> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private T input;

    private Map<String, Object> params;

    private Map<String, ResultDo<?>> result;

    private Boolean success = Boolean.TRUE;

    /**
     * 添加结果集
     */
    public ExchangeModel<T> addResult(String name, Object rs){
        if(result == null){
            result = new HashMap<>();
        }
        if(name == null || name.isEmpty()){
            name = "undefined";
        }
        result.put(name, ofSuccess(rs).setSort(result.size()+1));
        return this;
    }

    /**
     * 添加结果集
     */
    public ExchangeModel<T> addResult(String name, Object rs, Action action){
        if(result == null){
            result = new HashMap<>();
        }
        if(name == null || name.isEmpty()){
            name = "undefined";
        }
        result.put(name,new ResultDo<>(action, rs, result.size()+1));
        return this;
    }

    /**
     * 添加结果集
     */
    public ExchangeModel<T> addResultDo(String name, ResultDo<?> rs){
        if(result == null){
            result = new HashMap<>();
        }
        if(name == null || name.isEmpty()){
            name = "undefined";
        }
        result.put(name, rs);
        return this;
    }

    /**
     * 添加参数
     */
    public ExchangeModel<T> addParam(String key, Object param){
        if(params == null){
            params = new HashMap<>();
        }
        params.put(key, param);
        return this;
    }

    /**
     * 添加多个参数
     */
    public ExchangeModel<T> addParams(Map<String, Object> param){
        if(params == null){
            params = new HashMap<>();
        }
        params.putAll(param);
        return this;
    }

    /**
     * 获取扩展参数
     */
    public Object getParamsByKey(String key){
        if(params == null){
            return null;
        }
        return params.get(key);
    }

    public enum Action {
        EXECUTE_SUCCESS,
        EXECUTE_FAILED,
        EXECUTE_LATER,
        EXECUTE_EXCEPTION;
        Action() {
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class ResultDo<V> implements java.io.Serializable {

        private static final long serialVersionUID = 1L;

        private Action action;

        private V result;

        private int sort;

        private long timeStamp;

        private int code;

        public ResultDo(V result) {
            this.result = result;
            action = Action.EXECUTE_SUCCESS;
            timeStamp = System.currentTimeMillis();
        }

        public ResultDo(V result, int sort) {
            this.result = result;
            this.sort = sort;
            action = Action.EXECUTE_SUCCESS;
            timeStamp = System.currentTimeMillis();
        }

        public ResultDo(Action action, V result, int sort) {
            this.action = action;
            this.result = result;
            this.sort = sort;
            timeStamp = System.currentTimeMillis();
        }

        public ResultDo(V result, int sort, int code) {
            this.action = Action.EXECUTE_SUCCESS;
            this.result = result;
            this.sort = sort;
            this.code = code;
            timeStamp = System.currentTimeMillis();
        }
    }

    /**
     * 获取优先急最高的执行状态
     */
    public Action action(){
        if(result == null){
            return Action.EXECUTE_SUCCESS;
        }
        if(result.values().stream().anyMatch(m -> m.getAction() != null && m.getAction() == Action.EXECUTE_EXCEPTION)){
            return Action.EXECUTE_EXCEPTION;
        }
        if(result.values().stream().anyMatch(m -> m.getAction() != null && m.getAction() == Action.EXECUTE_LATER)){
            return Action.EXECUTE_LATER;
        }
        if(result.values().stream().anyMatch(m -> m.getAction() != null && m.getAction() == Action.EXECUTE_FAILED)){
            return Action.EXECUTE_FAILED;
        }
        return Action.EXECUTE_SUCCESS;
    }

    /**
     * 获取第一个执行结果
     */
    public Object firstResult(){
        if(result == null || result.isEmpty()){
            return null;
        }
        return result.values().stream().min(Comparator.comparing(ResultDo::getTimeStamp)).orElse(null);
    }

    /**
     * 获取第一个执行结果
     */
    public Object lastResult(){
        if(result == null || result.isEmpty()){
            return null;
        }
        return result.values().stream().max(Comparator.comparing(ResultDo::getTimeStamp)).orElse(null);
    }

    /**
     * 判断执行是否成功
     */
    public boolean isSuccess(){
        if(result == null){
            return Boolean.TRUE;
        }
        return result.values().stream()
                .noneMatch(m -> m.getAction() != null
                        && m.getAction() != Action.EXECUTE_SUCCESS);
    }

    /**
     * 通过sort排序Result
     */
    public List<ResultDo<?>> sortResult(){
        if(result == null){
            return null;
        }
        return result.values().stream()
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ResultDo::getSort))
                .collect(Collectors.toList());
    }

    /**
     * 获取code大于0的Result
     */
    public List<ResultDo<?>> thanZeroCodeList(){
        if(result == null){
            return null;
        }
        return result.values().stream()
                .filter(Objects::nonNull)
                .filter(f->f.getCode() > 0)
                .collect(Collectors.toList());
    }

    public static <M> ResultDo<M> ofSuccess(M m){
        return new ResultDo<M>(Action.EXECUTE_SUCCESS, m, 0);
    }

    public static <M> ResultDo<M> ofFailed(M m){
        return new ResultDo<M>(Action.EXECUTE_FAILED, m, 0);
    }

    public static <M> ResultDo<M> ofLater(M m){
        return new ResultDo<M>(Action.EXECUTE_LATER, m, 0);
    }

    public static <M> ResultDo<M> ofException(M m){
        return new ResultDo<M>(Action.EXECUTE_EXCEPTION, m, 0);
    }

    public static <T> ExchangeModel<T> of(T t){
        ExchangeModel<T> exchange = new ExchangeModel<>();
        exchange.setInput(t);
        return exchange;
    }

    @Override
    public String toString() {
        return "ExchangeModel{" +
                "result=" + result +
                '}';
    }
}
