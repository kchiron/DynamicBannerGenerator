package ui;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public ImageFileChooser() {
		super();
		setFileFilter(new FileNameExtensionFilter(LocalizedText.image, "png", "jpg", "jpeg"));
		
		UIManager.put("FileChooser.saveButtonText", LocalizedText.save);
		UIManager.put("FileChooser.openButtonText", LocalizedText.open);
		UIManager.put("FileChooser.cancelButtonText", LocalizedText.cancel);
		UIManager.put("FileChooser.updateButtonText", LocalizedText.reload);
		UIManager.put("FileChooser.helpButtonText", LocalizedText.help);
		UIManager.put("FileChooser.saveButtonToolTipText", LocalizedText.save_the_file);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
	}

}
