package data.property;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Video output properties
 * @author gcornut
 */
public class VideoOutputProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final static String[] standardVideoSize = {
		"640×360", 
		"1024×576", 
		"1280×720 (720p)", 
		"1440x900", 
		"1600×900", 
		"1920x1080 (1080p)"
	};
	
	private int indexOfVideoSize;
	private File outputFolder;

	public VideoOutputProperties(int videoSize, File outputFolder) {
		this.indexOfVideoSize = videoSize;
		
		if(outputFolder.getName().equals(".")){
			try {
				this.outputFolder = new File (outputFolder.getCanonicalFile().getParent());
			} catch (IOException e) {
				this.outputFolder = outputFolder;
			}
		}
		else this.outputFolder = outputFolder;
	}

	/** Constructs default video output properties */
	public VideoOutputProperties() {
		this(2, new File("."));
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
		return outputFolder;
	}
	
	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	public static String[] getStandardvideosize() {
		return standardVideoSize;
	}
	
	
}
