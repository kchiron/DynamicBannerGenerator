package dbg.util;


import org.junit.Test;

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

	@Test
	public void testFile() {
		File file = new File("");
		File file2 = new File(file.getAbsolutePath());
		System.out.println("is directory: "+file.isDirectory());
		System.out.println("is file: "+file.isFile());
		System.out.println("path: "+file.getAbsolutePath());
		System.out.println("name: "+file.getName());
		System.out.println("name2: "+file2.getName());
	}
}
