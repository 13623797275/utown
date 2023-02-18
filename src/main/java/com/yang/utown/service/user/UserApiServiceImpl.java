package com.yang.utown.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.utown.data.TestData;
import com.yang.utown.interfaces.ApiName;
import com.yang.utown.mapper.UserCaptchaMapper;
import com.yang.utown.pojo.EnvProperties;
import com.yang.utown.pojo.HttpResult;
import com.yang.utown.pojo.UserCaptcha;
import com.yang.utown.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.Assert;

import java.util.Random;

/**
 * @author yhw
 * @date 2023/2/14 17:52
 * @remark
 */
@Service
public class UserApiServiceImpl implements UserApiService {
	@Autowired
	private EnvProperties envProperties;

	/**
	 * 发送验证码并获取
	 * @return
	 */
	public  String sendCaptcha() {
		String url = envProperties.getTest_host() + ApiName.User_SendCaptcha;
		String param = "{\"captchaType\":\"SIGNUP_MOBILE\",\"identifier\":\"" + TestData.phoneAccount + "\"}";
		HttpResult httpResult = HttpUtils.doPost(url, param);
		JSONObject json = JSON.parseObject(httpResult.getResultBody());
		String captcha = json.getString("code");
		return captcha;

	}

	/**
	 * 校验验证码
	 * @return
	 */
	public String checkCaptcha(){
		String url = envProperties.getTest_host() + ApiName.User_CheckCaptcha;
		String captcha = sendCaptcha();
		String param = "{\"captchaType\":\"SIGNUP_MOBILE\",\"identifier\":\"" + TestData.phoneAccount + "\",\"captcha\":\"" + captcha + "\"}";
		HttpResult httpResult = HttpUtils.doPost(url, param);
		JSONObject json = JSON.parseObject(httpResult.getResultBody());
		String signupStatus = json.getString("signupStatus");
		return signupStatus;
	}

	/**
	 * 注册流程设置密码并获取token
	 * @return
	 */
	public String signupToken(){
		String url = envProperties.getTest_host() + ApiName.User_Signup;
		String param = "{\"identityType\":\"PHONE\",\"identifier\":\"" + TestData.phoneAccount + "\",\"password\":\"11111111\"}";
		HttpResult httpResult = HttpUtils.doPost(url, param);
		JSONObject json = JSON.parseObject(httpResult.getResultBody());
		String accessToken = json.getString("accessToken");
		TestData.token.put("accessToken", "Bearer "+accessToken);
		return accessToken;
	}
}
