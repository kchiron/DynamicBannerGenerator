package dbg.util;


import java.io.File;

public class TestUtils {
	public static File getTestFolder() {
		return new File(getTestFolderPath());
	}
	
	public static String getTestFolderPath() {
		return TestUtils.class.getResource(File.separator).getPath();
	}
	
	public static File getTestFile(String fileName) {
		return new File(getTestFolderPath()+fileName);
	}
	
}
