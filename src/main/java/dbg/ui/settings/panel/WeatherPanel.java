package dbg.ui.settings.panel;

import dbg.control.WeatherControl;
import dbg.data.WeatherLocation;
import dbg.ui.LocalizedText;
import dbg.ui.settings.form.UnitJSpinner;
import dbg.ui.settings.form.filechooser.FileChooserField;
import dbg.ui.settings.form.filechooser.MediaFileChooser;
import dbg.ui.settings.form.weatherlocation.WeatherLocationField;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WeatherPanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private final WeatherControl weatherControl;
	//private final JCheckBox cbNational;
	//private final JCheckBox cbRegional;
	//private final JCheckBox cbCity;
	private final JScrollPane locationScrollPane;
	private final JPanel locationsPanel;

	private final UnitJSpinner spinNbDays;
	private final UnitJSpinner displayTime;
	private final FileChooserField fileChooser;

	public WeatherPanel(SequencePanel sequencePanel) {
		super(new MigLayout("ins 10", "[130:130:150]10[grow]", ""), LocalizedText.get("weather_settings"));

		Font title = new Font(UIManager.getDefaults().getFont("Panel.font").getFamily(), Font.BOLD, 12);

		//Location
		JLabel lblLocationTitle = new JLabel(LocalizedText.get("location"));
		lblLocationTitle.setFont(title);
		add(lblLocationTitle, "wrap");
		{
			locationsPanel = new JPanel();
			locationsPanel.setOpaque(false);
			locationsPanel.setLayout(new BoxLayout(locationsPanel, BoxLayout.Y_AXIS));

			final Border fancy = new CompoundBorder(
					// Outside border 1px bottom light color
					new MatteBorder(0, 0, 1, 0, new Color(255, 255, 255)),
					// Border all around panel 1px dark grey
					new LineBorder(new Color(154, 154, 154), 1)
			);

			locationScrollPane = new JScrollPane(locationsPanel);
			locationScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
			locationScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			locationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			locationScrollPane.getViewport().setBackground(Color.white);
			locationScrollPane.setBorder(fancy);
			add(locationScrollPane, "spanx 3, wrap, hmin 85px, growy, growx");

			/*
			cbNational = new JCheckBox(LocalizedText.get("national_weather"));
			add(cbNational);

			cbRegional = new JCheckBox(LocalizedText.get("regional_weather"));
			add(cbRegional, "wrap");

			cbCity = new JCheckBox(LocalizedText.get("city_weather"));
			add(cbCity, "wrap");
			*/
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
		//for(WeatherProperties.Type type : WeatherProperties.Type.values())
		//	getWeatherTypeCheckBox(type).addActionListener(weatherControl);

		spinNbDays.addActionListener(weatherControl);
		displayTime.addActionListener(weatherControl);
		fileChooser.addActionListener(weatherControl);

		weatherControl.refreshUI();
	}

	/*
	 * Gets a weather type check box given the weather type.
	public JCheckBox getWeatherTypeCheckBox(WeatherProperties.Type type) {
		switch (type) {
			case CITY: return cbCity;
			case NATIONAL: return cbNational;
			case REGIONAL: return cbRegional;
		}
		return null;
	}*/
	
	public JSpinner getSpinNbDays() {
		return spinNbDays.getSpinner();
	}
	
	public FileChooserField getFileChooser() {
		return fileChooser;
	}
	
	public JSpinner getDisplayTime() {
		return displayTime.getSpinner();
	}

	public ArrayList<WeatherLocation> getWeatherLocations() {
		ArrayList<WeatherLocation> locations = new ArrayList<WeatherLocation>();
		for (Component component : locationsPanel.getComponents()) {
			if(component instanceof WeatherLocationField) {
				WeatherLocationField field = (WeatherLocationField) component;
				WeatherLocation location = field.getWeatherLocation();
				if(location != null)
					locations.add(location);
			}
		}
		return locations;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			fileChooser.checkError();
			weatherControl.updateSequenceWeatherElement();
		}
	}

	public void setWeatherLocations(List<WeatherLocation> locations) {
		locationsPanel.removeAll();
		for(WeatherLocation location : locations) {
			locationsPanel.add(createLocationField(location));
		}
		locationsPanel.add(createLocationField(null));
		locationsPanel.repaint();
		locationScrollPane.repaint();
	}

	private WeatherLocationField createLocationField(WeatherLocation location) {
		WeatherLocationField field = new WeatherLocationField();
		field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		field.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		if(location != null)
			field.setWeatherLocation(location);
		field.addActionListener(weatherControl);
		return field;
	}

	public void refresh() {
		setWeatherLocations(getWeatherLocations());
	}
}
