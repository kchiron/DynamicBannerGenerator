package ui.form.weatherlocation;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;

public class WeatherLocationDropDown extends JPopupMenu {

	private static final long serialVersionUID = 1L;
	private ArrayList<String> locations;
	
	public WeatherLocationDropDown() {
		locations = new ArrayList<String>();
	}
	
	public void updateList(ArrayList<String> locations) {
		removeAll();
		for(String location: locations)
			add(location);
		revalidate();
	}
	
	@Override
	public void removeAll() {
		locations.clear();
		super.removeAll();
	}
	
	@Override
	public JMenuItem add(String s) {
		locations.add(s);
		return super.add(s);
	}
	
	public void up() {
		move(-1);
	}
	
	public void down() {
		move(1);
	}
	
	/** Moves selected menu item */
	private void move(int offset) {
		MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
		
		if(selectedPath.length != 0) {
			for(MenuElement elem: selectedPath) {
				Component c = elem.getComponent();
				
				if(c instanceof JMenuItem) {
					int pos = locations.indexOf(((JMenuItem)c).getText());
					
					if(pos <= 0 && offset < 0) return;
					if(pos >= locations.size() && offset > 0) return;
					
					JMenuItem newSelected = (JMenuItem)getComponent(pos+offset);
					
					MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{this, newSelected});
				}
			}
		}
		else MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{this, (JMenuItem)getComponent(0)});
		System.out.println("ok");
	}
}