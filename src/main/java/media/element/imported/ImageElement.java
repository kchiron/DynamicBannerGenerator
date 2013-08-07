package media.element.imported;

import java.io.File;

import javax.swing.ImageIcon;

import exception.ZeroOrNegativeNumberException;

/**
 * Image element in a sequence
 * @author gcornut
 */
public class ImageElement extends InportedMediaElement {

	private static final long serialVersionUID = 1L;
	private int duration;
	
	/**
	 * Constructs a image element with a title, a file and a duration in seconds
	 * @param title
	 * @param inportedFile
	 * @param duration
	 * @throws ZeroOrNegativeNumberException if the duration is negative or equal to zero
	 */
	public ImageElement(String title, File inportedFile, int duration) throws ZeroOrNegativeNumberException {
		super(title, inportedFile);
		
		if(duration <= 0)
			throw new ZeroOrNegativeNumberException();
		this.duration = duration;
	}
	
	public int getDuration() {
		return duration;
	}

	@Override
	protected TwoStateIcon initIcon() {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("image.png"));
			ImageIcon icon2 = new ImageIcon(getClass().getResource("image_on.png"));
			return new TwoStateIcon(icon, icon2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
