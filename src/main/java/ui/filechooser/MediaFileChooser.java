	package ui.filechooser;

import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.LocalizedText;
import ui.settings.SettingsWindow;

/**
 * A JFileChooser specialized for video and image file selection
 * @author gcornut
 */
public class MediaFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	public MediaFileChooser(String title, Type... type) {
		super(new File("."));
		setName(title);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
	    setAcceptAllFileFilterUsed(false);
	    for(Type t: type) {
		    if(t == Type.IMAGE)
		    	addChoosableFileFilter(new FileNameExtensionFilter(LocalizedText.image+" (png, jpg, jpeg)", "png", "jpg", "jpeg"));
		    else if(t == Type.VIDEO)
		    	addChoosableFileFilter(new FileNameExtensionFilter(LocalizedText.video+" (mp4, wmv, mpeg4, avi)", "mp4", "wmv", "mpeg4", "avi", "ts"));	
	    }
	}

	public enum Type {VIDEO, IMAGE}
}
