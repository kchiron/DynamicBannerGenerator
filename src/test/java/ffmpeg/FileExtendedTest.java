package ffmpeg;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FileExtendedTest {

	private FileExtended testFile;

	@Rule
	public ExpectedException exception = ExpectedException.none();
	  
	@BeforeClass
    public static void setUpClass() throws Exception {
		
    }
	
	@Test
	public void testConstruct() throws IOException {
		exception.expect(IOException.class);
		testFile = new FileExtended("foo");
	}

	@Test
	public void testGetExtension() throws IOException {
		testFile = new FileExtended("foo.bar", false);
		
		String extension = testFile.getExtension();
		assertEquals("bar", extension);
	}
	
	@Test
	public void testNameWithoutExtension() throws IOException {
		testFile = new FileExtended("foo.bar", false);
		
		String fileName = testFile.getNameWithoutExtension();
		assertEquals("foo", fileName);
	}
	
	@Test
	public void test() throws IOException {
		testFile = new FileExtended("/path/to/file/foo.bar", false);
		
		System.out.println(new FileExtended(testFile.getParent(), false).getPath());
		
	}
}
