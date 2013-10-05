package data.media.element.imported;

import java.io.File;

import data.media.element.MediaElement;
import ffmpeg.FileExtended;

/**
 * Abstract class for data.media element imported from a file (as opposed to the generated elements)
 * @author gcornut
 */
public abstract class ImportedMediaElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private FileExtended inportedFile;

	public ImportedMediaElement(String title, File inportedFile, int duration) {
		super(title, (inportedFile != null ? inportedFile.getName() : ""), duration);
	}

	public FileExtended getInportedFile() {
		return inportedFile;
	}
	
	public void setInportedFile(FileExtended inportedFile) {
		this.inportedFile = inportedFile;
		setSubTitle(inportedFile.getName());
	}
}
