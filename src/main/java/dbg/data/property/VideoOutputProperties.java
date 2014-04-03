package dbg.data.property;

import java.io.File;
import java.io.Serializable;
import java.net.URI;

/**
 * Video output properties
 * @author gcornut
 */
public class VideoOutputProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final static String[] standardVideoSize = {
		"640x360",
		"1024x576",
		"1280x720 (720p)",
		"1440x900",
		"1600x900",
		"1920x1080 (1080p)"
	};
	
	private int indexOfVideoSize;
	private String outputFolderPath;

	public VideoOutputProperties(int videoSize, File outputFolder) {
		this.indexOfVideoSize = videoSize;

		setOutputFolder(outputFolder);
	}

	/** Constructs default video output properties */
	public VideoOutputProperties() {
		this(2, new File(""));
	}

	public String getVideoSize() {
		return standardVideoSize[indexOfVideoSize].replaceAll("\\s\\(\\d+p\\)", "");
	}
	
	public int getIndexOfVideoSize() {
		return indexOfVideoSize;
	}

	public void setIndexOfVideoSize(int indexOfVideoSize) {
		this.indexOfVideoSize = indexOfVideoSize;
	}

	public File getOutputFolder() {
		return new File(outputFolderPath);
	}
	
	public void setOutputFolder(File outputFolder) {
		URI outputRelativeURI = new File("").toURI().relativize(outputFolder.toURI());
		this.outputFolderPath = outputRelativeURI.getPath();
	}
	
	public static String[] getStandardVideoSize() {
		return standardVideoSize;
	}
	
	
}
