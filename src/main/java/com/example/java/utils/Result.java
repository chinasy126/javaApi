package com.example.java.utils;

import java.util.HashMap;
import java.util.Map;

public class Result {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String ,Object> data = new HashMap<>();

    /**
     * 返回成功
     * @return
     */
    public static Result ok(){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setSuccess(true);
        result.setMessage("成功");
        return result;
    }

    /**
     * 返回失败
     * @return
     */
    public static Result error(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(ResultCode.ERROR);
        //result.setMessage("失败");
        return result;
    }

    /**
     * 返回信息
     * @param str 传入的内容
     * @return
     */
    public Result message(String str){
        this.setMessage(str);
        return this;
    }


    public Result data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    public Result data(Map<String ,Object> map){
        this.setData(map);
        return this;
    }



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
