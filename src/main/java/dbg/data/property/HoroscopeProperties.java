package dbg.data.property;

import java.io.File;
import java.io.Serializable;

public class HoroscopeProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String backgroundImagePath;
	private int displayTime;
	private int signPerPage;
	
	/**
	 * Constructs default horoscope properties
	 */
	public HoroscopeProperties() {
		this(null, 2, 3);
	}
	
	public HoroscopeProperties(File backgroundImage, int horoscopeDisplayTime, int signPerPage) {
		super();
		setBackgroundImage(backgroundImage);
		this.displayTime = horoscopeDisplayTime;
		this.signPerPage = signPerPage;
	}

	public File getBackgroundImage() {
		return backgroundImagePath == null ? null : new File(backgroundImagePath);
	}

	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImagePath = backgroundImage == null ? null : new File("").toURI().relativize(backgroundImage.toURI()).getPath();
	}

	public int getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(int displayTime) {
		this.displayTime = displayTime;
	}

	public int getSignPerPage() {
		return signPerPage;
	}

	public void setSignPerPage(int signPerPage) {
		this.signPerPage = signPerPage;
	}
}
