package com.jacoffee.learning.boot.dto;

/**
 * WebApiResponse
 * @author jacoffee
 * @date 2020-07-05
 */
public class WebApiResponse<T> {

    public static final int SUCCESS = 0;

    public static final int ERROR = 1;

    private int code;

    private String msg;

    private T data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    /**
     * 成功响应
     * @param data data
     * @param <T> t
     * @return
     */
    public static <T> WebApiResponse<T> success(T data) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setCode(SUCCESS);
        response.setData(data);
        return response;
    }

    /**
     * 失败响应
     * @param errorMsg errorMsg
     * @param <T> t
     * @return
     */
    public static <T> WebApiResponse<T> failed(String errorMsg) {
        WebApiResponse<T> response = new WebApiResponse<>();
        response.setCode(ERROR);
        response.setMsg(errorMsg);
        return response;
    }

}
