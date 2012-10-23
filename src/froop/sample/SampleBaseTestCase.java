package froop.sample;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import froop.framework.WebDriverTestCase;

public class SampleBaseTestCase extends WebDriverTestCase {
	protected static final String BASE_URL = "http://localhost:18080/sample/";
	protected static final String LOGIN_URL = BASE_URL + "login.html";

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	protected void login(String user, String password) {
		driver.get(LOGIN_URL);
		WebElement userText = driver.findElement(By.name("user"));
		userText.clear();
		userText.sendKeys(user);
		WebElement passText = driver.findElement(By.name("password"));
		passText.clear();
		passText.sendKeys(password);
		WebElement loginButton = driver.findElement(By.name("login"));
		loginButton.click();
	}

	protected void logout() {
		driver.get(BASE_URL + "logout");
	}
}
