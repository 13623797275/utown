package com.yang.utown.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.utown.pojo.LoginLog;
import com.yang.utown.service.LoginLogService;
import com.yang.utown.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@DS("master")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService{

}




