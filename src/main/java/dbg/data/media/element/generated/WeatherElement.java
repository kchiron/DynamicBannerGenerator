package dbg.data.media.element.generated;

import dbg.data.WeatherLocation;
import dbg.data.property.WeatherProperties;
import dbg.ui.LocalizedText;

import java.io.File;

public class WeatherElement extends GeneratedMediaElement {

	private static final long serialVersionUID = 1L;
	
	private WeatherProperties.Type type;
	
	public WeatherElement(WeatherLocation location, int duration) {
		super("", (location==null)?"-":location.toString(), duration);
	}

	public WeatherProperties.Type getType() {
		return type;
	}

	public void setType(WeatherProperties.Type type) {
		this.type = type;
		
		switch (type) {
			case CITY:
				setTitle(LocalizedText.city_weather);
				break;
			case REGIONAL:
				setTitle(LocalizedText.regional_weather);
				break;
			case NATIONAL:
				setTitle(LocalizedText.national_weather);
				break;
		}
	}

	public void setLocation(WeatherLocation location) {
		setSubTitle((location==null)?"-":location.toString());
	}

	@Override
	public File generateImage() {
		return null;
	}
}
