package com.bytepound.thstandapp.business.repository.mysql.po.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytepound.thstandapp.business.repository.mysql.po.CommonDO;
import lombok.Data;

import java.util.Date;

@TableName("t_user_login_info")
@Data
public class UserLogInInfoDO extends CommonDO<Long> {

    /**
     * -------------登录信息----------------
     */
    /**
     * 0: id登录
     * 1：微信登录
     * 2: 电话号码
     * 3: 账密登录
     */
    private Integer loginType;
    /**
     * 登录账号：可以是微信凭证，也可以是电话号码
     */
    private String accountId;

    /**
     * 密码，适用于账密登录
     */
    private String passwordEncr;

    /**
     * 额外信息，包括ip信息
     */
    private String extInfo;

    private Date lastLoginTime;

    /**
     *------------------基本信息必填字段-----------------
     */

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 是否认证
     */
    private boolean verified;

    /**
     * 性别
     */
    private boolean male;
}
