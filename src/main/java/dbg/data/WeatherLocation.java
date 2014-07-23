package dbg.data;

import java.io.Serializable;

/**
 * Data class that stores a location with latitude, longitude, city, region and country
 * This class is used for the weather localization
 */
public class WeatherLocation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String country;
	private final String region;
	private final String city;
	private final double longitude;
	private final double latitude;
	
	public WeatherLocation(String country, String region, String city, double longitute, double latitude) {
		this.country = country;
		this.region = region;
		this.city = city;
		this.longitude = longitute;
		this.latitude = latitude;
	}

	public String getCountry() {
		return country;
	}

	public String getRegion() {
		return region;
	}

	public String getCity() {
		return city;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	@Override
	public String toString() {
		final StringBuilder str = new StringBuilder();
		
		if(city != null) {
			str.append(city);
		}
		if(region != null) {
			if(str.length() > 0) str.append(", ");
			str.append(region);
		}
		if(country != null) {
			if(str.length() > 0) str.append(", ");
			str.append(country);
		}
		return (str.length() > 0) ? str.toString() : "-";
	}
}