package com.yang.utown.mapper;
import org.apache.ibatis.annotations.Param;

import com.yang.utown.pojo.UserCaptcha;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.yang.utown.pojo.UserCaptcha
 */
public interface UserCaptchaMapper extends BaseMapper<UserCaptcha> {
	UserCaptcha selectCaptcha(@Param("identifier") String identifier);

}




