package ui.panel;

import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import ui.LocalizedText;
import ui.form.UnitJSpinner;
import ui.form.filechooser.FileChooserField;
import ui.form.filechooser.MediaFileChooser;
import ui.form.weatherlocation.WeatherLocationField;
import control.WeatherControl;
import data.property.WeatherProperties;

public class WeatherPanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JCheckBox cbNational;
	private final JCheckBox cbRegional;
	private final JCheckBox cbCity;
	private WeatherLocationField locationField;

	private final UnitJSpinner spinNbDays;
	private final UnitJSpinner displayTime;
	private final FileChooserField fileChooser;

	/*
	private final JCheckBox cbCurrentDay;
	private final JCheckBox cbTheNextDay;
	 */
	
	public WeatherPanel(SequencePanel sequencePanel) {
		super(new MigLayout("ins 10", "[130:130:150]10[]", ""), LocalizedText.weather_settings);
		
		Font title = new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 12);
		
		//Location
		JLabel lblLocationTitle = new JLabel(LocalizedText.location);
		lblLocationTitle.setFont(title);
		add(lblLocationTitle, "wrap");
		{
			cbNational = new JCheckBox(LocalizedText.national_weather);
			add(cbNational);
			
			cbRegional = new JCheckBox(LocalizedText.regional_weather);
			add(cbRegional, "wrap");
			
			cbCity = new JCheckBox(LocalizedText.city_weather);
			add(cbCity, "wrap");
			
			//Location
			add(new JLabel(LocalizedText.location+" :"), "ax right");
			locationField = new WeatherLocationField();
			add(locationField, "wrap, wmin 180px");
		}
		
		//Other
		JLabel lblOtherTitle = new JLabel(LocalizedText.others);
		lblOtherTitle.setFont(title);
		add(lblOtherTitle, "wrap");
		{
			// Nb Days
			add(new JLabel(LocalizedText.nb_days_displayed+" :"), "ax right");
			spinNbDays = new UnitJSpinner(LocalizedText.days, 1, 2);
			add(spinNbDays, "wrap");
			
			//Display time
			add(new JLabel(LocalizedText.display_time+" :"), "ax right");
			displayTime = new UnitJSpinner("sec", 0, null);
			add(displayTime, "al left center, wrap");
			
			//Background image select
			add(new JLabel(LocalizedText.background_image+" :"), "al right top");
			fileChooser = new FileChooserField(
				null, 
				new MediaFileChooser(LocalizedText.choose_an_image, MediaFileChooser.Type.IMAGE), 
				LocalizedText.choose_an_image, 
				LocalizedText.no_file_selected
			);
			add(fileChooser);
		}
		
		WeatherControl weatherControl = new WeatherControl(sequencePanel, this);
		for(WeatherProperties.Type type : WeatherProperties.Type.values())
			getWeatherTypeCheckBox(type).addActionListener(weatherControl);
		
		/*
		for(int i = 0; i < 2; i++)
			getDaysCheckBox(i).addActionListener(weatherControl);
		*/
		
		spinNbDays.addActionListener(weatherControl);
		locationField.addActionListener(weatherControl);
		displayTime.addActionListener(weatherControl);
		fileChooser.addActionListener(weatherControl);
	}
	
	/**
	 * Gets a weather type check box given the weather type.
	 */
	public JCheckBox getWeatherTypeCheckBox(WeatherProperties.Type type) {
		switch (type) {
			case CITY: return cbCity;
			case NATIONAL: return cbNational;
			case REGIONAL: return cbRegional;
		}
		return null;
	}
	
	/*
	/**
	 * Gets a day check box given the number of the day.<br/>
	 * Only two days available:<br/>
	 * <ol start="0">
	 * 	<li> => the current day</li>
	 * 	<li> => the next day </li>
	 * </ol>
	
	public JCheckBox getDaysCheckBox(int numDay) {
		switch (numDay) {
			case 0: return cbCurrentDay;
			case 1: return cbCurrentDay;
		}
		return null;
	}*/
	
	public UnitJSpinner getSpinNbDays() {
		return spinNbDays;
	}
	
	public FileChooserField getFileChooser() {
		return fileChooser;
	}
	
	public UnitJSpinner getDisplayTime() {
		return displayTime;
	}
	
	public WeatherLocationField getLocationField() {
		return locationField;
	}
}
