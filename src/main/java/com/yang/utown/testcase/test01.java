package com.yang.utown.testcase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yang.utown.base.BaseTest;
import com.yang.utown.mapper.UserCaptchaMapper;
import com.yang.utown.pojo.HttpResult;
import com.yang.utown.pojo.UserCaptcha;
import com.yang.utown.service.UserCaptchaService;
import com.yang.utown.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author yhw
 * @date 2023/2/11 15:57
 * @remark
 */
public class test01 extends BaseTest {
	@Test
	public void test1() throws IOException, URISyntaxException {
		String url = "https://eolink.o.apispace.com/phone/api/v1/forward/china/phone/attribution";
		String jsonparams ="{\"phone_number\":\"13610048587\"}";
		HashMap<String,String> headers = new HashMap<String,String>();
		headers.put("User-Agent", "userAgent");
		headers.put("X-APISpace-Token", "j4hd74a3urdwfnx1icattg5l7kx8wvgp");
		headers.put("Authorization-Type", "apikey");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone_number","13610048587");
		HttpResult httpResult = HttpUtils.doGet(url, headers,params);
		String code =  httpResult.getStatusCode();
		JSONObject jsonObject = JSON.parseObject(httpResult.getResultBody());
		String code1 = jsonObject.getString("code");
		System.out.println(code1);
		Assert.assertEquals("500", httpResult.getStatusCode());
	}
	@Test
	public void test01(){
		String url = "https://www.baidu.com/";
		HttpResult httpResult = HttpUtils.doGet(url);
		System.out.println(httpResult);
	}
	@Test
	public void test02(){
		String url = "https://api.test.utown.io:32080/public/user/send-captcha";
		Map<String,String> header = new HashMap<String,String>();
		header.put("Content-Type", "application/json");
		// header.put("Content-Type", "application/x-www-form-urlencoded");
		String jsonparams ="{\"captchaType\":\"SIGNUP_EMAIL\",\"identifier\":\"22312312@163.com\"}";
		HttpResult result = HttpUtils.doPost(url, header, jsonparams);
		System.out.println(result.getResultBody());

	}
	@Test
	public void test03() throws IOException {
		String url = "https://eolink.o.apispace.com/teladress/teladress";
		HashMap<String,String> header = new HashMap<String,String>();
		header.put("X-APISpace-Token", "j4hd74a3urdwfnx1icattg5l7kx8wvgp");
		header.put("Authorization-Type", "apikey");
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("mobile", "13286457456");
		HttpResult httpResult = HttpUtils.doPostForm(url, params, header);
		System.out.println(httpResult.getResultBody());
	}
	@Test
	public void test04() throws IOException {
		String url = "https://api.test.utown.io:32080/app/resource/file/upload-meeting-file";
		HashMap<String,String> header = new HashMap<String,String>();
		String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NzYzNjU3MjAsImV4cCI6MTY3Njk3MDUyMCwiand0X3VzZXIiOnsiZ3VpZCI6Ijg5MTM0ZmQyY2I0MzQxZGViMDA3MzNhMDRiMTljODQyIiwidXNlcklkIjoxMDAwMTg2LCJpZGVudGlmaWVyIjoiMTIzNEBxcS5jb20iLCJuaWNrbmFtZSI6IiZuYnNwIiwiYXZhdGFyIjoiaHR0cHM6Ly9jZG4udGVzdC51dG93bi5pby9pLzIwMjIxMDI0LzEvMi80LzEyNDFlMjBiNjY0ZTQxNDBhOTk3MzEzODgyYjM2Njk2LnBuZyIsImZhY2UiOiJodHRwczovL2Nkbi50ZXN0LnV0b3duLmlvL2kvMjAyMjEwMjQvYS8yL2UvYTJlMzQ2ZmJmY2YyNGYwZDkxYjU3MGMwMmVhMzdmMGQucG5nIiwiYW5vbnltb3VzIjpmYWxzZSwibGFuZyI6ImVuIiwicmVnaW9uIjoiSUQiLCJ1c2VyVHlwZSI6MH19.6mPqpdEJ_78W4GCn_h8mTXIeg-z60TwemXtFlj0lA8g";
		header.put("authorization", authorization);
		// header.put("Content-Type", "multipart/form-data");
		File file = new File("C:\\Users\\yhw\\Downloads\\11111.doc");
		HashMap<String,String> param = new HashMap<String,String>();
		param.put("meetingId","5719");
		HttpResult httpResult = HttpUtils.doPostUploadOneFile(url, file, param, header);
		System.out.println(httpResult);
	}
	@Autowired
	private UserCaptchaMapper userCaptchaMapper;
	@Test
	public void test05(){
		Random random = new Random();
		int i = random.nextInt(100000);
		String account = i+"@163.com";
		UserCaptcha userCaptcha = userCaptchaMapper.selectCaptcha("account");
		System.out.println(userCaptcha.getCaptcha());
	}
}
