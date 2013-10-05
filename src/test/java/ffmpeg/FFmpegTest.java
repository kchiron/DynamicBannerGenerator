package ffmpeg;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert.*;

import exception.UnknownOperatingSystem;


public class FFmpegTest {

	private static String ffmpegTestFolderPath;
	private static String testFolderPath;

	@BeforeClass
	public static void setUpClass() throws Exception {
		File outputFolder;
		URL folder = FFmpegTest.class.getResource(File.separator);
		outputFolder = new File(folder.getPath());

		testFolderPath = outputFolder.getPath()+File.separator;
		ffmpegTestFolderPath = testFolderPath+"ffmpegTest"+File.separator;

		File ffmpegTest = new File(ffmpegTestFolderPath);
		if(!ffmpegTest.isDirectory()) ffmpegTest.mkdir();
	}

	/**
	 * Testing ffmpeg launch with the "-h" argument
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownOperatingSystem
	 */
	@Test
	public void testExecution() throws IOException, InterruptedException, UnknownOperatingSystem {
		int exitCode = FFmpeg.execute(new String[] {"-h"});
		assertThat(exitCode, is(0));
	}
	
	@Test
	public void testGetVideoDuration() throws IOException {
		FileExtended video = new FileExtended(testFolderPath+"testVideo.mp4");
		assertThat(FFmpeg.getVideoDuration(video), is(8));
	}
	
	@Test
	public void testConvertImageToVideo() throws IOException, InterruptedException, UnknownOperatingSystem {
		File result = FFmpeg.convertImageToVideo(new FileExtended(ffmpegTestFolderPath, false), 5, new FileExtended(testFolderPath+"sun.jpg"));
		assertThat(result, notNullValue());
	}

	@Test
	public void testConcatenateVideos() throws IOException, InterruptedException, UnknownOperatingSystem {
		FileExtended video1 = FFmpeg.convertImageToVideo(new FileExtended(ffmpegTestFolderPath, false), 5, new FileExtended(testFolderPath+"sun.jpg"));
		FileExtended video2 = FFmpeg.convertImageToVideo(new FileExtended(ffmpegTestFolderPath, false), 5, new FileExtended(testFolderPath+"moon.jpg"));

		FileExtended[] input = {video1,	video2};
		FileExtended output = new FileExtended(ffmpegTestFolderPath+"concat.mp4", false);

		int exitCode = FFmpeg.concatenateVideos(output, input);
		assertThat(exitCode, is(0));
	}
}
