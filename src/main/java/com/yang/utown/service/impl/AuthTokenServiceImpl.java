package com.yang.utown.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yang.utown.pojo.AuthToken;
import com.yang.utown.service.AuthTokenService;
import com.yang.utown.mapper.AuthTokenMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class AuthTokenServiceImpl extends ServiceImpl<AuthTokenMapper, AuthToken>
    implements AuthTokenService{

}




