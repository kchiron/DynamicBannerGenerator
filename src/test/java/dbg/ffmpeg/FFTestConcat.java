package dbg.ffmpeg;

import org.junit.Test;

public class FFTestConcat {
	@Test
	public void test() throws Exception {
		//ProcessBuilder pb = new ProcessBuilder("ffmpeg-native/ffmpeg-mac");
		
		FFmpegTest fFmpegTest = new FFmpegTest();
		FFmpegTest.setUpClass();
		fFmpegTest.testConvertImageToVideo();
	}
}
