package dbg;

import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.SettingsWindow;
import dbg.ui.systemtraymenu.SystemTrayMenu;
import dbg.ui.util.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Application {

	private static Application _instance;
	private final SettingsWindow settingsWindow;
	private final SystemTrayMenu systemTrayMenu;

	public static Application getInstance() {
		if (_instance == null)
			_instance = new Application();
		return _instance;
	}

	private Application() {
		//Loads application properties
		try {
			PropertyManager.loadFromFile();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), LocalizedText.get("error"), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

		UiUtils.initUIManager();

		settingsWindow = new SettingsWindow();
		settingsWindow.setVisible(true);

		systemTrayMenu = SystemTray.isSupported() ? new SystemTrayMenu() : null;
	}

	public static void main(String[] args) {
		Application.getInstance();
	}

	public void exit() {
		System.exit(0);
	}

	public void save() {
		Component activeWindow = settingsWindow;
		if (!activeWindow.isVisible())
			activeWindow = null;

		try {
			PropertyManager.saveToFile();
			//JOptionPane.showMessageDialog(activeWindow, LocalizedText.configuration_saved_success, LocalizedText.saving, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(activeWindow, LocalizedText.get("configuration_saved_failed"), LocalizedText.get("saving"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void saveAndExit() {
		save();
		exit();
	}

	public SettingsWindow getSettingWindows() {
		return settingsWindow;
	}

	public SystemTrayMenu getSystemTrayMenu() {
		return systemTrayMenu;
	}
}
