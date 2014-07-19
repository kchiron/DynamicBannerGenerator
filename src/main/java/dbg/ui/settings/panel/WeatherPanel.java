package dbg.ui.settings.panel;

import dbg.control.WeatherControl;
import dbg.data.property.WeatherProperties;
import dbg.ui.LocalizedText;
import dbg.ui.settings.form.UnitJSpinner;
import dbg.ui.settings.form.filechooser.FileChooserField;
import dbg.ui.settings.form.filechooser.MediaFileChooser;
import dbg.ui.settings.form.weatherlocation.WeatherLocationField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class WeatherPanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private final WeatherControl weatherControl;
	private final JCheckBox cbNational;
	private final JCheckBox cbRegional;
	private final JCheckBox cbCity;
	private final WeatherLocationField locationField;

	private final UnitJSpinner spinNbDays;
	private final UnitJSpinner displayTime;
	private final FileChooserField fileChooser;

	public WeatherPanel(SequencePanel sequencePanel) {
		super(new MigLayout("ins 10", "[130:130:150]10[]", ""), LocalizedText.get("weather_settings"));

		Font title = new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 12);

		//Location
		JLabel lblLocationTitle = new JLabel(LocalizedText.get("location"));
		lblLocationTitle.setFont(title);
		add(lblLocationTitle, "wrap");
		{
			cbNational = new JCheckBox(LocalizedText.get("national_weather"));
			add(cbNational);

			cbRegional = new JCheckBox(LocalizedText.get("regional_weather"));
			add(cbRegional, "wrap");

			cbCity = new JCheckBox(LocalizedText.get("city_weather"));
			add(cbCity, "wrap");

			//Location
			add(new JLabel(LocalizedText.get("location") + " :"), "ax right");
			locationField = new WeatherLocationField();
			add(locationField, "wrap, wmin 180px");
		}

		//Other
		JLabel lblOtherTitle = new JLabel(LocalizedText.get("others"));
		lblOtherTitle.setFont(title);
		add(lblOtherTitle, "wrap");
		{
			// Nb Days
			add(new JLabel(LocalizedText.get("nb_days_displayed") + " :"), "ax right");
			spinNbDays = new UnitJSpinner(LocalizedText.get("days"), 1, 2);
			add(spinNbDays, "wrap");

			//Display time
			add(new JLabel(LocalizedText.get("display_time") + " :"), "ax right");
			displayTime = new UnitJSpinner("sec", 0, null);
			add(displayTime, "al left center, wrap");

			//Background image select
			add(new JLabel(LocalizedText.get("background_image") + " :"), "al right top");
			fileChooser = new FileChooserField(
				null,
				new MediaFileChooser(LocalizedText.get("choose_an_image"), MediaFileChooser.Type.IMAGE),
				LocalizedText.get("choose_an_image"),
				LocalizedText.get("no_file_selected")
			);
			add(fileChooser);
		}

		weatherControl = new WeatherControl(sequencePanel, this);
		for(WeatherProperties.Type type : WeatherProperties.Type.values())
			getWeatherTypeCheckBox(type).addActionListener(weatherControl);
		
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
	
	public JSpinner getSpinNbDays() {
		return spinNbDays.getSpinner();
	}
	
	public FileChooserField getFileChooser() {
		return fileChooser;
	}
	
	public JSpinner getDisplayTime() {
		return displayTime.getSpinner();
	}
	
	public WeatherLocationField getLocationField() {
		return locationField;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			fileChooser.checkError();
			weatherControl.updateSequenceWeatherElement();
		}
	}
}
