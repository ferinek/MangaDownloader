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
import org.springframework.stereotype.Service;

import com.kslodowicz.manga.utils.ThreadUtils;

@Service
public class MangaDownloaderService {
	private static final String DOWNLOAD_PAGE_MANGA_FREAK = "http://www3.mangafreak.net/Manga/Rosario_Vampire";
	private static final String DOWNLOAD_PAGE_MANGA_FOX = "http://mangafox.me/manga/????/";
	@Autowired
	private WebDriver driver;

	public void dowloadAllMangaFromMangaFreak() {
		driver.get(DOWNLOAD_PAGE_MANGA_FREAK);
		ThreadUtils.sleep(5);
		System.out.println("szukam linkow");
		List<WebElement> elements = driver.findElements(By.cssSelector("a[rel='NOFOLLOW']"));
		for (WebElement element : elements) {
			element.click();
			ThreadUtils.sleep(1);
		}
	}

	public void dowloadAllMangaFromMangaFox() {
		System.out.println("Loading Page");

		driver.get(DOWNLOAD_PAGE_MANGA_FOX);
		System.out.println("Page Loaded, expanding chapters to find first");
		runJsScript("$('#chapters').show()");
		runJsScript("$('.expand').click()");
		List<WebElement> elements = driver.findElements(By.cssSelector("a[class='tips']"));
		WebElement firstChapter = elements.get(elements.size() - 1);
		System.out.println("First Chapter Found");
		firstChapter.click();
		boolean koniec = true;
		while (koniec) {
			if (driver.getCurrentUrl().equals(DOWNLOAD_PAGE_MANGA_FOX)) {
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
	}

	private String getPage() {
		String currentUrl = driver.getCurrentUrl();
		String[] split = currentUrl.split("/");
		if (split.length < 8) {
			return null;
		}
		String lastPart = split[split.length - 1];
		return getStandarizedNumber(lastPart.substring(0, lastPart.length() - 5));
	}

	private String getChapter() {
		String currentUrl = driver.getCurrentUrl();
		String[] split = currentUrl.split("/");
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
			Files.copy(in, Paths.get("D:/Manga/" + chapter + "/" + page + ".jpg"));
		} catch (Exception e) {
			System.out.println("sciaganie obrazkow sie zjebalo" + e);
		}
	}

	private void makeDir(String chapter) {
		File file = new File("D:/Manga/" + chapter);
		if (!file.exists()) {
			file.mkdir();
		}

	}

	private String getStandarizedNumber(String number) {
		for (int i = number.length(); i < 4; i++) {
			number = "0" + number;
		}
		return number;
	}
}
