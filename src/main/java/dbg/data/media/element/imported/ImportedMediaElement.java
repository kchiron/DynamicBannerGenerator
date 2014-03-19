package dbg.data.media.element.imported;

import java.io.File;

import dbg.data.media.element.MediaElement;

/**
 * Abstract class for data.media element imported from a file (as opposed to the generated elements)
 * @author gcornut
 */
public abstract class ImportedMediaElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private File file;

	public ImportedMediaElement(String title, File inportedFile, int duration) {
		super(title, (inportedFile != null ? inportedFile.getName() : ""), duration);
		this.file = inportedFile;
	}

	public File getFile() {
		return file;
	}
	
	public void setFile(File inportedFile) {
		this.file = inportedFile;
		setSubTitle(inportedFile.getName());
	}
}
