package dbg.data.property;

import dbg.data.media.MediaSequence;
import dbg.ui.LocalizedText;

import javax.swing.*;
import java.io.*;

public class PropertyManager {

	private static MediaSequence sequence;
	private static VideoOutputProperties videoOutputProperties;
	private static HoroscopeProperties horoscopeProperties;
	private static WeatherProperties weatherProperties;

	private static String googlePlacesAPIKey;
	private static String wunderGroundAPIKey;

	//Configuration file
	private static File configFile = new File("config.bin");

	/**
	 * Loads the properties from the configuration file or creates new default properties if the file doesn't exist
	 *
	 * @throws Exception
	 */
	public static void loadFromFile() throws Exception {
		ObjectInputStream input = null;

		Exception error = null;
		if (configFile.exists()) {
			try {
				input = new ObjectInputStream(new FileInputStream(configFile));

				Object o;
				boolean EOF = false;

				while (!EOF) {
					try {
						o = input.readObject();
						if (o instanceof MediaSequence)
							sequence = (MediaSequence) o;
						else if (o instanceof HoroscopeProperties)
							horoscopeProperties = (HoroscopeProperties) o;
						else if (o instanceof VideoOutputProperties)
							videoOutputProperties = (VideoOutputProperties) o;
						else if (o instanceof WeatherProperties)
							weatherProperties = (WeatherProperties) o;
						else if (o instanceof String && googlePlacesAPIKey == null)
							googlePlacesAPIKey = (String) o;
						else if (o instanceof String)
							wunderGroundAPIKey = (String) o;
					} catch (EOFException e1) {
						EOF = true;
					} catch (Exception ignored) {}
				}
			} catch (IOException e) {
				error = e;
			} finally {
				if (input != null) input.close();
			}
		}
		boolean newProp = false;
		if(sequence == null) {
			newProp = true;
			sequence = new MediaSequence();
		}
		if(videoOutputProperties == null) {
			newProp = true;
			videoOutputProperties = new VideoOutputProperties();
		}
		if(horoscopeProperties == null) {
			newProp = true;
			horoscopeProperties = new HoroscopeProperties();
		}
		if(weatherProperties == null) {
			newProp = true;
			weatherProperties = new WeatherProperties();
		}

		if(error != null)
			throw error;

		if (googlePlacesAPIKey == null || googlePlacesAPIKey.isEmpty()) {
			googlePlacesAPIKey = JOptionPane.showInputDialog(null, LocalizedText.get("enter_googleapikey"), LocalizedText.get("missing_data"), JOptionPane.QUESTION_MESSAGE);

			if (googlePlacesAPIKey == null || googlePlacesAPIKey.isEmpty()) {
				throw new Exception(LocalizedText.get("missing_data") + ": " + LocalizedText.get("googleapikey_required"));
			}
			newProp = true;
		}
		if (wunderGroundAPIKey == null || wunderGroundAPIKey.isEmpty()) {
			wunderGroundAPIKey = JOptionPane.showInputDialog(null, LocalizedText.get("enter_wundergroundapikey"), LocalizedText.get("missing_data"), JOptionPane.QUESTION_MESSAGE);

			if (wunderGroundAPIKey == null || wunderGroundAPIKey.isEmpty()) {
				throw new Exception(LocalizedText.get("missing_data") + ": " + LocalizedText.get("wundergroundapikey_required"));
			}
			newProp = true;
		}


		if(newProp)
			saveToFile();
	}

	/**
	 * Saves all properties to the configuration file
	 */
	public static void saveToFile() throws IOException {
		ObjectOutputStream output = null;
		try {
			output = new ObjectOutputStream(new FileOutputStream(configFile));

			output.writeObject(sequence);
			output.writeObject(horoscopeProperties);
			output.writeObject(videoOutputProperties);
			output.writeObject(weatherProperties);
			output.writeObject(googlePlacesAPIKey);
			output.writeObject(wunderGroundAPIKey);
		} catch (IOException e) {
			throw e;
		} finally {
			if (output != null) output.close();
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

	public static void setVideoOutputProperties(VideoOutputProperties videoOutputProperties) {
		PropertyManager.videoOutputProperties = videoOutputProperties;
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

	public static void setWunderGroundAPIKey(String wunderGroundAPIKey) {
		PropertyManager.wunderGroundAPIKey = wunderGroundAPIKey;
	}

	public static String getWunderGroundAPIKey() {
		return wunderGroundAPIKey;
	}
}
