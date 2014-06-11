package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.VideoOutputProperties;
import dbg.util.ActivityMonitor;
import dbg.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

public class VideoAssemblerTest {

	private static VideoAssembler videoAssembler;

	@BeforeClass
	public static void setUpClass() throws Exception {
		MediaSequence mediaSequence = new MediaSequence() {{
			add(new VideoElement("Earth", TestUtils.getMediaSample("earth.mts")));
			add(new ImageElement("Moon", TestUtils.getMediaSample("moon.jpg"), 5));
			add(new VideoElement("Earth", TestUtils.getMediaSample("earth.mts")));
		}};
		VideoOutputProperties videoOutputProperties = new VideoOutputProperties();
		videoOutputProperties.setOutputFolder(new File(""));
		videoOutputProperties.setIndexOfVideoSize(3);

		videoAssembler = new VideoAssembler(mediaSequence, videoOutputProperties, new ActivityMonitor() {
			@Override
			public void setProgress(String message, int progress) {
				System.out.println("[" + progress + "%] " + (message != null ? message : ""));
			}
		});
	}

	@Test
	public void test() throws Exception {
		videoAssembler.call();
	}

}
