package ui.panel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import ui.ImageFileChooser;
import ui.LocalizedText;
import ui.custom.TabContentPanel;
import ui.custom.PlaceHolder;
import ui.custom.UnitJSpinner;
import net.miginfocom.swing.MigLayout;

public class WeatherPanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	
	private final JTextField txtLocation;
	private final JCheckBox cbNational;
	private final JCheckBox cbRegional;
	private final JCheckBox cbCity;
	private final JCheckBox cbCurrentDay;
	private final JCheckBox cbTheNextDay;
	
	private final ImageFileChooser backgroundImageFile;
	
	public WeatherPanel() {
		super(new MigLayout("ins 10"), LocalizedText.weather_settings);
		
		//Location
		add(new JLabel(LocalizedText.location), "wrap");
		{
			cbNational = new JCheckBox(LocalizedText.national_weather);
			add(cbNational);
			
			cbRegional = new JCheckBox(LocalizedText.regional_weather);
			add(cbRegional, "wrap");
			
			cbCity = new JCheckBox(LocalizedText.city_weather);
			add(cbCity, "wrap");
		}
		
		//Displayed days
		add(new JLabel(LocalizedText.displayed_days), "wrap");
		{
			cbCurrentDay = new JCheckBox(LocalizedText.current_day);
			add(cbCurrentDay);
			
			cbTheNextDay = new JCheckBox(LocalizedText.the_next_day);
			add(cbTheNextDay, "wrap");
		}
		
		//Other
		add(new JLabel(LocalizedText.other), "wrap");
		{
			add(new JLabel(LocalizedText.location+" :"), "alignx right,gapx 10px 10px");
			
			txtLocation = new JTextField();
			PlaceHolder placeHolder = new PlaceHolder(LocalizedText.city_zip_code_state, txtLocation);
			placeHolder.changeAlpha(0.5f);
			placeHolder.changeStyle(Font.ITALIC);
			add(txtLocation, "wrap, grow, span 2");
			
			add(new JLabel(LocalizedText.display_time+" :"), "alignx right,gapx 10px 10px");
			add(new UnitJSpinner("sec"), "alignx left,aligny center,wrap");
			
			backgroundImageFile = new ImageFileChooser();
			final JButton btnSelectImage = new JButton(LocalizedText.select_an_image);
			btnSelectImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.out.println(backgroundImageFile.showOpenDialog(null));
				}
	
			});
			
			add(new JLabel(LocalizedText.background_image+" :"), "alignx right,gapx 10px 10px");
			add(btnSelectImage, "wrap, span 2");
		}
	}
}
