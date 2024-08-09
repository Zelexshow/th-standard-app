package com.bytepound.thstandapp.application.api;

import com.bytepound.thstandapp.application.TestApplication;
import com.bytepound.thstandapp.application.service.UserService;
import com.bytepound.thstandapp.business.repository.mysql.dao.UserLogInInfoDOMapper;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
public class BaseServiceTest {

    @Resource
    protected UserLogInInfoDOMapper userLogInInfoDOMapper;
    @Resource
    protected UserService userService;

}
