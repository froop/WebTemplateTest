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
		assertInputValue("user", "user1");
		assertInputValue("password", "");
	}

	@Test
	public void testRedirectByNotLogin() throws Exception {
		logout();

		driver.get(BASE_URL + "sample/");

		assertThat(driver.getCurrentUrl(), is(LOGIN_URL));
	}

	@Test
	public void testRedirectOriginUrl() throws Exception {
		final String originUrl = BASE_URL + "sample/summary.html";

		loginWithOriginUrl(originUrl);

		assertThat(driver.getCurrentUrl(), is(originUrl));
	}

	@Test
	public void testClearOriginUrl() throws Exception {
		loginWithOriginUrl(BASE_URL + "sample/summary.html");

		login("user1", "password1");

		assertThat(driver.getCurrentUrl(), is(BASE_URL));
	}

	private void loginWithOriginUrl(final String originUrl) {
		logout();
		driver.get(originUrl);
		inputLogin("user1", "password1");
	}
}
