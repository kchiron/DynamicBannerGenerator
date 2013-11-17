package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JSpinner;

import ui.form.filechooser.FileChooserField;
import ui.form.weatherlocation.WeatherLocationField;
import ui.panel.SequencePanel;
import ui.panel.WeatherPanel;
import data.WeatherLocation;
import data.media.element.MediaElement;
import data.media.element.generated.WeatherElement;
import data.property.PropertyManager;
import data.property.WeatherProperties;

/**
 * Class handling all user action on the weather panel and loading saved properties to the weather panel
 */
public class WeatherControl implements ActionListener {

	private final SequencePanel sequencePanel;
	private final WeatherPanel weatherPanel;

	private WeatherProperties properties;

	public WeatherControl(SequencePanel sequencePanel, WeatherPanel weatherPanel) {
		this.sequencePanel = sequencePanel;
		this.weatherPanel = weatherPanel;

		properties = PropertyManager.getWeatherProperties();

		//Update the weather type check boxes on the weather panel
		if(properties.getActiveTypes() != null) {
			for(WeatherProperties.Type type: properties.getActiveTypes()) {
				weatherPanel.getWeatherTypeCheckBox(type).setSelected(true);
			}
		}

		//Update the weather location field on the weather panel
		if(properties.getLocation() != null) {
			weatherPanel.getLocationField().setWeatherLocation(properties.getLocation());
			
			updateLocationCheckbox();
		}
		else {
			for(WeatherProperties.Type type : WeatherProperties.Type.values())
				weatherPanel.getWeatherTypeCheckBox(type).setEnabled(false);
		}

		//Update the number of day spinner
		weatherPanel.getSpinNbDays().setValue(properties.getNbDays());

		//Update the display time JSpinner on the weather panel
		weatherPanel.getDisplayTime().setValue(properties.getDisplayTime());

		//Update the background image chooser on the weather panel
		weatherPanel.getFileChooser().setFile(properties.getBackgroundImage());
		
		// Updates the weather elements in the sequence in case some properties changed
		updateSequenceWeatherElement();
		updateTime();
		updateLocation();
	}

	/** Handles all user actions on the weather panel */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		
		if(source instanceof JCheckBox) {
			JCheckBox checkBox = (JCheckBox) source;
			
			for(WeatherProperties.Type type: WeatherProperties.Type.values()) {
				if(checkBox.equals(weatherPanel.getWeatherTypeCheckBox(type))) {
					if(checkBox.isSelected())
						properties.addActiveType(type);
					else
						properties.removeActiveType(type);
					break;
				}
			}
			updateSequenceWeatherElement();
		}
		else if(source.equals(weatherPanel.getLocationField())) {
			WeatherLocationField field = (WeatherLocationField) e.getSource();
			WeatherLocation location = field.getWeatherLocation();
			properties.setLocation(location);
			
			updateLocationCheckbox();
			
			updateLocation();
		}
		else if(source.equals(weatherPanel.getSpinNbDays())) {
			JSpinner spinner = (JSpinner) source;
			properties.setNbDays((int)spinner.getValue());
		} 
		else if(source.equals(weatherPanel.getDisplayTime())) {
			JSpinner spinner = (JSpinner) source;
			final int displayTime = (int)spinner.getValue();
				
			properties.setDisplayTime(displayTime);
			updateTime();
			updateSequenceWeatherElement();
		}
		else if(source.equals(weatherPanel.getFileChooser())) {
			FileChooserField field = (FileChooserField) source;
			properties.setBackgroundImage(field.getFile());
		}
	}
	
	/** Update location check box depending on the weather location */
	private void updateLocationCheckbox() {
		WeatherLocation location = properties.getLocation();
		
		try {
			boolean enable = false;
			for(WeatherProperties.Type type: WeatherProperties.Type.values()) {
				final JCheckBox cb = weatherPanel.getWeatherTypeCheckBox(type);
				
				switch(type) {
					case CITY: 
						enable = !(location.getCity() == null);
						break;
					case REGIONAL: 
						enable = !(location.getRegion() == null);
						break;
					case NATIONAL: 
						enable = !(location.getCountry() == null);
						break;
				}
				
				cb.setSelected(enable);
				cb.setEnabled(enable);
				
				if(enable) properties.addActiveType(type);
				else properties.removeActiveType(type);
				
				actionPerformed(new ActionEvent(cb, ActionEvent.ACTION_PERFORMED, "auto update"));
				
			}
		} catch(NullPointerException e){}
	}

	/**  */
	private void updateSequenceWeatherElement() {
		
		if(properties.getDisplayTime() > 0) { 
			
			ArrayList<WeatherElement> weatherElements = sequencePanel.getSequence().getElementsByClass(WeatherElement.class);
			
			int nbWeatherElements = properties.getActiveTypes().size();
			int diff = weatherElements.size() - nbWeatherElements;
			
			// Update the number of weather elements in the 
			if(diff > 0) {
				for(int i = 0; i < diff; i++) {
					WeatherElement element = weatherElements.get(weatherElements.size()-1);
					sequencePanel.getSequence().remove(element);
					weatherElements.remove(element);
				}
			}
			else if(diff < 0) {
				for(int i = 0; i < Math.abs(diff); i++) {
					WeatherElement element = new WeatherElement(properties.getLocation(), properties.getDisplayTime());
					sequencePanel.getSequence().add(element);
					weatherElements.add(element);
				}
			}
			
			for(int i = 0; i < weatherElements.size(); i++) {
				WeatherElement element = weatherElements.get(i);
				
				element.setType(properties.getActiveTypes().get(i));
			}
			
			sequencePanel.refreshList();
		} 
		else {
			sequencePanel.getSequence().removeAllElementsByClass(WeatherElement.class);
			sequencePanel.refreshList();
		}
		
	}
	
	/** Updates the display time for each weather elements in the MediaSequence */
	private void updateTime() {
		if(properties.getDisplayTime() > 0) {
			for(MediaElement element : sequencePanel.getSequence().getElementsByClass(WeatherElement.class)) {
				element.setDuration(properties.getDisplayTime());
			}
			
			sequencePanel.refreshList();
		}
	}
	
	/** Updates the displayed location for each weather elements in the MediaSequence */
	private void updateLocation() {
		if(properties.getDisplayTime() > 0) {
			ArrayList<WeatherElement> weatherElements = sequencePanel.getSequence().getElementsByClass(WeatherElement.class);
			for(WeatherElement element : weatherElements) {
				element.setLocation(properties.getLocation());
			}
			
			sequencePanel.refreshList();
		}
	}	

}
