package dbg.util;


import java.io.File;

/**
 * Utils class for DBG JUnit tests
 */
public class TestUtils {
	public static File getTestFolder() {
		return new File(getTestFolderPath());
	}
	
	public static String getTestFolderPath() {
		return TestUtils.class.getResource(File.separator).getPath();
	}
	
	public static File getTestFile(String fileName) {
		return new File(getTestFolderPath(), fileName);
	}

	public static File getMediaSample(String name) {
		File mediaSamples = new File(getTestFolder(), "media-samples");

		return new File(mediaSamples, name);
	}
}
