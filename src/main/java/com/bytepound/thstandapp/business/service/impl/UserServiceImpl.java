package com.bytepound.thstandapp.business.service.impl;

import com.bytepound.thstandapp.api.req.UserCreateParam;
import com.bytepound.thstandapp.api.rsp.UserCreationVO;
import com.bytepound.thstandapp.business.model.constant.ErrorCode;
import com.bytepound.thstandapp.business.model.constant.LoginType;
import com.bytepound.thstandapp.business.repository.mysql.dao.UserLogInInfoDOMapper;
import com.bytepound.thstandapp.business.repository.mysql.po.user.UserLogInInfoDO;
import com.bytepound.thstandapp.business.service.UserService;
import com.bytepound.thstandapp.business.model.BizException;
import com.bytepound.thstandapp.business.util.DBUtils;
import com.bytepound.thstandapp.business.util.IDGenerator;
import com.bytepound.thstandapp.business.util.SnowFlakeIDGenerator;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 单机情况使用这个
     */
    private final IDGenerator idGenerator = new SnowFlakeIDGenerator(0, 0);

    @Resource
    private UserLogInInfoDOMapper userLogInInfoDOMapper;

    @Resource
    private DBUtils dbUtils;


    @Override
    public UserCreationVO createUserFromWechat(UserCreateParam userCreateParam) {
        String wechatOpenId = userCreateParam.getWechatOpenId();

        // 检查用户是否已经存在
        if (isUserExists(wechatOpenId)) {
            log.error("User with wechatOpenId {} already exists!", wechatOpenId);
            throw new BizException(ErrorCode.SERVICE_USER_RECORD_ALREADY_EXIST.getCode(),
                    ErrorCode.SERVICE_USER_RECORD_ALREADY_EXIST.getMsg());
        }

        // 创建用户登录信息对象
        UserLogInInfoDO userLogInInfoDO = buildUserLogInInfo(userCreateParam, wechatOpenId);

        // 入库
        dbUtils.checkAffectedRows(
                () -> userLogInInfoDOMapper.insert(userLogInInfoDO), 1, ErrorCode.SERVICE_INSERT_RECORD_FAIL
        );

        return UserCreationVO.buildFrom(LoginType.WECHAT_LOGIN.getCode(), wechatOpenId,
                String.valueOf(userLogInInfoDO.getId()));
    }

    /**
     * 考虑复用
     * @param wechatOpenId
     * @return
     */
    private boolean isUserExists(String wechatOpenId) {
        Map<String, Object> condition = new HashMap<>(1);
        condition.put("account_id", wechatOpenId);
        List<UserLogInInfoDO> userLoginInfoDOS = userLogInInfoDOMapper.selectByMap(condition);
        return !userLoginInfoDOS.isEmpty();
    }

    private UserLogInInfoDO buildUserLogInInfo(UserCreateParam userCreateParam, String wechatOpenId) {
        UserLogInInfoDO userLogInInfoDO = new UserLogInInfoDO();
        userLogInInfoDO.setLoginType(LoginType.WECHAT_LOGIN.getCode());
        userLogInInfoDO.setAccountId(wechatOpenId);
        userLogInInfoDO.setAvatarUrl(userCreateParam.getAvatarUrl());
        userLogInInfoDO.setNickname(userCreateParam.getNickName());
        userLogInInfoDO.setMale(false);
        userLogInInfoDO.initNow();
        userLogInInfoDO.setLastLoginTime(userLogInInfoDO.getGmtCreateTime());
        userLogInInfoDO.setId(getNextId());
        return userLogInInfoDO;
    }

    private Long getNextId() {
        return idGenerator.nextId();
    }
}
