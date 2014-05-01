package dbg.ui.settings.form.weatherlocation;

import com.claygregory.api.google.places.*;
import dbg.data.WeatherLocation;
import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.form.PlaceHolder;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Weather location text field handling search and confirmation of the typed location (using Google APIs)
 */
public class WeatherLocationField extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final ArrayList<ActionListener> customListeners;
	private final GooglePlaces places;
	
	//Components
	private final JTextField txtLocation;
	private final WeatherLocationDropDown dropDown;

	//Weather weatherLocation
	private WeatherLocation weatherLocation;

	private final CompoundBorder defaultBorder;
	private final CompoundBorder errorBorder;
	
	public WeatherLocationField() {
		super(new BorderLayout());

		places = new GooglePlaces(PropertyManager.getGooglePlacesAPIKey());
		customListeners = new ArrayList<>(1);
		dropDown = new WeatherLocationDropDown(this);
		
		defaultBorder = new CompoundBorder(new LineBorder(new Color(0, 0, 0, 70)), new EmptyBorder(2, 5, 2, 5));
		errorBorder = new CompoundBorder(new LineBorder(new Color(255, 0 , 0, 150)), new EmptyBorder(2, 5, 2, 5));

		{ // Components
			txtLocation = new JTextField();
			PlaceHolder placeHolder = new PlaceHolder(LocalizedText.get("city_zip_code_state"), txtLocation, 0.5f);
			placeHolder.changeStyle(Font.ITALIC);
			add(txtLocation, BorderLayout.CENTER);
		}
		
		// Catch text entering
		txtLocation.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				showDropDown(e);
			}
			
			public void insertUpdate(DocumentEvent e) {
				if(e.getLength() != txtLocation.getText().length())
					showDropDown(e);
			}
			
			public void changedUpdate(DocumentEvent e) {
				showDropDown(e);
			}
		});
		
		// Catch special keys
		txtLocation.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_UP:
						dropDown.up();
						break;
					case KeyEvent.VK_DOWN:
						dropDown.down();
						break;
					case KeyEvent.VK_ENTER:
						WeatherLocationDropDown.PredictionMenuItem it = dropDown.getSelectedItem();
						if(it != null) {
							for(ActionListener listener: it.getActionListeners())
								listener.actionPerformed(new ActionEvent(it, ActionEvent.ACTION_PERFORMED, "weatherLocation validation"));	
						}
						break;
				}
			}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
		
		checkValidLocation();
	}

	private void checkValidLocation() {
		if(weatherLocation == null) {
			txtLocation.setBorder(errorBorder);
			txtLocation.revalidate();
		} else {
			txtLocation.setBorder(defaultBorder);
			txtLocation.revalidate();
		}
	}
	
	private void showDropDown(final DocumentEvent e) {
		if(e.getDocument().getLength() > 0) {
			
			if(!dropDown.isVisible()) { 
				Rectangle r = txtLocation.getBounds();
				dropDown.show(txtLocation, (r.x), (r.y+r.height));
				dropDown.setVisible(true);
				
				weatherLocation = null;
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					dropDown.removeAll();
					dropDown.add("Loading...");
					dropDown.pack();
					dropDown.revalidate();
					
					if(e.getDocument().getLength() == 2) {
						txtLocation.setSelectionStart(2);
						txtLocation.setSelectionEnd(2);
					}
					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AutocompleteQueryOptions options = new AutocompleteQueryOptions();
							options.param("components", "country:fr");
							options.param("language", "fr");
							AutocompleteResult result = places.autocomplete(txtLocation.getText(), options, false);
							
							if(result.isOkay()) {
								dropDown.removeAll();
								for (Prediction p : result)
									dropDown.add(p);
								dropDown.pack();
								dropDown.revalidate();
								checkValidLocation();
							} else {
								//Google connection error
								//dropDown.setVisible(false);
								dropDown.removeAll();
								dropDown.add("Error...");
								dropDown.pack();
								dropDown.revalidate();
							}
							
						}
					});
				}
			});
			
			txtLocation.requestFocus();
		}
		else {
			dropDown.setVisible(false);
		}
	}
	
	public WeatherLocation weatherLocationFromPrediction(Prediction p) {
		WeatherLocation weatherLocation;
		PlaceDetailResult res = places.detail(p.getReference(), false);
		
		if(res.isOkay()) {
			PlaceDetail detail = res.getResult();
			float longitute = detail.getGeometry().getLocation().getLongitude();
			float latitude = detail.getGeometry().getLocation().getLatitude();
			String country = null;
			String region = null;
			String city = null;
			
			String postal = null; //in case the region is not set
			for(AddressComponent adr: detail.getAddressComponents()) {
				//System.out.println(adr.getLongName()+"-"+adr.getTypes());

				if(adr.getTypes().contains("political")) {
					if(adr.getTypes().contains("locality"))
						city = adr.getLongName();
					else if(adr.getTypes().contains("administrative_area_level_1"))
						region = adr.getLongName();
					else if(adr.getTypes().contains("country"))
						country = adr.getLongName();
				}
				else if(adr.getTypes().contains("postal_code")) 
					postal = adr.getLongName();
			}
			
			//If region missing while city registered, redo a search
			if(city != null && region == null) {
				if(postal != null) {
					AutocompleteQueryOptions options = new AutocompleteQueryOptions();
					options.param("components", "country:fr");
					options.param("language", "fr");
					AutocompleteResult result = places.autocomplete(postal, options, false);
					
					if(result.isOkay()) {
						return weatherLocationFromPrediction(result.iterator().next());
					}
				}
			}
			
			weatherLocation = new WeatherLocation(country, region, city, longitute, latitude);
		}
		else weatherLocation = null;
		
		return weatherLocation;
	}

	public void addActionListener(ActionListener listener) {
		customListeners.add(listener);
	}

	public WeatherLocation getWeatherLocation() {
		return weatherLocation;
	}
	
	public void setWeatherLocation(WeatherLocation weatherLocation) {
		this.weatherLocation = weatherLocation;

		checkValidLocation();
		for(ActionListener listener: customListeners)
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "weatherLocation validation"));
		
		txtLocation.setText(weatherLocation.toString());
		dropDown.setVisible(false);
	}
}
