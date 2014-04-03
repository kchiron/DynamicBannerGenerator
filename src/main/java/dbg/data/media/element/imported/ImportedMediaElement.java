package dbg.data.media.element.imported;

import dbg.data.media.element.MediaElement;

import java.io.File;
import java.net.URI;

/**
 * Abstract class for data.media element imported from a file (as opposed to the generated elements)
 * @author gcornut
 */
public abstract class ImportedMediaElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private String filePath;

	public ImportedMediaElement(String title, File inportedFile, int duration) {
		super(title, (inportedFile != null ? inportedFile.getName() : ""), duration);
		setFile(inportedFile);
	}

	public File getFile() {
		return new File(filePath);
	}
	
	public void setFile(File inportedFile) {
		URI outputRelativeURI = new File("").toURI().relativize(inportedFile.toURI());
		this.filePath = outputRelativeURI.getPath();
		setSubTitle(inportedFile.getName());
	}
}
