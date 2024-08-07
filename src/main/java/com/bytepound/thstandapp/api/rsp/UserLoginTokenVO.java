package com.bytepound.thstandapp.api.rsp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginTokenVO implements Serializable {
    private static final long serialVersionUID = -3650849331833462416L;
    private UserLoginVO loginVO;
    private String nickName;
    private String avatarUrl;
    private String token;
}
