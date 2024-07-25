package com.bytepound.thstandapp.server.model.constant;

import com.bytepound.thstandapp.server.model.BizException;

import java.text.MessageFormat;

public enum ErrorCode {

    //成功
    SUCCESS(0, "成功"),
    SERVER_ERROR("999", "Server Internal Error: {0}"),
    REPEATED_REQ("001", "重入参数错误: {0}"),
    CERT_CHECK_FAIL("004", "证件号码校验未通过,描述:{0}"),
    INVALID_PARAM("005", "Invalid input msg, field: {0}, value: {1}, reason: {2}");

    private final Integer code;

    private final String msgTemplate;

    ErrorCode(int code, String msgTemplate) {
        this.code = code;
        this.msgTemplate = msgTemplate;
    }

    ErrorCode(String codeStr, String msgTemplate) {
        this.code = Integer.parseInt(codeStr);
        this.msgTemplate = msgTemplate;
    }

    public static BizException buildFitBizException(int errorCode, String msg) {
        return new BizException(errorCode, msg);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg(Object... params) {
        return MessageFormat.format(this.msgTemplate, params);
    }

    public BizException buildBizException(Object... params) {
        return new BizException(this.code, MessageFormat.format(this.msgTemplate, params));
    }
}
