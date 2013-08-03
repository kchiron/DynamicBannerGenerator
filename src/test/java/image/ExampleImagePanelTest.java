package image;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import org.junit.Test;
import util.TestUtils;

/**
 * Test and demonstration of the ImagePanel class with its export to image
 * @author gcornut
 */
public class ExampleImagePanelTest {

	@Test
	public void test() throws IOException {
		//New image panel
		final ImagePanel bgPanel = new ExampleImagePanel();
		
		//Save the new image into a file
		File ouputImage = TestUtils.getTestFile("export.png");
		bgPanel.exportToImage(ouputImage, "png");
		
		//Find result at: target/test-classes/export.png
	}
	
}
