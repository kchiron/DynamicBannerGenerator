package dbg.data.media.element.generated;

import dbg.data.media.element.MediaElement;
import dbg.exception.ImageGenerationException;
import dbg.util.TemporaryFileHandler;

import java.io.File;
import java.io.IOException;

public abstract class GeneratedMediaElement extends MediaElement {

	private static final long serialVersionUID = 2555993888374485896L;

	public GeneratedMediaElement(String title, String subTitle, int duration) {
		super(title, subTitle, duration);
	}

	public abstract File generateImage(TemporaryFileHandler temporaryFileHandler) throws ImageGenerationException, IOException;
}
