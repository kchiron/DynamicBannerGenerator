package data.media.element.generated;

import java.util.ArrayList;

import control.HoroscopeControl;
import ui.LocalizedText;
import data.media.element.MediaElement;

public class HoroscopeElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private final ArrayList<HoroscopeControl.Signs> signs;
	
	public HoroscopeElement(int duration) {
		super(LocalizedText.horoscope, "", duration);
		this.signs = new ArrayList<HoroscopeControl.Signs>();
	}

	public void setSigns(HoroscopeControl.Signs[] signs) {
		this.signs.clear();
		String subTitle = "";
		for(HoroscopeControl.Signs sign: signs) {
			if(sign != null) {
				this.signs.add(sign);
				subTitle += sign.toString().toLowerCase() + ", "; 	
			}
		}
		subTitle = subTitle.substring(0, subTitle.length()-2);
		this.setSubTitle(subTitle);
	}
}