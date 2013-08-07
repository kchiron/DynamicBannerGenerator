	package ui.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.LocalizedText;
import ui.SettingsWindow;

public class ImageFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public ImageFileChooser(String title) {
		super(new File("."));
		setName(title);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
	    setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileNameExtensionFilter(LocalizedText.image+" (png, jpg, jpeg)", "png", "jpg", "jpeg"));
	}

}
