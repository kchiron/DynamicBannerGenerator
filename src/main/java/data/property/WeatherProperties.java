package data.property;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import data.WeatherLocation;
import ui.LocalizedText;

public class WeatherProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ArrayList<Type> activeTypes;
	private int nbDays;
	private WeatherLocation location;
	private int displayTime;
	private File backgroundImage;
	
	public WeatherProperties( ArrayList<Type> activeTypes, int nbDays, WeatherLocation location,
			int displayTime, File backgroundImage) {
		this.activeTypes = activeTypes;
		this.nbDays = nbDays;
		this.location = location;
		this.displayTime = displayTime;
		this.backgroundImage = backgroundImage;
	}
	
	public WeatherProperties() {
		this(new ArrayList<Type>(), 0, null, 0, null);
	}

	public ArrayList<Type> getActiveTypes() {
		return activeTypes;
	}
	
	public void addActiveType(Type type) {
		if(!this.activeTypes.contains(type))
			this.activeTypes.add(type);
	}
	
	public void removeActiveType(Type type) {
		this.activeTypes.remove(type);
	}

	public int getNbDays() {
		return nbDays;
	}

	public void setNbDays(int nbDays) {
		this.nbDays = nbDays;
	}

	public WeatherLocation getLocation() {
		return location;
	}

	public void setLocation(WeatherLocation location) {
		this.location = location;
	}

	public int getDisplayTime() {
		return displayTime;
	}

	public void setDisplayTime(int displayTime) {
		this.displayTime = displayTime;
	}

	public File getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(File backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	/** Enums types of weather forcast (city, regional and national) */
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
