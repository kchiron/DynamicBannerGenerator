package dbg.data.property;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;

import dbg.data.media.MediaSequence;
import dbg.ui.LocalizedText;

public class PropertyManager {

	private static MediaSequence sequence;
	private static VideoOutputProperties videoOutputProperties;
	private static HoroscopeProperties horoscopeProperties;
	private static WeatherProperties weatherProperties;

	private static String googlePlacesAPIKey;
	
	//Configuration file
	private static File configFile = new File("config.bin");

	/**
	 * Loads the properties from the configuration file or creates new default properties if the file doesn't exist
	 * @throws Exception 
	 */
	public static void loadFromFile() throws Exception {
		if(configFile.exists()) {
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(configFile));

				Object o = null;
				try {
					while(true) {
						o = input.readObject();
						if(o instanceof MediaSequence)
							sequence = (MediaSequence) o;
						else if(o instanceof HoroscopeProperties)
							horoscopeProperties = (HoroscopeProperties) o;
						else if(o instanceof VideoOutputProperties)
							videoOutputProperties = (VideoOutputProperties) o;
						else if(o instanceof WeatherProperties)
							weatherProperties = (WeatherProperties) o;
						else if(o instanceof String)
							googlePlacesAPIKey = (String) o;
					}
				} catch(EOFException e1) {}

				input.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			sequence = new MediaSequence();
			videoOutputProperties = new VideoOutputProperties();
			horoscopeProperties = new HoroscopeProperties();
			weatherProperties = new WeatherProperties();
		}
		
		if(googlePlacesAPIKey == null || googlePlacesAPIKey.isEmpty()) {
			googlePlacesAPIKey = JOptionPane.showInputDialog(null, LocalizedText.enter_googleapikey, LocalizedText.missing_data, JOptionPane.QUESTION_MESSAGE);
			
			if(googlePlacesAPIKey == null || googlePlacesAPIKey.isEmpty()) {
				throw new Exception(LocalizedText.missing_data+": "+LocalizedText.googleapikey_required);
			}
		}
	}

	/**	
	 * Saves all properties to the configuration file
	 */
	public static void saveToFile() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(configFile));

			output.writeObject(sequence);
			output.writeObject(horoscopeProperties);
			output.writeObject(videoOutputProperties);
			output.writeObject(weatherProperties);
			output.writeObject(googlePlacesAPIKey);

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File getConfigFile() {
		return configFile;
	}

	public static void setConfigFile(File configFile) {
		PropertyManager.configFile = configFile;
	}

	public static MediaSequence getSequence() {
		return sequence;
	}

	public static void setSequence(MediaSequence sequence) {
		PropertyManager.sequence = sequence;
	}

	public static VideoOutputProperties getVideoOutputProperties() {
		return videoOutputProperties;
	}

	public static HoroscopeProperties getHoroscopeProperties() {
		return horoscopeProperties;
	}

	public static WeatherProperties getWeatherProperties() {
		return weatherProperties;
	}
	
	public static void setGooglePlacesAPIKey(String googlePlacesAPIKey) {
		PropertyManager.googlePlacesAPIKey = googlePlacesAPIKey;
	}
	
	public static String getGooglePlacesAPIKey() {
		return googlePlacesAPIKey;
	}
}
