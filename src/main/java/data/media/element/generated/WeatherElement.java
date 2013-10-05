package data.media.element.generated;

import data.media.element.MediaElement;
import data.property.WeatherProperties;

public class WeatherElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	/** Number of days displayed in the same page */
	private int nbDays;
	private WeatherProperties.Type type;
	
	public WeatherElement(String subTitle, WeatherProperties.Type type, int nbDays, int duration) {
		super(type.toString(), subTitle, duration);
		this.type = type;
		this.nbDays = nbDays;
	}
	
	public WeatherProperties.Type getType() {
		return type;
	}
	
	public int getNbDays() {
		return nbDays;
	}
}
