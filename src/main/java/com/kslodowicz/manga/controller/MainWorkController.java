package com.kslodowicz.manga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kslodowicz.manga.service.MangaFoxDownloaderService;
import com.kslodowicz.manga.service.MangaFreakDownloaderService;

@Component
public class MainWorkController {
	@Autowired
	private MangaFoxDownloaderService mangafox;
	@Autowired
	private MangaFreakDownloaderService mangafreak;

	@Value("${manga.source}")
	private String mangaSource;
	@Value("${manga.link}")
	private String mangaLink;
	@Value("${manga.name}")
	private String mangaName;
	@Value("${manga.savepath}")
	private String mangaSavePath;

	public void start() {

		switch (mangaSource) {
		case "mangafox":
			mangafox.dowloadAllMangaFromMangaFox();
			break;
		case "mangafreak":
			mangafreak.dowloadAllMangaFromMangaFreak();
			break;
		default:
			System.out.println("Unsupported manga source. Please contact with developer.");
		}
	};

}
