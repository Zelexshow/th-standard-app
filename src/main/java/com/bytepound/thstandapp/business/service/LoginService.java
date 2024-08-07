package com.bytepound.thstandapp.business.service;

import com.bytepound.thstandapp.api.rsp.UserLoginTokenVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface LoginService {

    /**
     * 返回的是token
     *
     * @param locationInfo
     * @param loginType
     * @param accountId
     * @return
     */
    UserLoginTokenVO login(@NotEmpty String locationInfo, @NotNull Integer loginType, @NotNull String accountId);
}
