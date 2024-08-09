package com.bytepound.thstandapp.application.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginParam implements Serializable {
    private static final long serialVersionUID = 1708775099610976208L;
    @NotNull(message = "loginType can not be null")
    private Integer loginType;
    @NotEmpty(message = "accountId can not be empty")
    private String accountId;


}
