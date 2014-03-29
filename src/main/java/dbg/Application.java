package dbg;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.SettingsWindow;
import dbg.ui.util.UiUtils;

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
					final SettingsWindow window = new SettingsWindow();
					window.setVisible(true);
					
					//Prevent window closing 
					window.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent paramWindowEvent) {
							window.requestFocus();
							window.toFront();
							
							switch (
								JOptionPane.showOptionDialog(
									window, 
									LocalizedText.confirm_close, 
									"", 
									JOptionPane.YES_NO_CANCEL_OPTION, 
									JOptionPane.WARNING_MESSAGE,
									null, 
									new String[]{LocalizedText.save, LocalizedText.quit, LocalizedText.cancel},
									LocalizedText.save)
							) {
								case 1: window.dispose();
								case 0: window.saveAndExit();
								default: return;
							}
						}
					});
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), LocalizedText.error, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
