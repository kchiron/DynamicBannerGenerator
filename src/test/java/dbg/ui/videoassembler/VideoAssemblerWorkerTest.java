package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.VideoOutputProperties;
import dbg.util.TestUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class VideoAssemblerWorkerTest {

	private static VideoAssembler videoAssembler;

	@BeforeClass
	public static void setUpClass() throws Exception {
		MediaSequence mediaSequence = new MediaSequence(){{
			add(new VideoElement("Earth", TestUtils.getMediaSample("earth.ts")));
			add(new ImageElement("Moon", TestUtils.getMediaSample("moon.jpg"), 5));
			add(new VideoElement("Earth", TestUtils.getMediaSample("earth.ts")));
		}};
		VideoOutputProperties videoOutputProperties = new VideoOutputProperties();
		videoOutputProperties.setOutputFolder(new File(""));
		videoOutputProperties.setIndexOfVideoSize(3);

		videoAssembler = new VideoAssembler(mediaSequence, videoOutputProperties, null) {
			@Override
			protected void process(List<String> chunks) {
				System.out.println(chunks);
			}

			@Override
			protected void done() {
				System.out.println("DONE");
			}
		};
		videoAssembler.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("progress")) {
					System.out.println("progress : "+evt.getNewValue());
				}
				else System.out.println(evt.getPropertyName()+" : "+evt.getNewValue());
			}
		});
	}
	
	@Test
	public void test() throws InterruptedException, ExecutionException {
		videoAssembler.execute();
		videoAssembler.get();
	}

}
