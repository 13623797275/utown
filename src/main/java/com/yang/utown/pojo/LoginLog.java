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
 * @TableName login_log
 */
@TableName(value ="login_log")
@Data
public class LoginLog implements Serializable {
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
     * 身份类型(0:wechat,1:google,2:apple,3:facebook,11:username,12:phone,13:email)
     */
    @TableField("identity_type")
    private Byte identityType;

    /**
     * username(local),open_id(oauth),device_id(anon)
     */
    private String identifier;

    /**
     * 
     */
    private String lang;

    /**
     * 
     */
    private String version;

    /**
     * 
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 
     */
    @TableField("device_version")
    private String deviceVersion;

    /**
     * 0:ios,1:android,2:other
     */
    @TableField("device_type")
    private Byte deviceType;

    /**
     * 0:mobile,1:web,2:pad
     */
    @TableField("client_type")
    private Byte clientType;

    /**
     * 
     */
    @TableField("login_ip")
    private Long loginIp;

    /**
     * 
     */
    @TableField("login_time")
    private Date loginTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}