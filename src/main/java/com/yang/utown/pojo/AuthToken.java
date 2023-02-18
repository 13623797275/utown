package com.yang.utown.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName auth_token
 */
@TableName(value ="auth_token")
@Data
public class AuthToken implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String guid;

    /**
     * 
     */
    @TableField("login_ip")
    private Long loginIp;

    /**
     * 
     */
    @TableField("refresh_ip")
    private Long refreshIp;

    /**
     * 设备id
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * Access Token
     */
    @TableField("access_token")
    private String accessToken;

    /**
     * Refresh Token
     */
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * Refresh Token过期时间
     */
    @TableField("access_expire_time")
    private Date accessExpireTime;

    /**
     * Refresh Token过期时间
     */
    @TableField("refresh_expire_time")
    private Date refreshExpireTime;

    /**
     * 
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 
     */
    @TableField("update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}