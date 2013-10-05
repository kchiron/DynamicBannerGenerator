package ui.form.weatherlocation;

import java.io.Serializable;

public class WeatherLocation implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private final String country;
	private final String region;
	private final String city;
	private final double longitute;
	private final double latitude;
	
	public WeatherLocation(String country, String region, String city, double longitute, double latitude) {
		this.country = country;
		this.region = region;
		this.city = city;
		this.longitute = longitute;
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
	
	public double getLongitute() {
		return longitute;
	}
	
	@Override
	public String toString() {
		return city + (region != null ? ", " + region : "") + (country != null ? ", " + country : "");
	}
}