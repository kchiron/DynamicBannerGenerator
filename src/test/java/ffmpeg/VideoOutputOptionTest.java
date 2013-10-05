package ffmpeg;

import static org.junit.Assert.*;

import org.junit.Test;

public class VideoOutputOptionTest {

	@Test
	public void test() {
		VideoOutputOption v = new VideoOutputOption("1440x900", "400k", "21", "libx264", "mp4", "output");
		assertEquals("-s 1440x900 -b:v 400k -r 21 -c:v libx264 output.mp4", v.toString());
	}

}
