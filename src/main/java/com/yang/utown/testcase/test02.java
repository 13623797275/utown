package com.yang.utown.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.utown.base.BaseTest;
import com.yang.utown.data.TestData;
import com.yang.utown.interfaces.ApiName;
import com.yang.utown.mapper.AuthTokenMapper;
import com.yang.utown.mapper.UserCaptchaMapper;
import com.yang.utown.pojo.AuthToken;
import com.yang.utown.pojo.EnvProperties;
import com.yang.utown.pojo.HttpResult;
import com.yang.utown.pojo.UserCaptcha;
import com.yang.utown.service.user.UserApiServiceImpl;
import com.yang.utown.utils.HttpUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

/**
 * @author yhw
 * @date 2023/2/15 16:33
 * @remark
 */
public class test02 extends BaseTest {
	@Autowired
	private UserCaptchaMapper userCaptchaMapper;
	@Autowired
	private UserApiServiceImpl userApiService;
	@Autowired
	private AuthTokenMapper authTokenMapper;

	@BeforeClass(description="注册流程")
	public void regsiterAccount()  {

	}

	@Test(description="测试DEMO",groups = "user1")
	@Feature("模块名称--注册模块")
	@Story("用例名称--发送验证码")
	@Description("接口返回与数据库查询验证码一致")
	public void getCaptcha(){
		String captcha = userApiService.sendCaptcha();
		UserCaptcha userCaptcha = userCaptchaMapper.selectCaptcha(TestData.phoneAccount);
		String captchaSql = userCaptcha.getCaptcha();
		Assert.assertEquals(captcha,captchaSql);
	}
	@Test(description="测试DEMO1",groups = "user")
	@Feature("模块名称--注册模块")
	@Story("用例名称--发送验证码")
	@Description("接口返回与数据库查询验证码一致")
	public void getCaptcha1(){
		String captcha = userApiService.sendCaptcha();
		UserCaptcha userCaptcha = userCaptchaMapper.selectCaptcha(TestData.phoneAccount);
		String captchaSql = userCaptcha.getCaptcha();
		Assert.assertEquals(captcha,captchaSql);
	}
	@Test
	@Feature("模块名称--注册模块")
	@Story("用例名称--校验验证码")
	@Description("校验验证码接口")
	public void login(){
		String captcha = userApiService.checkCaptcha();
		Assert.assertEquals(captcha,"SET_PASSWORD");
	}
	@Test
	@Feature("模块名称--注册模块")
	@Story("用例名称--设置密码")
	@Description("设置密码并获取token")
	public void getToken(){
		String captcha = userApiService.checkCaptcha();
		String token = userApiService.signupToken();
		AuthToken authTokens = authTokenMapper.selectAccessToken(TestData.phoneAccount);
		System.out.println(authTokens.getAccessToken());
		Assert.assertEquals(token,authTokens.getAccessToken());
	}

}
