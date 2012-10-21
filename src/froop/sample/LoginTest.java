package froop.sample;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginTest extends SampleBaseTestCase {

	@Test
	public void testLoginSuccess() throws Exception {
		login("user1", "password1");

		assertEquals(BASE_URL, driver.getCurrentUrl());
	}

	@Test
	public void testLoginFailure() throws Exception {
		login("user1", "password2");

		assertEquals(LOGIN_URL, driver.getCurrentUrl());
		WebElement errorDiv = driver.findElement(By.id("error"));
		assertEquals("ログインが拒否されました", errorDiv.getText());
	}
}
