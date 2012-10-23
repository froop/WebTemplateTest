package froop.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginTest extends SampleBaseTestCase {

	@Test
	public void testLoginSuccess() throws Exception {
		login("user1", "password1");

		assertThat(driver.getCurrentUrl(), is(BASE_URL));
	}

	@Test
	public void testLoginFailure() throws Exception {
		login("user1", "password2");

		assertThat(driver.getCurrentUrl(), is(LOGIN_URL));
		WebElement errorDiv = driver.findElement(By.id("error"));
		assertThat(errorDiv.getText(), is("ログインが拒否されました"));
		WebElement userText = driver.findElement(By.name("user"));
		assertThat(userText.getAttribute("value"), is("user1"));
		WebElement passText = driver.findElement(By.name("password"));
		assertThat(passText.getAttribute("value"), is(""));
	}

	@Test
	public void testRedirectByNotLogin() throws Exception {
		logout();

		driver.get(BASE_URL + "sample/");

		assertThat(driver.getCurrentUrl(), is(LOGIN_URL));
	}
}
