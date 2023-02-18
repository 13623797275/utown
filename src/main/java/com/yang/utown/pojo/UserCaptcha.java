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
 * @TableName user_captcha
 */
@TableName(value ="user_captcha")
@Data
public class UserCaptcha implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 邮箱,手机号等
     */
    private String identifier;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 0:email reg,1:mobile reg,2:email auth,3:mobile auth,4:forget password email
     */
    @TableField("captcha_type")
    private Byte captchaType;

    /**
     * 0:未使用,1:已使用
     */
    private Byte status;

    /**
     * 
     */
    @TableField("share_code")
    private String shareCode;

    /**
     * 0-总统,1-1010,2-im
     */
    @TableField("share_source")
    private Byte shareSource;

    /**
     * 验证码有效期
     */
    @TableField("expire_time")
    private Date expireTime;

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