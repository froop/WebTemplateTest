package froop.sample;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
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

	private void upload(File file, String comment) throws IOException {
		driver.get(UPLOAD_URL);
		driver.findElement(By.name("file")).sendKeys(file.getAbsolutePath());
		WebElement textElem = driver.findElement(By.name("text"));
		textElem.clear();
		textElem.sendKeys(comment);
		driver.findElement(By.name("register")).click();
	}

//	@Test
//	public void testFileSizeMax() throws Exception {
//		String fileName = "16M上限";
//		int fileSize = 16 * 1000 * 1000 - 500;
//
//		File expected = new File("work/" + fileName);
//		createDummyFile(expected.getAbsolutePath(), fileSize);
//		driver.findElement(By.name("file")).sendKeys(expected.getAbsolutePath());
//		driver.findElement(By.name("add")).click();
//		assertEquals(fileContentUrl + "edit", driver.getCurrentUrl());
//		driver.findElement(By.name("preview")).click();
//		driver.findElement(By.name("record")).click();
//
//		driver.get(userContentUrl);
//		assertDownloadFile(expected);
//
//		expected.delete();
//	}
//
//	@Test
//	public void testFileSizeError() throws Exception {
//		setupAddMode();
//
//		File file = new File("work/16Mオーバー");
//		createDummyFile(file.getAbsolutePath(), 16 * 1000 * 1000);
//		driver.findElement(By.name("file")).sendKeys(file.getAbsolutePath());
//		driver.findElement(By.name("add")).click();
//
//		assertEquals(fileContentUrl + "edit-add", driver.getCurrentUrl());
//		String error = getErrorMessage();
//		assertTrue(error, error.matches("アップロードしたファイルに問題があります " +
//				"\\(the request was rejected because its size \\(16000...\\) " +
//				"exceeds the configured maximum \\(16000000\\)\\)"));
//
//		file.delete();
//	}
//
//	private static void createDummyFile(String name, int length)
//			throws IOException {
//		RandomAccessFile file = new RandomAccessFile(name, "rw");
//		file.setLength(length);
//		file.close();
//	}
//
//	@Test
//	public void testUnicode() throws Exception {
//		String categoryName = "尾骶骨カテゴリ";
//		String fileName = "Unicode尾骶骨.txt";
//		addNormalFileContent("尾骶骨カテゴリ", Arrays.asList(fileName));
//
//		assertFileContentTop(Arrays.asList(categoryName));
//		driver.get(userContentUrl);
//		assertNewsLinks(Arrays.asList(
//				getToday() + " " + categoryName + "：" + fileName + " 更新"));
//		assertFileContentTitles(Arrays.asList(categoryName));
//		assertFileContentFiles(0, Arrays.asList(fileName));
//		assertFileUpdateDates(0, Arrays.asList(getUpdateDateText()));
//		assertDownloadFile(fileName);
//	}
//
//	@Test
//	public void testHasSpace() throws Exception {
//		String fileName = "I have space.pdf";
//		addNormalFileContent("ファイルカテゴリ１", Arrays.asList(fileName));
//
//		driver.get(userContentUrl);
//		assertNewsLinks(Arrays.asList(
//				getToday() + " ファイルカテゴリ１：" + fileName + " 更新"));
//		assertFileContentFiles(0, Arrays.asList(fileName));
//		assertDownloadFile(fileName);
//	}

	private String getAbsolutePath(String fileName) {
		try {
			URI fileURI = getClass().getResource(fileName).toURI();
			File upFile = new File(fileURI);
			return upFile.getAbsolutePath();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

//	private void assertDownloadFile(String fileName)
//			throws IOException {
//		File expected = new File(getAbsolutePath(fileName));
//		assertDownloadFile(expected);
//	}

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
