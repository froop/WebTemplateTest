package froop.sample;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
		File file = new File(getAbsolutePath(UPLOAD_FILE));

		upload(file, "コメント1");

		assertDownloadFile(file);
		assertInputValue("text", "コメント1");
	}

	@Test
	public void testFileSizeMax() throws Exception {
		File file = new File("work/16M上限");
		int fileSize = 16 * 1000 * 1000 - 500;
		createDummyFile(file, fileSize);

		upload(file, "");

		assertDownloadFile(file);

		file.delete();
	}

	@Test
	public void testFileSizeError() throws Exception {
		File file = new File("work/16Mオーバー");
		int fileSize = 16 * 1000 * 1000;
		createDummyFile(file, fileSize);

		upload(file, "");

		String pageSource = driver.getPageSource();
		assertTrue(pageSource.contains(" 400 "));
		assertTrue(pageSource.matches(
				".*the request was rejected because its size \\(16000...\\) " +
				"exceeds the configured maximum \\(16000000\\).*"));

		file.delete();
	}

	private static void createDummyFile(File path, int length)
			throws IOException {
		path.getParentFile().mkdir();
		RandomAccessFile file = new RandomAccessFile(path.getAbsolutePath(), "rw");
		file.setLength(length);
		file.close();
	}

	@Test
	public void testUnicode() throws Exception {
		File file = new File(getAbsolutePath("Unicode尾骶骨.txt"));

		upload(file, "尾骶骨");

		assertDownloadFile(file);
		assertInputValue("text", "尾骶骨");
	}

	@Test
	public void testHasSpace() throws Exception {
		File file = new File(getAbsolutePath("I have space.pdf"));

		upload(file, "");

		assertDownloadFile(file);
	}

	private String getAbsolutePath(String fileName) {
		try {
			URI fileURI = getClass().getResource(fileName).toURI();
			File upFile = new File(fileURI);
			return upFile.getAbsolutePath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void upload(File file, String comment) throws IOException {
		driver.get(UPLOAD_URL);
		driver.findElement(By.name("file")).sendKeys(file.getAbsolutePath());
		WebElement textElem = driver.findElement(By.name("text"));
		textElem.clear();
		textElem.sendKeys(comment);
		driver.findElement(By.name("register")).click();
	}

	private void assertDownloadFile(File expected) throws IOException {
		String actualName = expected.getName().replace('骶', '？'); //TODO
		File actual = new File(DOWNLOAD_DIR + "/" + actualName);
		actual.delete();

		driver.findElement(By.linkText(expected.getName())).click();

		assertTrue("exists", actual.exists());
		waitDowload(expected, actual);
		assertEquals("size", expected.length(), actual.length());
		assertTrue("content", FileUtils.contentEquals(expected, actual));

		actual.delete();
	}

	private void waitDowload(File expected, File actual) {
		for (int i = 0; i < 20; i++) {
			if (actual.length() >= expected.length()) {
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
