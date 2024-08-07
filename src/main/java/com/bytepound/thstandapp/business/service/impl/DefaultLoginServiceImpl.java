package com.bytepound.thstandapp.business.service.impl;

import cn.hutool.json.JSONObject;
import com.bytepound.thstandapp.api.rsp.UserLoginTokenVO;
import com.bytepound.thstandapp.api.rsp.UserLoginVO;
import com.bytepound.thstandapp.business.model.constant.ErrorCode;
import com.bytepound.thstandapp.business.model.constant.ExtInfoConstant;
import com.bytepound.thstandapp.business.model.constant.LoginType;
import com.bytepound.thstandapp.business.model.login.IpLocationInfo;
import com.bytepound.thstandapp.business.model.login.JWTPayload;
import com.bytepound.thstandapp.business.model.login.JWTUtils;
import com.bytepound.thstandapp.business.repository.mysql.dao.UserLogInInfoDOMapper;
import com.bytepound.thstandapp.business.repository.mysql.po.user.UserLogInInfoDO;
import com.bytepound.thstandapp.business.service.LoginService;
import com.bytepound.thstandapp.business.util.DBUtils;
import com.bytepound.thstandapp.business.util.IDGenerator;
import com.bytepound.thstandapp.business.util.SnowFlakeIDGenerator;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DefaultLoginServiceImpl implements LoginService {

    private final IDGenerator idGenerator = new SnowFlakeIDGenerator(0, 0);
    private static final Long EXPIRE_RANGE = 48 * 60 * 60 * 1000L;

    @Resource
    private UserLogInInfoDOMapper userLogInInfoDOMapper;

    @Resource
    private DBUtils dbUtils;

    @Override
    public UserLoginTokenVO login(String locationInfo, Integer loginType, String accountId) {
        Date now = new Date();
        LoginType typeEn = LoginType.valueOf(loginType);
        UserLogInInfoDO loginInfoDO;
        switch (typeEn) {
            //1.先拿loginDO 2.再拿basicDO
            case WECHAT_LOGIN -> {
                Map<String, Object> conditionMap = new HashMap<>() {
                    {
                        put("account_id", accountId);
                    }
                };
                List<UserLogInInfoDO> loginInfoDOS = userLogInInfoDOMapper.selectByMap(conditionMap);
                if (loginInfoDOS.isEmpty()) {
                    // 用户不存在，则直接注册用户
                    log.info("start register user: loginType: {}, accountId: {}", loginType, accountId);
                    return createDefaultUserIfNotRegistered(locationInfo, accountId, loginType);
                }
                loginInfoDO = loginInfoDOS.get(0);
                // 更新一下登录时间
                loginInfoDO.setLastLoginTime(now);
                break;
            }
            // 通过userId
            case APP_ID_LOGIN -> {
                long userId = Long.parseLong(accountId);
                loginInfoDO = userLogInInfoDOMapper.selectById(userId);
                if (loginInfoDO == null) {
                    // 不存在则注册用户
                    log.info("start register user: loginType: {}, accountId: {}", loginType, accountId);
                    return createDefaultUserIfNotRegistered(locationInfo, accountId, loginType);
                }
                break;
            }
            default -> throw ErrorCode.SERVICE_INVALID_LOGIN_PARAM.buildBizException();
        }

        // 更新login db中的字段信息
        updateLoginStatus(locationInfo, loginInfoDO, now);
        UserLoginVO userLoginVO = getUserLoginVO(locationInfo, loginType, loginInfoDO, now);
        // 生成token
        JWTPayload<UserLoginVO> payload = JWTPayload.<UserLoginVO>builder().body(userLoginVO)
                .timestamp(now.getTime())
                .expiration(now.getTime() + EXPIRE_RANGE)
                .build();
        String token = JWTUtils.generateTokenByHMAC(ExtInfoConstant.gson.toJson(payload), JWTUtils.SECRET);
        UserLoginTokenVO userLoginTokenVO = new UserLoginTokenVO();
        userLoginTokenVO.setToken(token);
        userLoginTokenVO.setLoginVO(userLoginVO);
        userLoginTokenVO.setNickName(loginInfoDO.getNickname());
        userLoginTokenVO.setAvatarUrl(loginInfoDO.getAvatarUrl());
        return userLoginTokenVO;
    }

    private void updateLoginStatus(String locationInfo, UserLogInInfoDO loginInfoDO, Date now) {
        String extInfo = loginInfoDO.getExtInfo();
        JsonObject extInfoJs = ExtInfoConstant.gson.fromJson(extInfo, new TypeToken<JsonObject>(){}.getType());
        if (extInfoJs == null) {
            extInfoJs = new JsonObject();
        }
        extInfoJs.addProperty(ExtInfoConstant.KEY_LOGIN_LOCATION, locationInfo);
        // update do状态
        loginInfoDO.setLastLoginTime(now);
        loginInfoDO.setExtInfo(ExtInfoConstant.gson.toJson(extInfoJs));
        loginInfoDO.setGmtModTime(now);

        // 入库
        dbUtils.checkAffectedRows(
                () -> userLogInInfoDOMapper.updateById(loginInfoDO), 1, ErrorCode.SERVICE_UPDATE_FAIL
        );

    }


    public static void main(String[] args) {
        String extInfo = "{\"login_location_info\":\"0|0|0|内网IP|内网IP\"}";

        Gson gson = new Gson();
        try {
            JsonObject extInfoJs = gson.fromJson(extInfo, new TypeToken<JsonObject>(){}.getType());
            System.out.println("Parsed JSON: " + extInfoJs);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }


    private UserLoginTokenVO createDefaultUserIfNotRegistered(String locationInfo, String accountId, Integer loginType) {

        UserLogInInfoDO userLoginInfoDO = new UserLogInInfoDO();
        userLoginInfoDO.setLoginType(loginType);
        userLoginInfoDO.setAccountId(accountId);
        //填充额外信息
        Date now = new Date();
        userLoginInfoDO.setDelete(false);
        userLoginInfoDO.setGmtCreateTime(now);
        userLoginInfoDO.setGmtModTime(now);
        userLoginInfoDO.setLastLoginTime(now);
        Long loginInfoId = idGenerator.nextId();
        userLoginInfoDO.setId(loginInfoId);


        dbUtils.checkAffectedRows(
                () -> userLogInInfoDOMapper.insert(userLoginInfoDO), 1, ErrorCode.SERVICE_INSERT_RECORD_FAIL
        );
        UserLoginVO userLoginVO = getUserLoginVO(locationInfo, loginType, userLoginInfoDO, now);
        // 生成token
        JWTPayload<UserLoginVO> payload = JWTPayload.<UserLoginVO>builder().body(userLoginVO)
                .timestamp(now.getTime())
                .expiration(now.getTime() + EXPIRE_RANGE)
                .build();
        String token = JWTUtils.generateTokenByHMAC(ExtInfoConstant.gson.toJson(payload), JWTUtils.SECRET);
        UserLoginTokenVO userLoginTokenVO = new UserLoginTokenVO();
        userLoginTokenVO.setToken(token);
        userLoginTokenVO.setLoginVO(userLoginVO);
        userLoginTokenVO.setNickName(userLoginInfoDO.getNickname());
        userLoginTokenVO.setAvatarUrl(userLoginInfoDO.getAvatarUrl());
        return userLoginTokenVO;
    }

    /**
     * 获取UserLoginVO
     */
    private UserLoginVO getUserLoginVO(String locationInfo, Integer loginType, UserLogInInfoDO basicInfoDO, Date now) {
        IpLocationInfo ipLocationInfo = genIpLocationInfo(locationInfo);
        UserLoginVO retVO = new UserLoginVO();
        retVO.setLoginTime(now);
        Long userId = basicInfoDO.getId();
        retVO.setUserId(userId);
        retVO.setLocationInfo(ipLocationInfo);
        retVO.setLoginType(loginType);
        return retVO;
    }

    private IpLocationInfo genIpLocationInfo(String locationInfo) {
        String[] locationStrs = locationInfo.split("\\|");
        IpLocationInfo ipLocationInfo = new IpLocationInfo();
        ipLocationInfo.setCountry(locationStrs[0]);
        ipLocationInfo.setProvince(locationStrs[2]);
        ipLocationInfo.setCity(locationStrs[3]);
        ipLocationInfo.setIsp(locationStrs[4]);
        return ipLocationInfo;
    }
}
