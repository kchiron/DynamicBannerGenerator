package dbg.data.media.element.imported;

import java.io.File;

/**
 * Image element in a sequence
 * @author gcornut
 */
public class ImageElement extends ImportedMediaElement {

	private static final long serialVersionUID = 1L;
	
	public ImageElement(String title, File inportedFile, int duration) {
		super(title, inportedFile, duration);	
	}
	
}
