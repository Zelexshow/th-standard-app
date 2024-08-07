package com.bytepound.thstandapp.business.model.constant;

import com.bytepound.thstandapp.business.model.BizException;

import java.text.MessageFormat;

public enum ErrorCode {



    /**
     * service layer
     */
    SERVICE_UPDATE_FAIL("2002","update fail","更新数据失败！"),
    SERVICE_USER_RECORD_ALREADY_EXIST("2004", "user already exist", "用户已存在！"),
    SERVICE_INSERT_RECORD_FAIL("2003", "insert fail","新增数据失败！"),


    SERVICE_INVALID_LOGIN_PARAM("2007", "invalid login param","无效登录参数"),
    SERVICE_INVALID_LOGIN_TOKEN("2008", "invalid login token","无效的登录token"),

    //成功
    SUCCESS("0", "success", "成功！"),
    SERVER_ERROR("999", "Server Internal Error: {0}", "系统异常！");

    private final String code;

    private final String msgTemplate;

    /**
     * 中文描述
     */
    private String descCN;

    ErrorCode(String code, String msgTemplate, String descCN) {
        this.code = code;
        this.msgTemplate = msgTemplate;
        this.descCN = descCN;
    }

    public static BizException buildFitBizException(int errorCode, String msg) {
        return new BizException(errorCode, msg);
    }

    public String getCode() {
        return this.code;
    }

    public String getMsg(Object... params) {
        return MessageFormat.format(this.msgTemplate, params);
    }

    public BizException buildBizException(Object... params) {
        return new BizException(this.code, MessageFormat.format(this.msgTemplate, params));
    }
}
