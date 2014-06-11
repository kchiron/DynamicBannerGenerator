package dbg.ffmpeg;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class FFmpegConcatTest {
	
	private static MediaSequence sequence;
	private static File outputFile;
	private static FFmpegVideoData outputOptions;

	@SuppressWarnings("serial")
	@BeforeClass
	public static void setUpClass() {
		final VideoElement videoElement1 = new VideoElement("Earth", TestUtils.getMediaSample("earth.mts"));
		final ImageElement imageElement1 = new ImageElement("Moon", TestUtils.getMediaSample("moon.jpg"), 15);
		final ImageElement imageElement2 = new ImageElement("Sun", TestUtils.getMediaSample("sun.jpg"), 10);

		sequence = new MediaSequence(){{
			add(videoElement1); //4 sec
			add(imageElement1); //15 sec
			add(videoElement1); //4 sec
			add(imageElement2); //10 sec
		}};

		outputFile = new File("output-concat.test");
		
		outputOptions = new FFmpegVideoData();
		outputOptions.setBitRate("500k");
		outputOptions.setSize("1440x900");
	}
	
	@Test
	public void test() throws Exception {
		FFmpegConcat concat = new FFmpegConcat(sequence, outputFile, outputOptions){
			public void setProgress(String message, int progress) {
				System.err.println(String.format("%02d", progress) + "% [" + (message != null ? message : "") + "]");
			}
		};
		
		concat.execute(null);
	}
}
