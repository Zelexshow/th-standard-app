package com.bytepound.thstandapp.api.rsp;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserCreationVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8263064571583123201L;

    private Integer createType;
    private String accountId;
    private String userId;

    public static UserCreationVO buildFrom(Integer createType, String wxOpenId, String userId) {
        UserCreationVO vo = new UserCreationVO();
        vo.setCreateType(createType);
        vo.setAccountId(wxOpenId);
        vo.setUserId(userId);
        return vo;
    }
}
