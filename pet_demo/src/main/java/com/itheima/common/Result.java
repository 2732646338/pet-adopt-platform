package com.itheima.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    private Result(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    //成功带数据
    public static <T> Result<T> success(T data){
        return new Result<>(200, "成功", data);
    }
    //成功无数据
    public static <T> Result<T> success(){
        return new Result<>(200, "成功", null);
    }

    //失败自定义状态码和消息
    public static <T> Result<T> error(Integer code, String msg){
        return new Result<>(code, msg, null);
    }
    //失败默认状态码
    public static <T> Result<T> error(String msg){
        return new Result<>(500, msg, null);
    }
}
