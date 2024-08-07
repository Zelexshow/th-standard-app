package com.bytepound.thstandapp.business.repository.mysql.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bytepound.thstandapp.business.repository.mysql.po.user.UserLogInInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLogInInfoDOMapper extends BaseMapper<UserLogInInfoDO> {
}
