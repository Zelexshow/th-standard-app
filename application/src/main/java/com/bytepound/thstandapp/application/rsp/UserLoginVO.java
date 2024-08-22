package com.bytepound.thstandapp.application.rsp;

import com.bytepound.thstandapp.common.model.login.IpLocationInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLoginVO implements Serializable {
    private Integer loginType;
    private Long userId;
    private Date loginTime;
    private IpLocationInfo locationInfo;
}
