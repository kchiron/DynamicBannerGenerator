package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JSpinner;

import ui.LocalizedText;
import ui.form.filechooser.FileChooserField;
import ui.panel.HoroscopePanel;
import ui.panel.SequencePanel;
import data.media.element.MediaElement;
import data.media.element.generated.HoroscopeElement;
import data.media.element.generated.WeatherElement;
import data.property.HoroscopeProperties;
import data.property.PropertyManager;

/**
 * Class handling all user action on the horoscope panel and loading saved properties to the horoscope panel
 */
public class HoroscopeControl implements ActionListener {

	private final SequencePanel sequencePanel;
	private final HoroscopePanel horoscopePanel;
	
	private HoroscopeProperties properties;
	
	public HoroscopeControl(SequencePanel sequencePanel, HoroscopePanel horoscopePanel) {
		this.sequencePanel = sequencePanel;
		this.horoscopePanel = horoscopePanel;
		
		properties = PropertyManager.getHoroscopeProperties();
		this.horoscopePanel.getDisplayTime().setValue(properties.getDisplayTime());
		this.horoscopePanel.getSignsPerPage().setValue(properties.getSignPerPage());
		this.horoscopePanel.getFileChooser().setFile(properties.getBackgroundImage());
		
		updateSequenceHoroscopeElement();
		updateTime();
	}
	
	/** Handles all user action on the horoscope panel */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		
		if(source.equals(horoscopePanel.getSignsPerPage())) {
			final int signPerPage = (int)((JSpinner)source).getValue();
			properties.setSignPerPage(signPerPage);
			updateSequenceHoroscopeElement();
		}
		else if(source.equals(horoscopePanel.getDisplayTime())) {
			final int displayTime = (int)((JSpinner)source).getValue();
			
			if(displayTime > 0 && properties.getDisplayTime() <= 0) {
				// If the displayTime turns into positive
				
				properties.setDisplayTime(displayTime);	
				updateSequenceHoroscopeElement();
			}
			else if(displayTime <= 0 && properties.getDisplayTime() > 0) {
				// If the displayTime turns into negative
				
				sequencePanel.getSequence().removeAllElementsByClass(HoroscopeElement.class);
				sequencePanel.refreshList();
			}
			
			properties.setDisplayTime(displayTime);	
			updateTime();
		}
		else if(source.equals(horoscopePanel.getFileChooser())) {
			properties.setBackgroundImage(((FileChooserField)source).getFile());
		}
	}
	
	/** Updates the media sequence with the correct number of horoscope pages to display depending on the number of signs per page */
	private void updateSequenceHoroscopeElement() {
		
		if(properties.getDisplayTime() > 0) {
			ArrayList<HoroscopeElement> horoscopeElements = sequencePanel.getSequence().getElementsByClass(HoroscopeElement.class);
			
			int nbHoroscopeElement = (int) Math.ceil((double)Signs.values().length/(double)properties.getSignPerPage());
			int diff = horoscopeElements.size() - nbHoroscopeElement;
			
			if(diff > 0) {
				for(int i = 0; i < diff; i++) {
					HoroscopeElement element = horoscopeElements.get(horoscopeElements.size()-1);
					sequencePanel.getSequence().remove(element);
					horoscopeElements.remove(element);
				}
			}
			else if(diff < 0) {
				for(int i = 0; i < Math.abs(diff); i++) {
					HoroscopeElement element = new HoroscopeElement(properties.getDisplayTime());
					sequencePanel.getSequence().add(element);
					horoscopeElements.add(element);
				}
			}
			
			final Signs[] signs = Signs.values();
			final Signs[] elementSigns = new Signs[properties.getSignPerPage()];
			for(int i = 0; i < horoscopeElements.size(); i++) {
				HoroscopeElement element = horoscopeElements.get(i);
				
				for(int j = 0; j < properties.getSignPerPage(); j++) {
					try {
						elementSigns[j] = signs[i*properties.getSignPerPage()+j];
					} catch (ArrayIndexOutOfBoundsException e) {
						elementSigns[j] = null;
					}
				}
				
				element.setSigns(elementSigns);
			}
			
			sequencePanel.refreshList();
		}
	}
	
	/** Updates the display time for each horoscope elements in the MediaSequence */
	private void updateTime() {
		if(properties.getDisplayTime() > 0) {
			for(MediaElement element : sequencePanel.getSequence().getElementsByClass(HoroscopeElement.class)) {
				element.setDuration(properties.getDisplayTime());
			}
			
			sequencePanel.refreshList();
		}
	}
	
	/** Horoscope signs enum */
	public enum Signs {
		ARIES(LocalizedText.aries),
		TAURUS(LocalizedText.taurus),
		GEMINI(LocalizedText.gemini),
		CANCER(LocalizedText.cancer),
		LEO(LocalizedText.leo),
		VIRGO(LocalizedText.virgo),
		LIBRA(LocalizedText.libra),
		SCORPIO(LocalizedText.scorpio),
		SAGITTARIUS(LocalizedText.sagittarius),
		CAPRICORN(LocalizedText.capricorn),
		AQUARIUS(LocalizedText.aquarius),
		PISCES(LocalizedText.pisces);
		
		private final String name;
		
		private Signs(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
}
