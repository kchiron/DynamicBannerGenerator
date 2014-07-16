package dbg.ui.systemtraymenu;

import dbg.Application;
import dbg.ui.LocalizedText;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SystemTrayMenu extends PopupMenu {
	// Initialize System tray menu
	public SystemTrayMenu() {
		// Maximize button
		MenuItem maximizeSettingsWindow = new MenuItem(LocalizedText.get("open_setting_window"));
		maximizeSettingsWindow.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Application.getInstance().getSettingWindows().maximize();
			}
		});
		add(maximizeSettingsWindow);

		// Quit button
		MenuItem quit = new MenuItem(LocalizedText.get("action.quit"));
		quit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Application.getInstance().exit();
			}
		});
		add(quit);

		// Menu icon
		final TrayIcon trayIcon = new TrayIcon(new ImageIcon(SystemTrayMenu.class.getResource("systemTrayMenuIcon.png")).getImage());
		trayIcon.setPopupMenu(this);

		try {
			SystemTray.getSystemTray().add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
