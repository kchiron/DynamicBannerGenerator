package ui.form.weatherlocation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ui.LocalizedText;
import ui.form.PlaceHolder;

import com.claygregory.api.google.places.AutocompleteResult;
import com.claygregory.api.google.places.GooglePlaces;
import com.claygregory.api.google.places.Prediction;

import data.property.PropertyManager;

/**
 * Weather location text field handling search and confirmation of the typed location (using Google APIs)
 */
public class WeatherLocationField extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final ArrayList<ActionListener> customListeners;
	
	//Components
	private final JTextField txtLocation;
	private final JLabel lblStatus;
	private final WeatherLocationDropDown dropDown;

	//Weather weatherLocation
	private WeatherLocation weatherLocation;
	
	public WeatherLocationField() {
		super(new BorderLayout());
		
		customListeners = new ArrayList<ActionListener>(1);

		txtLocation = new JTextField();
		PlaceHolder placeHolder = new PlaceHolder(LocalizedText.city_zip_code_state, txtLocation, 0.5f);
		placeHolder.changeStyle(Font.ITALIC);
		add(txtLocation, BorderLayout.CENTER);

		lblStatus = new JLabel();

		dropDown = new WeatherLocationDropDown();
		
		//Catch text entering
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
		
		//Catch special keys
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
						for(ActionListener listener: customListeners)
							listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "weatherLocation validation"));
						break;
				}
			}
			
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
	}

	private void showDropDown(DocumentEvent e) {
		if(e.getDocument().getLength() > 0) {
			if(!dropDown.isVisible()) { 
				Rectangle r = txtLocation.getBounds();
				dropDown.show(txtLocation, (r.x), (r.y+r.height));
				dropDown.setVisible(true);
			}
			
			dropDown.removeAll();
			dropDown.add("Loading...");
			dropDown.pack();
			dropDown.revalidate();

			GooglePlaces places = new GooglePlaces(PropertyManager.getGooglePlacesAPIKey());
			AutocompleteResult result = places.autocomplete(txtLocation.getText(), false);

			dropDown.removeAll();
			for (Prediction p : result)
				dropDown.add(p.getDescription());
			dropDown.pack();
			dropDown.revalidate();

			txtLocation.requestFocus();
		}
		else {
			dropDown.setVisible(false);
		}
	}
 	
	private void searchLocation(String loc) {
		GooglePlaces places = new GooglePlaces(PropertyManager.getGooglePlacesAPIKey());
		AutocompleteResult result = places.autocomplete(loc,  false );

		for (Prediction p : result)
			System.out.println(p.getDescription());
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
