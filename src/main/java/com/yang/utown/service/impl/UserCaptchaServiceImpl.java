package com.yang.utown.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.utown.pojo.UserCaptcha;
import com.yang.utown.service.UserCaptchaService;
import com.yang.utown.mapper.UserCaptchaMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@DS("master")
public class UserCaptchaServiceImpl extends ServiceImpl<UserCaptchaMapper, UserCaptcha>
    implements UserCaptchaService{

}




