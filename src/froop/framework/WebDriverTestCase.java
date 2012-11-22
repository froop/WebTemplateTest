package froop.framework;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverTestCase {
	protected static final String DOWNLOAD_DIR =
			System.getProperty("java.io.tmpdir");

	protected static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = createFirefoxDriver();
	}

	private static WebDriver createFirefoxDriver() {
		FirefoxProfile prop = new FirefoxProfile();
		prop.setPreference("browser.download.folderList", 2);
		prop.setPreference("browser.download.useDownloadDir", true);
		prop.setPreference("browser.download.dir", DOWNLOAD_DIR);
		prop.setPreference("browser.helperApps.neverAsk.saveToDisk",
				"application/octet-stream");
		return new FirefoxDriver(prop);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.quit();
	}

	protected static void waitPage(final String url) {
		(new WebDriverWait(driver, 2)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return driver.getCurrentUrl().startsWith(url);
			}
		});
	}

	protected static void assertInputValue(String name, String value) {
		WebElement input = driver.findElement(By.name(name));
		assertThat(input.getAttribute("value"), is(value));
	}

	protected static void assertClassText(String tagClass, String text) {
		List<WebElement> elements = driver.findElements(By.className(tagClass));
		WebElement element = null;
		try {
			element = elements.get(0);
		} catch (IndexOutOfBoundsException e) {
			fail("Not Exists " + tagClass + "=" + text);
		}

		assertThat(element.getText(), is(text));
	}
}
