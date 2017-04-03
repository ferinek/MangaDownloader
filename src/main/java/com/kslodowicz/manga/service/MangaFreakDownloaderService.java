package com.kslodowicz.manga.service;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kslodowicz.manga.utils.ThreadUtils;

@Service
public class MangaFreakDownloaderService {
	@Value("${manga.link}")
	private String mangaLink;

	@Autowired
	private WebDriver driver;

	public void dowloadAllMangaFromMangaFreak() {
		driver.get(mangaLink);
		ThreadUtils.sleep(5);
		System.out.println("looking for links");
		List<WebElement> elements = driver.findElements(By.cssSelector("a[rel='NOFOLLOW']"));
		for (WebElement element : elements) {
			System.out.println("Clinking on element: " + element.getText());
			element.click();
			ThreadUtils.sleep(1);
		}
		System.out.println("Downloading complete. You now can turn off application.");
		
	}

}
