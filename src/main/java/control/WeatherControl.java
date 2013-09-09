package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import ui.panel.SequencePanel;
import ui.panel.WeatherPanel;
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
		
		//Update the days check boxes on the weather panel
		for(int i = 0; i < properties.getNbDays(); i++) {
			weatherPanel.getDaysCheckBox(i).setSelected(true);
		}

		//Update the weather location field on the weather panel
		if(properties.getLocation() != null) {
			weatherPanel.getLocationField().setWeatherLocation(properties.getLocation());
		}

		//Update the display time JSpinner on the weather panel
		weatherPanel.getDisplayTime().setValue(properties.getDisplayTime());

		//Update the background image chooser on the weather panel
		weatherPanel.getFileChooser().setFile(properties.getBackgroundImage());
	}

	/** Handles all user actions on the weather panel*/
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JCheckBox) {
			JCheckBox source = (JCheckBox) e.getSource();
			
			for(WeatherProperties.Type type: WeatherProperties.Type.values()) {
				if(source.equals(weatherPanel.getWeatherTypeCheckBox(type))) {
					if(source.isSelected())
						properties.addActiveType(type);
					else
						properties.removeActiveType(type);
					return;
				}
			}
			
			int nbDays = 0;
			for(int i = 0; i < properties.getNbDays(); i++) {
				if(weatherPanel.getDaysCheckBox(i).isSelected())
					nbDays++;
			}
			properties.setNbDays(nbDays);
			return;
		}

		
	}

}
