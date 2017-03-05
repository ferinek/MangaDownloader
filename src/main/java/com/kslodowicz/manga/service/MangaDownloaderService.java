package com.kslodowicz.manga.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kslodowicz.manga.utils.ThreadUtils;

@Service
public class MangaDownloaderService {
	private static final String DOWNLOAD_PAGE = "http://www3.mangafreak.net/Manga/Rosario_Vampire";
	@Autowired
	private WebDriver driver;

	public void dowloadAllManga() {
		driver.get(DOWNLOAD_PAGE);
		ThreadUtils.sleep(5);
		System.out.println("szukam linkow");
		List<WebElement> elements = driver.findElements(By.cssSelector("a[rel='NOFOLLOW']"));
		for (WebElement element : elements) {
			element.click();
			ThreadUtils.sleep(1);
		}
	}
}
