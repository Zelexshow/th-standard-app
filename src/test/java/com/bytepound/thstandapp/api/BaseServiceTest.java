package com.bytepound.thstandapp.api;


import com.bytepound.thstandapp.TestApplication;
import com.bytepound.thstandapp.business.repository.mysql.dao.UserLogInInfoDOMapper;
import com.bytepound.thstandapp.business.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestApplication.class)
public class BaseServiceTest {

    @Resource
    protected UserLogInInfoDOMapper userLogInInfoDOMapper;
    @Resource
    protected UserService userService;

}
