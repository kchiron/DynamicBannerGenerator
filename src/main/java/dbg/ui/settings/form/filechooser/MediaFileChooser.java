package dbg.ui.settings.form.filechooser;

import dbg.ffmpeg.SupportedFileFormat;
import dbg.ui.LocalizedText;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

/**
 * A JFileChooser specialized for video and image file selection
 *
 * @author gcornut
 */
public class MediaFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;

	private static final FileNameExtensionFilter imageFilter = new FileNameExtensionFilter(
			LocalizedText.get("image") + " (" + SupportedFileFormat.getImageFormatsString() + ")",
			SupportedFileFormat.getImageFormats()
	);

	private static final FileNameExtensionFilter videoFilter = new FileNameExtensionFilter(
			LocalizedText.get("video") + " (" + SupportedFileFormat.getVideoFormatsString() + ")",
			SupportedFileFormat.getVideoFormats()
	);

	public MediaFileChooser(String title, Type... type) {
		super(new File("."));
		setName(title);
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setAcceptAllFileFilterUsed(false);

		for (Type t : type) {
			if (t == Type.IMAGE)
				addChoosableFileFilter(imageFilter);
			else if (t == Type.VIDEO)
				addChoosableFileFilter(videoFilter);
		}
		setFileFilter(type[0] == Type.IMAGE ? imageFilter : videoFilter);
	}

	public enum Type {VIDEO, IMAGE}
}
