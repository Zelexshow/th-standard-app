package com.bytepound.thstandapp.application.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bytepound.thstandapp.application.TestApplication;
import com.bytepound.thstandapp.application.service.UserService;
import com.bytepound.thstandapp.common.repository.mysql.dao.UserLogInInfoDOMapper;
import com.bytepound.thstandapp.common.repository.mysql.po.user.UserLogInInfoDO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class BaseServiceTest {

    @Resource
    protected UserLogInInfoDOMapper userLogInInfoDOMapper;
    @Resource
    protected UserService userService;

    /**
     * 待删除的测试数据
     */
    protected List<String> tearDownedWeChatOpenIds = new ArrayList<>();

    /**
     * 删除测试数据
     */
    @AfterEach
    public void tearDownUserOpenId() {
        log.info("开始清理数据....");
        for (String weChatOpenId : tearDownedWeChatOpenIds) {
            QueryWrapper<UserLogInInfoDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account_id", weChatOpenId);
            userLogInInfoDOMapper.delete(queryWrapper);
            log.info("accountId: {} has been deleted", weChatOpenId);
        }
    }

}
