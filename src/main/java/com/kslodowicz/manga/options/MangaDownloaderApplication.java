package com.kslodowicz.manga.options;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.kslodowicz.manga.controller.MainWorkController;

@SpringBootApplication
@ComponentScan("com.kslodowicz.manga")
public class MangaDownloaderApplication {

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext run = SpringApplication.run(MangaDownloaderApplication.class, args);
		run.getBean(MainWorkController.class).start();
		
	}
	
	
}
