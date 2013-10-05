package data.media.element;

import java.io.Serializable;

/**
 * MediaElement of a <code>MediaSequence</code>
 * @author gcornut
 */
public abstract class MediaElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String subTitle;
	private int duration;
	
	public MediaElement(String title, String subTitle, int duration)  {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.duration = duration;
	}
	
	public void setTitle(String title) {
		char[] sArray = title.trim().toCharArray();
		sArray[0] = Character.toUpperCase(sArray[0]);
		this.title = new String(sArray);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle.trim();
	}

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}
}
