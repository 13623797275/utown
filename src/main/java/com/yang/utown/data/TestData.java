package com.yang.utown.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author yhw
 * @date 2023/2/15 20:12
 * @remark
 */
public class TestData {
	public static String phoneAccount = getAccount();
	public static String mailAccount = getAccount();
	public static HashMap<String,String> token = new HashMap<String,String>();



	/**
	 * 生成手机号
	 * @return
	 */
	static  String getAccount(){
		Random random = new Random();
		int i = random.nextInt(100000);
		String account = "62-"+i;
		return account;
	}


}
