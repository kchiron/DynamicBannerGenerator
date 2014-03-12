package dbg.data.media.element.imported;

import dbg.ffmpeg.FileExtended;

/**
 * Image element in a sequence
 * @author gcornut
 */
public class ImageElement extends ImportedMediaElement {

	private static final long serialVersionUID = 1L;
	
	public ImageElement(String title, FileExtended inportedFile, int duration) {
		super(title, inportedFile, duration);	
	}
	
}
