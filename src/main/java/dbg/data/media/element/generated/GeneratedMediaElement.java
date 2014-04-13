package dbg.data.media.element.generated;

import dbg.data.media.element.MediaElement;
import dbg.exception.ImageGenerationException;

import java.io.File;
import java.io.IOException;

public abstract class GeneratedMediaElement extends MediaElement {

	public GeneratedMediaElement(String title, String subTitle, int duration) {
		super(title, subTitle, duration);
	}

	public abstract File generateImage() throws ImageGenerationException, IOException;
}
