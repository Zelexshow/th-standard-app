package com.bytepound.thstandapp.api;


import com.bytepound.thstandapp.api.req.UserCreateParam;
import com.bytepound.thstandapp.api.rsp.UserCreationVO;
import com.bytepound.thstandapp.business.repository.mysql.po.user.UserLogInInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@Slf4j
public class UserServiceTest extends BaseServiceTest {

    @Test
    public void testCreateUser() {
        UserCreateParam userCreateParam = new UserCreateParam();
        userCreateParam.setAvatarUrl("www.baidu.com");
        userCreateParam.setMale(false);
        userCreateParam.setWechatOpenId("testOpenId");
        userCreateParam.setNickName("tomCat");

        UserCreationVO userVO = userService.createUserFromWechat(userCreateParam);

        String userId = userVO.getUserId();
        UserLogInInfoDO userLogInInfoDO = userLogInInfoDOMapper.selectById(Long.parseLong(userId));
        log.info("查询到了新生成的User：{}", userLogInInfoDO);
        Assertions.assertTrue(Objects.nonNull(userLogInInfoDO));



    }
    public void testCreateUserTest() {
        UserLogInInfoDO userLogInInfoDO = userLogInInfoDOMapper.selectById(1L);
        System.out.println(userLogInInfoDO);
        System.out.println("sdfsdfs");
    }
}
