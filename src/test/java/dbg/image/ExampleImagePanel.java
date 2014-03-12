package dbg.image;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import dbg.image.ImagePanel;
import dbg.ui.MultiLineLabel;
import dbg.ui.util.UiUtils;
import dbg.util.TestUtils;

/**
 * Test class used to demonstrate ImagePanel 
 * @author gcornut
 */
public class ExampleImagePanel extends ImagePanel {

	private static final long serialVersionUID = 1L;

	public ExampleImagePanel() throws IOException {
		//Sunset background
		super(ImageIO.read(TestUtils.getTestFile("sun.jpg")));
		
		//Add text, images...
		initContent();
		
		//This panel content was design on a 450x300 area
		// The following will scale the content to fit the background image size
		//$hide>>$  <--- Hiding the block below from the eclipse GUI designer
		{
			//Getting the size of the image (=panel size)
			final Dimension d = getPreferredSize();
			
			double scaleX = d.width / 450.0;
			double scaleY = d.width / 300.0;
			double scale =  Math.min(scaleX, scaleY);
			int shiftX = scale > 1.0 ? (int)((d.width - 450.0*scale)/2.0) : 0;
			int shiftY = scale > 1.0 ? (int)((d.height - 300.0*scale)/2.0) : 0;
			
			UiUtils.scaleAndShiftComponents(this, scale, shiftX, shiftY);	
		}
		//$hide<<$
	}

	/**
	 * Initializes the panel content.
	 * This panel is design based on a 450x300 panel (need to be scaled to fit the actual image size)
	 * @throws IOException if the image is not found
	 */
	private void initContent() throws IOException {
		
		//Absolute layout (component will be manually placed)
		setLayout(null);
		
		//Insert horoscope image
		{
			Image horoscope = ImageIO.read(TestUtils.getTestFile("horoscope.png"));
			ImagePanel im = new ImagePanel(horoscope);
			
			// X, Y, WIDTH, HEIGHT
			im.setBounds(29, 24, 59, 59);
			add(im);
		}
		
		//Insert sentence
		{
			//Label
			JLabel title = new JLabel("Nice Sunset ^^");
			title.setHorizontalAlignment(SwingConstants.LEFT);
			
			title.setFont(new Font("Helvetica", Font.BOLD, 18));

			title.setBounds(110, 24, 275, 59);
			add(title);
		}

		//Insert paragraph
		{
			//Label
			MultiLineLabel text = new MultiLineLabel(
				"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus euismod mollis tortor et porttitor. "+
				"Donec pellentesque, mauris non pretium commodo, tortor risus lacinia est, at laoreet est nibh egestas "+ 
				"ligula. Morbi vel nulla facilisis ligula vehicula convallis sit amet nec nunc. In hac habitasse platea "+ 
				"dictumst. Donec tincidunt elit arcu. Interdum et malesuada fames ac ante ipsum primis in faucibus. Quisque "+ 
				"sed pretium risus. Proin arcu velit, iaculis et nunc a, ullamcorper mattis odio.",
				MultiLineLabel.TextAlign.JUSTIFY
			);
			text.setVerticalAlignment(SwingConstants.TOP);
			text.setHorizontalAlignment(SwingConstants.CENTER);

			text.setFont(new Font("Helvetica", Font.PLAIN, 12));
			
			text.setBounds(29, 95, 392, 173);
			add(text);
		}
	}
}
