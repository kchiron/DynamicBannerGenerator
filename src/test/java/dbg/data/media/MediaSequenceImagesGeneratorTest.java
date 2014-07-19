package dbg.data.media;

import dbg.control.HoroscopeControl;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.generated.HoroscopeElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.PropertyManager;
import dbg.util.Logger;
import dbg.util.ProgressState;
import dbg.util.TemporaryFileHandler;
import dbg.util.TestUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MediaSequenceImagesGeneratorTest  {
	private static MediaSequence sequence;
	private static TemporaryFileHandler temporaryFileHandler;
	private static Logger logger;

	@SuppressWarnings("serial")
	@BeforeClass
	public static void setUpClass() throws Exception {
		final VideoElement videoElement = new VideoElement("Earth", TestUtils.getMediaSample("earth.mts"));
		final ImageElement imageElement = new ImageElement("Moon", TestUtils.getMediaSample("moon.jpg"), 15);
		final HoroscopeElement horoscopeElement = new HoroscopeElement(12);
		PropertyManager.loadFromFile();
		PropertyManager.getHoroscopeProperties().setBackgroundImage(TestUtils.getMediaSample("sun.jpg"));
		horoscopeElement.setSigns(new HoroscopeControl.Signs[]{HoroscopeControl.Signs.TAURUS, HoroscopeControl.Signs.SAGITTARIUS});

		sequence = new MediaSequence(){{
			add(imageElement); //15 sec
			add(horoscopeElement); //12 sec
			add(videoElement); //4 sec
		}};

		temporaryFileHandler = new TemporaryFileHandler();
		logger = new Logger("test-logs", MediaSequenceImagesGeneratorTest.class.getSimpleName());
	}

	@Test
	public void testGenerate() throws Exception {
		MediaSequence generate = new MediaSequenceImagesGenerator(temporaryFileHandler, sequence, logger).generate(new ProgressState(null));

		Assert.assertEquals(generate.size(), sequence.size());

		Assert.assertEquals(generate.getDuration(), sequence.getDuration());

		for(MediaElement element : generate) {
			Assert.assertTrue(element instanceof ImportedMediaElement);
		}
	}
}