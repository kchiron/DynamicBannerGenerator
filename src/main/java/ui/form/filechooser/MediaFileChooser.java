package ui.form.filechooser;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ui.LocalizedText;
import ffmpeg.SupportedFileFormat;

/**
 * A JFileChooser specialized for video and image file selection
 * @author gcornut
 */
public class MediaFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;
	
	private static final FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
		LocalizedText.image+" ("+SupportedFileFormat.getImageFormatsString()+")", 
		SupportedFileFormat.getImageFormats()
	);
	
	private static final FileNameExtensionFilter videoFilter = new FileNameExtensionFilter(
		LocalizedText.video+" ("+SupportedFileFormat.getVideoFormatsString()+")", 
		SupportedFileFormat.getVideoFormats()
	);

	public MediaFileChooser(String title, Type... type) {
		super(new File("."));
		setName(title);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setAcceptAllFileFilterUsed(false);

		for(Type t: type) {
			if(t == Type.IMAGE)
				addChoosableFileFilter(imageFilter);
			else if(t == Type.VIDEO)
				addChoosableFileFilter(videoFilter);
		}
		setFileFilter(type[0] == Type.IMAGE ? imageFilter : videoFilter);
	}

	public enum Type {VIDEO, IMAGE}
}
