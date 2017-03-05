import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main {

	private static final Integer STARTING_CHAPTER = 1;
	private static final Integer ENDING_CHAPTER = 148;
	private static final String OUTPUT_FOLDER = "C:\\Users\\Wookie\\Downloads\\Manga";
	private static final String URL_BASE = "http://images.mangafreak.net/Download/Ubel_Blatt_<number>";
	private static final String MANGA_NAME = "Ubel Blatt";

	public static void main(String[] args) throws Exception {
		File outputFolder = new File(OUTPUT_FOLDER + File.separator + MANGA_NAME);
		if (!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		for (int i = STARTING_CHAPTER; i <= ENDING_CHAPTER; i++) {
			String url = URL_BASE.replace("<number>", String.valueOf(i));
			String fileName = outputFolder.getAbsolutePath() + File.separator + MANGA_NAME + "_" + i + ".zip";
			downloadFile(url, fileName);
		}

	}

	private static void downloadFile(String url, String outputFile) throws Exception {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(outputFile);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
	}
}
