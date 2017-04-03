package com.kslodowicz.manga.service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MangaFoxDownloaderService {
	private static final String STRING = "/";
	private static final String JPG = ".jpg";
	private static final String _0 = "0";
	@Value("${manga.link}")
	private String mangaLink;
	@Value("${manga.name}")
	private String mangaName;
	@Value("${manga.savepath}")
	private String mangaSavePath;
	
	@Value("${mangafox.hasvolumes}")
	private int mangaHasVolumes;
	@Autowired
	private WebDriver driver;

	public void dowloadAllMangaFromMangaFox() {
		System.out.println("Loading Page");

		driver.get(mangaLink);
		System.out.println("Page Loaded, expanding chapters to find first");
		runJsScript("$('#chapters').show()");
		runJsScript("$('.expand').click()");
		List<WebElement> elements = driver.findElements(By.cssSelector("a[class='tips']"));
		WebElement firstChapter = elements.get(elements.size() - 1);
		System.out.println("First Chapter Found. Starting download.");
		firstChapter.click();
		boolean koniec = true;
		while (koniec) {
			if (driver.getCurrentUrl().equals(mangaLink)) {
				koniec = false;
				continue;
			}
			String page = getPage();
			if (page == null) {
				driver.findElement(By.cssSelector("a[class='btn next_page']")).click();
				continue;
			}
			WebElement image = driver.findElements(By.id("image")).get(0);
			String url = image.getAttribute("src");
			saveImage(getChapter(), page, url);
			driver.findElement(By.cssSelector("a[class='btn next_page']")).click();
		}
		System.out.println("Downloading complete. You now can turn off application.");
	}

	private String getPage() {
		String currentUrl = driver.getCurrentUrl();
		String[] split = currentUrl.split(STRING);
		if (split.length < 7+mangaHasVolumes) {
			return null;
		}
		String lastPart = split[split.length - 1];
		return getStandarizedNumber(lastPart.substring(0, lastPart.length() - 5));
	}

	private String getChapter() {
		String currentUrl = driver.getCurrentUrl();
		String[] split = currentUrl.split(STRING);
		return split[split.length - 2].substring(1);
	}

	private void runJsScript(String command) {
		JavascriptExecutor js;
		if (driver instanceof JavascriptExecutor) {
			js = (JavascriptExecutor) driver;
			js.executeScript(command);
		}

	}

	private void saveImage(String chapter, String page, String url) {
		try (InputStream in = new URL(url).openStream()) {
			makeDir(chapter);
			String pathForFile = createPathForFile(chapter, page);
			System.out.println(
					String.format("Saving image from chapter %s page %s to path %s", chapter, page, pathForFile));
			Files.copy(in, Paths.get(pathForFile));
		} catch (Exception e) {
			System.err.println(
					"Downloading file fucked up. Please ask developer for assistance. He will help you understand shitcode bellow:\n"
							+ e);
		}
	}

	private String createPathForFile(String chapter, String page) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mangaSavePath);
		stringBuilder.append(mangaName);
		stringBuilder.append(File.separatorChar);
		stringBuilder.append(chapter);
		stringBuilder.append(File.separatorChar);
		stringBuilder.append(page);
		stringBuilder.append(JPG);
		return stringBuilder.toString();
	}

	private void makeDir(String chapter) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(mangaSavePath);
		stringBuilder.append(mangaName);
		stringBuilder.append(File.separatorChar);
		stringBuilder.append(chapter);
		File file = new File(stringBuilder.toString());
		if (!file.exists()) {
			file.mkdirs();
		}

	}

	private String getStandarizedNumber(String number) {
		for (int i = number.length(); i < 4; i++) {
			number = _0 + number;
		}
		return number;
	}
}
