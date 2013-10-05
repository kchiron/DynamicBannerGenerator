package ui.form.weatherlocation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.LocalizedText;
import ui.form.PlaceHolder;

import com.claygregory.api.google.places.AddressComponent;
import com.claygregory.api.google.places.AutocompleteQueryOptions;
import com.claygregory.api.google.places.AutocompleteResult;
import com.claygregory.api.google.places.GooglePlaces;
import com.claygregory.api.google.places.PlaceDetail;
import com.claygregory.api.google.places.PlaceDetailResult;
import com.claygregory.api.google.places.Prediction;

import data.property.PropertyManager;

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
		customListeners = new ArrayList<ActionListener>(1);
		dropDown = new WeatherLocationDropDown(this);
		
		defaultBorder = new CompoundBorder(new LineBorder(new Color(0, 0, 0, 70)), new EmptyBorder(2, 5, 2, 5));
		errorBorder = new CompoundBorder(new LineBorder(new Color(255, 0 , 0, 150)), new EmptyBorder(2, 5, 2, 5));

		{ // Components
			txtLocation = new JTextField();
			PlaceHolder placeHolder = new PlaceHolder(LocalizedText.city_zip_code_state, txtLocation, 0.5f);
			placeHolder.changeStyle(Font.ITALIC);
			add(txtLocation, BorderLayout.CENTER);
		}
		
		// Catch text entering
		txtLocation.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				showDropDown(e);
			}
			
			public void insertUpdate(DocumentEvent e) {
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
				
				if(weatherLocation != null) {
					weatherLocation = null;
				}
			}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					dropDown.removeAll();
					dropDown.add("Loading...");
					dropDown.pack();
					dropDown.revalidate();
					
					if(e.getDocument().getLength() == 1) {
						txtLocation.setSelectionStart(1);
						txtLocation.setSelectionEnd(1);
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
	
	public void setWeatherLocationFromPrediction(Prediction p) {
		PlaceDetailResult res = places.detail(p.getReference(), false);
		
		if(res.isOkay()) {
			PlaceDetail detail = res.getResult();
			float longitute = detail.getGeometry().getLocation().getLongitude();
			float latitude = detail.getGeometry().getLocation().getLatitude();
			String country = "";
			String region = "";
			String city = "";
			
			/*if(!detail.getTypes().contains("geocode")) {
				detail = places.
			}*/
			
			for(AddressComponent adr: detail.getAddressComponents()) {
				if(adr.getTypes().contains("political")) {
					if(adr.getTypes().contains("locality"))
						city = adr.getLongName();
					else if(adr.getTypes().contains("administrative_area_level_1"))
						region = adr.getLongName();
					else if(adr.getTypes().contains("country"))
						country = adr.getLongName();
				}
			}
			
			weatherLocation = new WeatherLocation(country, region, city, longitute, latitude);
		}
		else weatherLocation = null;
		
		checkValidLocation();
		for(ActionListener listener: customListeners)
			listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "weatherLocation validation"));
		
		txtLocation.setText(weatherLocation.toString());
		dropDown.setVisible(false);
	}

	public void addActionListener(ActionListener listener) {
		customListeners.add(listener);
	}

	public WeatherLocation getWeatherLocation() {
		return weatherLocation;
	}
	
	public void setWeatherLocation(WeatherLocation weatherLocation) {
		this.weatherLocation = weatherLocation;
	}
}