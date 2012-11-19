package froop.sample;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SampleInputTest extends SampleBaseTestCase {
	private static final String SUMMARY_URL = SAMPLE_URL + "summary.html";
	private static final String INPUT_URL = SAMPLE_URL + "input.html";

	@Before
	public void setUp() throws Exception {
		super.setUp();
		clearAllItems();
	}

	private void clearAllItems() throws Exception {
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

	@Test
	public void testInputNew() throws Exception {
		inputNew();

		assertSummary();
		moveToInputPage();
		assertInput();
	}

	private void inputNew() {
		driver.findElement(By.linkText("新規")).click();
		assertThat(driver.getCurrentUrl(), is(INPUT_URL));

		driver.findElement(By.name("text")).sendKeys("テスト1");
		driver.findElement(By.name("date")).sendKeys("20121023");
		driver.findElement(By.name("time_minute")).sendKeys("1719");
		driver.findElement(By.name("number")).sendKeys("123");
		Select selection = new Select(driver.findElement(By.name("selection")));
		selection.selectByValue("value1");
		driver.findElement(By.name("flag")).click();
		driver.findElement(By.name("register")).click();
	}

	private void assertSummary() {
		assertThat(driver.getCurrentUrl(), is(SUMMARY_URL));
		assertClassText("text", "テスト1");
		assertClassText("date", "2012/10/23");
		assertClassText("time_minute", "17:19");
		assertClassText("number", "123");
		assertClassText("selection", "選択肢1");
		assertClassText("flag", "true");
	}

	private void assertClassText(String tagClass, String text) {
		List<WebElement> elements = driver.findElements(By.className(tagClass));
		WebElement element = null;
		try {
			element = elements.get(0);
		} catch (IndexOutOfBoundsException e) {
			fail("Not Exists " + tagClass + "=" + text);
		}

		assertThat(element.getText(), is(text));
	}

	private void moveToInputPage() {
		driver.findElement(By.className("item-link")).click();
	}

	private void assertInput() {
//		assertThat(driver.getCurrentUrl(), is(INPUT_URL));
		assertInputValue("text", "テスト1");
		assertInputValue("date", "2012/10/23");
		assertInputValue("time_minute", "17:19");
		assertInputValue("number", "123");
		assertInputValue("selection", "value1");
		assertInputValue("flag", "1");
	}
}
