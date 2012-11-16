package froop.sample;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import froop.framework.WebDriverTestCase;

public class SampleBaseTestCase extends WebDriverTestCase {
	protected static final String BASE_URL = "http://localhost:18080/sample/";
	protected static final String LOGIN_URL = BASE_URL + "login.html";
	protected static final String SAMPLE_URL = BASE_URL + "sample/";
	protected static final String SUMMARY_URL = SAMPLE_URL + "summary.html";

	@Before
	public void setUp() throws Exception {
		login("user1", "password1");
		clearAllItems();
	}

	@After
	public void tearDown() throws Exception {
		logout();
	}

	protected void login(String user, String password) {
		driver.get(LOGIN_URL);
		inputLogin(user, password);
	}

	protected void inputLogin(String user, String password) {
		inputUser(user);
		inputPassword(password);
		clickLoginButton();
	}

	private void inputUser(String user) {
		WebElement userText = driver.findElement(By.name("user"));
		userText.clear();
		userText.sendKeys(user);
	}

	private void inputPassword(String password) {
		WebElement passText = driver.findElement(By.name("password"));
		passText.clear();
		passText.sendKeys(password);
	}

	private void clickLoginButton() {
		WebElement loginButton = driver.findElement(By.name("login"));
		loginButton.click();
	}

	protected void logout() {
		driver.get(BASE_URL + "logout");
	}

	private static void clearAllItems() throws Exception {
		while (true) {
			driver.get(SAMPLE_URL);
			List<WebElement> elements = driver.findElements(By
					.className("item-link"));
			if (elements.size() == 0) {
				return;
			}
			elements.get(0).click();

			driver.findElement(By.name("disabled")).click();
			driver.findElement(By.name("register")).click();
		}
	}
}
