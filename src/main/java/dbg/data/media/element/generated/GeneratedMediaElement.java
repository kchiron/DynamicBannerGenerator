package dbg.data.media.element.generated;

import dbg.data.media.element.MediaElement;

import java.io.File;

public abstract class GeneratedMediaElement extends MediaElement {

	public GeneratedMediaElement(String title, String subTitle, int duration) {
		super(title, subTitle, duration);
	}

	public abstract File generateImage() throws Exception;
}
