package media.element.imported;

import java.io.File;

import javax.swing.ImageIcon;


public class VideoElement extends InportedMediaElement {

	private static final long serialVersionUID = 1L;

	public VideoElement(String title, File inportedFile) {
		super(title, inportedFile);
	}

	
	@Override
	protected TwoStateIcon initIcon() {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("video.png"));
			ImageIcon icon2 = new ImageIcon(getClass().getResource("video_on.png"));
			return new TwoStateIcon(icon, icon2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
