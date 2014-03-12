package dbg.ui.settings.form.weatherlocation;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

import com.claygregory.api.google.places.Prediction;

public class WeatherLocationDropDown extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	private final ActionListener menuAction;
	private int pos;
	
	public WeatherLocationDropDown(final WeatherLocationField parent) {
		menuAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof PredictionMenuItem) {
					PredictionMenuItem it = (PredictionMenuItem)e.getSource();
					parent.setWeatherLocation(parent.weatherLocationFromPrediction(it.getPrediction()));
				}
			}
		};
	}
	
	public void updateList(ArrayList<String> locations) {
		removeAll();
		for(String location: locations)
			add(location);
		revalidate();
	}
	
	@Override
	public void removeAll() {
		super.removeAll();
	}
	
	public JMenuItem add(Prediction s) {
		return super.add(new PredictionMenuItem(s));
	}
	
	public PredictionMenuItem getSelectedItem() {
		MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();

		if(selectedPath.length != 0 && isVisible()) { 
			Component c = selectedPath[selectedPath.length-1].getComponent();
			
			if(c instanceof PredictionMenuItem) {
				return (PredictionMenuItem) c;
			}
			else return null;
		}
		else return null;
	}
	
	public void up() {
		move(-1);
	}
	
	public void down() {
		move(1);
	}
	
	/** Moves selected menu item if the menu is opened */
	private void move(int offset) {
		MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();

		if(selectedPath.length != 0 && isVisible()) {
			Component c = selectedPath[selectedPath.length-1].getComponent();
			JMenuItem newSelected = null;
			
			if(c instanceof JMenuItem) {
				if(pos <= 0 && offset < 0) return;
				if(pos >= getComponents().length && offset > 0) return;
				pos += offset;
				
				newSelected = (JMenuItem)getComponent(pos);
			}
			else if(c instanceof JPopupMenu) {
				pos = 0;
				newSelected = (JMenuItem)getComponent(0);
			}
			
			if(newSelected != null) {
				MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{this, newSelected});
			}
		}
	}
	
	class PredictionMenuItem extends JMenuItem {
		
		private static final long serialVersionUID = 1L;
		private final Prediction prediction;
		
		public PredictionMenuItem(Prediction prediction) {
			super(prediction.getDescription());
			this.prediction = prediction;
			addActionListener(menuAction);
		}
		
		public Prediction getPrediction() {
			return prediction;
		}
	}
}