package com.yang.utown.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.yang.utown.pojo.AuthToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Entity com.yang.utown.pojo.AuthToken
 */
public interface AuthTokenMapper extends BaseMapper<AuthToken> {
	AuthToken selectAccessToken(@Param("identifier") String identifier);

}




