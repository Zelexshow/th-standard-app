package com.bytepound.thstandapp.application.service;


import com.bytepound.thstandapp.application.req.UserCreateParam;
import com.bytepound.thstandapp.application.rsp.UserCreationVO;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
public interface UserService {
    UserCreationVO createUserFromWechat(@Valid UserCreateParam userCreateParam);
}
