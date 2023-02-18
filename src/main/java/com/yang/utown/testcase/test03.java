package com.yang.utown.testcase;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;


/**
 * @author yhw
 * @date 2023/2/16 20:38
 * @remark
 * 等待元素：
 * 1.强制等待 Threed.sleep(3000) 用在查找元素之前强制等待3秒
 * 2.隐式等待 implicitlyWait 全局等待，写在setup 的初始化方法中，每次查找元素时最长等待30 秒，在30秒内找到元素就接着执行
 *		Duration duration = Duration.ofSeconds(15);
 * 		driver.manage().timeouts().implicitlyWait(duration);
 * 3.显示等待 WebDriverWait,一般和 util配合使用，在规定的超时等待时间中元素出现就行，如果不出现就会报错
 */
public class test03 {
	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "M2007J1SC");
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "12");
		caps.setCapability("appPackage", "io.jagat.main");
		caps.setCapability("appActivity", "io.utown.unity.UTownPlayerActivity");
		caps.setCapability("noReset", true);
		caps.setCapability("unicodeKeyboard", true);
		caps.setCapability("resetKeyboard", true);

		URL url = new URL("http://127.0.0.1:4723/wd/hub");
		AndroidDriver driver = new AndroidDriver(url, caps);
		Duration duration = Duration.ofSeconds(15);
		driver.manage().timeouts().implicitlyWait(duration);
		driver.findElement(By.id("io.jagat.main:id/login_start_more_options")).click();
		driver.findElement(By.id("io.jagat.main:id/dialog_tv_to_login")).click();
		driver.findElement(By.id("io.jagat.main:id/login_input_email_edit")).click();
		driver.findElement(By.xpath("//*[@resource-id='io.jagat.main:id/login_input_email_edit']/android.widget.RelativeLayout/android.widget.EditText")).sendKeys("beta01@mail163.cf");
		driver.findElement(By.id("io.jagat.main:id/login_input_password_edit")).click();
		driver.findElement(By.xpath("//*[@resource-id='io.jagat.main:id/login_input_password_edit']/android.widget.RelativeLayout/android.widget.EditText")).sendKeys("11111111");
		driver.findElement(By.id("io.jagat.main:id/login_password_next_tv")).click();
		String result = driver.findElement(By.xpath("//*[@resource-id='io.jagat.main:id/login_input_password_edit']/android.widget.RelativeLayout/android.widget.EditText")).getText();
		System.out.println(result);
		System.out.println("哈哈哈哈");
		System.out.println("master github");
		System.out.println("push test");
		System.out.println("pull test");
	}

}
