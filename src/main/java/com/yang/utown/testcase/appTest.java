package com.yang.utown.testcase;

import com.yang.utown.base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * @author yhw
 * @date 2023/2/16 19:10
 * @remark
 */
public class appTest extends BaseTest {
	@Test
	public void test1() throws MalformedURLException {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("deviceName", "M2007J1SC");
		caps.setCapability("platformName", "Android");
		caps.setCapability("platformVersion", "12");
		caps.setCapability("appPackage", "io.jagat.main");
		caps.setCapability("appActivity", "io.utown.unity.UTownPlayerActivity");
		caps.setCapability("noReset", false);
		caps.setCapability("unicodeKeyboard", true);
		caps.setCapability("resetKeyboard", true);

		URL url = new URL("http://127.0.0.1:4723/wd/hub");
		AndroidDriver driver = new AndroidDriver(url, caps);
		Duration duration = Duration.ofSeconds(15);
		driver.manage().timeouts().implicitlyWait(duration);
		driver.findElement(By.id("io.jagat.main:id/login_start_more_options")).click();
		driver.findElement(By.id("io.jagat.main:id/dialog_tv_to_login")).click();
		driver.findElement(By.id("io.jagat.main:id/login_input_email_edit")).click();
		driver.findElement(By.xpath("//*[@resource-id='io.jagat.main:id/login_input_email_edit']/android.widget.RelativeLayout/android.widget.EditText"))
				.sendKeys("beta01@mail163.cf");
		driver.findElement(By.id("io.jagat.main:id/login_input_password_edit")).click();
		driver.findElement(By.xpath("//*[@resource-id='io.jagat.main:id/login_input_password_edit']/android.widget.RelativeLayout/android.widget.EditText"))
				.sendKeys("11111111");
		driver.findElement(By.id("io.jagat.main:id/login_password_next_tv")).click();
	}


}
