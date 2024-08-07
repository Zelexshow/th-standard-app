package com.bytepound.thstandapp.api.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 微信注册参数
 */
@Data
@ToString
public class UserCreateParam implements Serializable {

    private static final long serialVersionUID = -7478803008766597630L;
    /**
     * 微信登录开放id
     */
    @NotEmpty(message = "wechatOpenId can not be empty")
    private String wechatOpenId;

    /**
     * 这里暂时使用微信昵称
     */
    private String nickName;

    private String avatarUrl;

    private boolean male;



}
