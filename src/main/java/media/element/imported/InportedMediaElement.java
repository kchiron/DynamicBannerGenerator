package media.element.imported;

import java.io.File;

import media.element.MediaElement;

/**
 * Abstract class for media element imported from a file (as opposed to the generated elements)
 * @author gcornut
 */
public abstract class InportedMediaElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private File inportedFile;

	public InportedMediaElement(String title, File inportedFile) {
		super(title, inportedFile.getName());
	}

	public File getInportedFile() {
		return inportedFile;
	}
}
