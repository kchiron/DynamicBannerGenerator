package dbg.data.property;

import java.io.File;
import java.io.Serializable;

public class HoroscopeProperties implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private File backgroundImage;
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
		this.backgroundImage = backgroundImage;
		this.displayTime = horoscopeDisplayTime;
		this.signPerPage = signPerPage;
	}

	public File getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImage = backgroundImage;
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
