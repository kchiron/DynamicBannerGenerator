import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import ui.LocalizedText;
import ui.settings.SettingsWindow;
import ui.util.UiUtils;
import data.property.PropertyManager;


public class Application {

	public static void main(String[] args) {
		//Initializes the UI with some basic settings
		UiUtils.initUIManager();
		
		try {
			//Loads application properties
			PropertyManager.loadFromFile();
			
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
						case 2: return;
						case 1: window.exit();
						case 0: window.saveAndExit();
						default: return;
					}
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), LocalizedText.error, JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
