package com.yang.utown.interfaces;

/**
 * @author yhw
 * @date 2023/2/3 19:27
 * @remark
 */
public class ApiName {
	public static final String User_SendCaptcha = "/public/user/send-captcha"; //注册发送验证码
	public static final String User_CheckCaptcha = "/public/user/check-captcha"; //校验验证码
	public static final String User_Signup = "/public/user/signup"; //设置密码
	public static final String User_Login = "/public/user/login";	//登录
	public static final String User_Update_Info = "/app/user/update-info";		//修改昵称
	public static final String User_Upload_Meeting_File="/app/resource/file/upload-meeting-file";	//会议上传文件
}
