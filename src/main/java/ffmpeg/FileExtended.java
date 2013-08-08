package ffmpeg;

import java.io.File;
import java.io.IOException;

/**
 * Inherits from java.io.File but brings utilities like <code>getExtension</code>.
 * @author gcornut
 */
public class FileExtended extends File {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a <code>FileExtended</code> with its path and whether the file should exists or not
	 * @param pathToFile path to the file
	 * @param shouldExist true if you want to check that the file exists; false otherwise
	 * @throws IOException if the file should exists but is not found
	 */
	public FileExtended(String pathToFile, boolean shouldExist) throws IOException {
		super(pathToFile);
		
		if(shouldExist && !this.exists()) throw new IOException("File doesn't exists!");
	}
	
	public FileExtended(String pathToFile) throws IOException {
		this(pathToFile, true);
	}
	
	/**
	 * Gets the extension of the file
	 * @return the extension of the file
	 */
	public String getExtension() {
		final String fileName = super.getName();
		String extension = "";
		
		int i = fileName.lastIndexOf('.');
		int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

		if (i > p) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}
	
	/**
	 * Gets the name of the file without its extension
	 * @return file name without extension
	 */
	public String getNameWithoutExtension() {
		final String fileName = super.getName();
		
		if (fileName == null) return null;

        int pos = fileName.lastIndexOf(".");

        if (pos == -1) return fileName;

        return fileName.substring(0, pos);
	}
}
