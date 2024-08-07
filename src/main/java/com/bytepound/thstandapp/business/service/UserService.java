package com.bytepound.thstandapp.business.service;


import com.bytepound.thstandapp.api.req.UserCreateParam;
import com.bytepound.thstandapp.api.rsp.UserCreationVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    UserCreationVO createUserFromWechat(@Valid UserCreateParam userCreateParam);
}
