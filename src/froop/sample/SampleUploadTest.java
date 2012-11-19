package froop.sample;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SampleUploadTest extends SampleBaseTestCase {
	private static final String UPLOAD_URL = SAMPLE_URL + "upload.html";
	private static final String UPLOAD_FILE = "SampleUpload.txt";

	@Before
	public void setUp() throws Exception {
		super.setUp();
		clearAllItems();
	}

	private void clearAllItems() throws Exception {
		driver.get(UPLOAD_URL);
		while (true) {
			List<WebElement> elements = driver.findElements(By.linkText("delete"));
			if (elements.size() == 0) {
				return;
			}
			elements.get(0).click();
		}
	}

	@Test
	public void testUpload() throws Exception {
		inputNew();
	}

	private void inputNew() {
		driver.get(UPLOAD_URL);
		String path = getAbsolutePath(UPLOAD_FILE);
		driver.findElement(By.name("file")).sendKeys(path);
		WebElement textElem = driver.findElement(By.name("text"));
		textElem.clear();
		textElem.sendKeys("あいう");
		driver.findElement(By.name("register")).click();

		//TODO assert
	}

	protected String getAbsolutePath(String fileName) {
		try {
			URI fileURI = getClass().getResource(fileName).toURI();
			File upFile = new File(fileURI);
			return upFile.getAbsolutePath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
}
