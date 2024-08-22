package com.bytepound.thstandapp.application.controller;


import com.bytepound.thstandapp.application.req.UserCreateParam;
import com.bytepound.thstandapp.application.req.UserLoginParam;
import com.bytepound.thstandapp.application.rsp.UserCreationVO;
import com.bytepound.thstandapp.application.rsp.UserLoginTokenVO;
import com.bytepound.thstandapp.common.model.BizException;
import com.bytepound.thstandapp.common.model.CommonResult;
import com.bytepound.thstandapp.application.service.LoginService;
import com.bytepound.thstandapp.application.service.UserService;
import com.bytepound.thstandapp.application.util.Ip2RegionUtil;
import com.bytepound.thstandapp.common.util.IpUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/mini/user-facade")
public class UserFacade {

    @Resource
    private UserService userService;

    @Resource
    private LoginService loginService;

    @ResponseBody
    @RequestMapping(value = "/createUserFromWX", method = RequestMethod.POST)
    public CommonResult<UserCreationVO> registerUserFromWX(
            @RequestBody UserCreateParam userCreateParam) {
        try {
            UserCreationVO userFromWechat = userService.createUserFromWechat(userCreateParam);
            return CommonResult.getSuccessResult(userFromWechat);
        } catch (BizException bizException) {
            return CommonResult.getFailureResult(bizException.getErrorCode(), bizException.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult<UserLoginTokenVO> login(
            @RequestBody UserLoginParam userLoginParam,
            HttpServletRequest httpServletRequest) {
        String ip = IpUtil.getIpAddr(httpServletRequest);
        String fullLocationInfo = Ip2RegionUtil.getLocationFromGlobalCache(ip);
        log.info("accountId:{}, start login..., ip: {}, locationInfo: {}"
                , userLoginParam.getAccountId(), ip, fullLocationInfo);
        try {
            UserLoginTokenVO loginTokenVO = loginService.login(fullLocationInfo, userLoginParam.getLoginType(), userLoginParam.getAccountId());
            return CommonResult.getSuccessResult(loginTokenVO);
        }catch (BizException bizException) {
            return CommonResult.getFailureResult(bizException.getErrorCode(), bizException.getMessage());
        }
    }
}
