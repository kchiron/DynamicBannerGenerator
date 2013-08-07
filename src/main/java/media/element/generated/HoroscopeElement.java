package media.element.generated;

import javax.swing.ImageIcon;

import media.element.MediaElement;

public class HoroscopeElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	public HoroscopeElement(String title, String subTitle) {
		super(title, subTitle);
	}

	@Override
	protected TwoStateIcon initIcon() {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("horoscope.png"));
			ImageIcon icon2 = new ImageIcon(getClass().getResource("horoscope_on.png"));
			return new TwoStateIcon(icon, icon2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
