package com.example.java.vo;

public class ObjectRESTResult {
    protected int statusCode;
    protected Object returnObj;
    protected String msg;

    public int getStatusCode() {
        return statusCode;
    }

    public Object getReturnObj() {
        return returnObj;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setReturnObj(Object returnObj) {
        this.returnObj = returnObj;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
