package com.bytepound.thstandapp.business.model;

import java.io.Serializable;
import java.util.Arrays;

public class CommonResult<T> implements Serializable {
    private static final long serialVersionUID = 1340563394201259857L;

    protected boolean success;

    protected String errorCode;

    protected String errorMsg;

    protected T result;

    // 保存errorMsg的占位变量值，便于重新根据errorCode和语言渲染errorMsg，支持国际化
    protected Object[] arguments;

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    public CommonResult() {
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static <T> CommonResult<T> getSuccessResult(T v) {
        CommonResult<T> result = new CommonResult<T>();
        result.setSuccess(true);
        result.setResult(v);
        return result;
    }

    public static <T> CommonResult<T> getFailureResult(String errorCode, String msg) {
        CommonResult<T> result = new CommonResult<T>();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMsg(msg);
        return result;
    }

    @Override
    public String toString() {
        return "CommonResult [success=" + success + ", errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", result="
                + result + ", arguments=" + Arrays.toString(arguments) + "]";
    }
}