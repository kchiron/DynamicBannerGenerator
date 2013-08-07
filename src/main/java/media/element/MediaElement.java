package media.element;

import java.io.Serializable;

import javax.swing.ImageIcon;

/**
 * MediaElement of a <code>MediaSequence</code>
 * @author gcornut
 */
public abstract class MediaElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String subTitle;
	private final TwoStateIcon icon;
	
	public MediaElement(String title, String subTitle) {
		super();
		this.title = title;
		this.subTitle = subTitle;
		this.icon = initIcon();
	}

	public String getTitle() {
		return title;
	}

	public String getSubTitle() {
		return subTitle;
	}
	
	public ImageIcon getIcon() {
		return icon.getIcon();
	}
	
	public ImageIcon getSelectedIcon() {
		return icon.getSelectedIcon();
	}
	
	protected abstract TwoStateIcon initIcon();
	
	/**
	 * Two state icon (selected and not selected)
	 */
	protected class TwoStateIcon {

		private final ImageIcon icon; 
		private final ImageIcon selectedIcon;
		
		public TwoStateIcon(ImageIcon icon, ImageIcon selectedIcon) {
			super();
			this.icon = icon;
			this.selectedIcon = selectedIcon;
		}
		
		public ImageIcon getIcon() {
			return icon;
		}
		
		public ImageIcon getSelectedIcon() {
			return selectedIcon;
		}
	}
}
