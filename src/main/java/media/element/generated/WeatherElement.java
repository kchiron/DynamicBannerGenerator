package media.element.generated;

import javax.swing.ImageIcon;

import media.element.MediaElement;
import ui.LocalizedText;

public class WeatherElement extends MediaElement {

	private static final long serialVersionUID = 1L;
	
	private Type type;
	
	public WeatherElement(String subTitle, Type type) {
		super(type.toString(), subTitle);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}

	@Override
	protected TwoStateIcon initIcon() {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource("weather.png"));
			ImageIcon icon2 = new ImageIcon(getClass().getResource("weather_on.png"));
			return new TwoStateIcon(icon, icon2);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Enums types of weather forcast (city, regional and national)
	 */
	public enum Type {
		CITY(LocalizedText.city_weather), 
		REGIONAL(LocalizedText.regional_weather), 
		NATIONAL(LocalizedText.national_weather);
		
		private String title;

		private Type(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	};
	
}
