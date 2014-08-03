package dbg.data.property;

import dbg.data.WeatherLocation;
import dbg.ui.LocalizedText;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeatherProperties implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ArrayList<Type> activeTypes;
	private int nbDays;
	private ArrayList<WeatherLocation> locations;
	private int displayTime;
	private File backgroundImage;
	
	public WeatherProperties(ArrayList<Type> activeTypes, int nbDays, ArrayList<WeatherLocation> locations,
			int displayTime, File backgroundImage) {
		this.activeTypes = activeTypes;
		this.nbDays = nbDays;
		this.locations = locations;
		this.displayTime = displayTime;
		this.backgroundImage = backgroundImage;
	}
	
	public WeatherProperties() {
		this(new ArrayList<Type>(), 0, new ArrayList<WeatherLocation>(), 0, null);
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

	public List<WeatherLocation> getLocations() {
		return locations;
	}

	public void setLocations(ArrayList<WeatherLocation> locations) {
		this.locations = locations;
	}

	public boolean addLocation(WeatherLocation location) {
		return locations.add(location);
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
		CITY(LocalizedText.get("city_weather")),
		REGIONAL(LocalizedText.get("regional_weather")),
		NATIONAL(LocalizedText.get("national_weather"));
		
		private String title;

		private Type(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
}
