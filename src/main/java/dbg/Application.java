package dbg;

import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.SettingsWindow;
import dbg.ui.util.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Application {

	public static void main(String[] args) {
		try {
			//Loads application properties
			PropertyManager.loadFromFile();
			
			SwingUtilities.invokeLater(new Thread() {
				@Override
				public void run() {
					//Initializes the UI with some basic settings
					UiUtils.initUIManager();
					
					//Creating and running setting window
					final SettingsWindow window = SettingsWindow.getInstance();
					SettingsWindow.getInstance().setVisible(true);
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), LocalizedText.get("error"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public static void exit() {
		System.exit(0);
	}

	public static void save() {
		Component activeWindow = SettingsWindow.getInstance();
		if(!activeWindow.isVisible()) activeWindow = null;

		try {
			PropertyManager.saveToFile();
			//JOptionPane.showMessageDialog(activeWindow, LocalizedText.configuration_saved_success, LocalizedText.saving, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(activeWindow, LocalizedText.get("configuration_saved_failed"), LocalizedText.get("saving"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void saveAndExit() {
		save();
		exit();
	}

}
