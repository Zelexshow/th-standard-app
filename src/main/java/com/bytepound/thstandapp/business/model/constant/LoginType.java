package com.bytepound.thstandapp.business.model.constant;

public enum LoginType {

    WECHAT_LOGIN(1),
    // 使用userLoginId
    APP_ID_LOGIN(2),
    PHONE_LOGIN(3);

    private Integer code;

    LoginType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static LoginType valueOf(Integer code) {
        for (LoginType value : LoginType.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return WECHAT_LOGIN;
    }
}
