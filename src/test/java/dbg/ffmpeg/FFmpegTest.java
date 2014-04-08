package dbg.ffmpeg;

import dbg.exception.UnknownOperatingSystem;
import dbg.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
		int exitCode = FFmpeg.execute(Arrays.asList("-h"));
		assertThat(exitCode, is(0));
	}
	
	@Test
	public void testGetVideoDuration() throws IOException {
		File video = TestUtils.getMediaSample("earth.mts");
		assertThat(FFmpeg.getVideoDuration(video), is(4));
	}
	
	@Test
	public void testGetVideoData() throws IOException {
		File video = TestUtils.getMediaSample("earth.mts");
		FFmpegVideoData videoData = FFmpeg.getVideoData(video);
		System.out.println("Duration: "+videoData.getDuration());
		System.out.println("Size: "+videoData.getSize());
		System.out.println("Codec: "+videoData.getCodec());
	}
	
	@Test
	public void testConvertImageToVideo() throws IOException, InterruptedException, UnknownOperatingSystem {
		File result = FFmpeg.convertImageToVideo(
			new File(ffmpegTestFolderPath), 5, TestUtils.getMediaSample("sun.jpg"), "sun", false
		);
		assertThat(result, notNullValue());
	}
}
