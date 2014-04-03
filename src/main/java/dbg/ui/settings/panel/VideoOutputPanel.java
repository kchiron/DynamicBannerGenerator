package dbg.ui.settings.panel;

import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import dbg.control.VideoOutputControl;
import dbg.data.property.VideoOutputProperties;
import dbg.ui.LocalizedText;
import dbg.ui.settings.form.filechooser.FileChooserField;
import dbg.ui.settings.form.filechooser.FolderChooser;
import net.miginfocom.swing.MigLayout;

/**
 * Setting panel for the video output
 * @author gcornut
 */
public class VideoOutputPanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private final JComboBox<String> cbVideoSize;
	private final FileChooserField fileChooser;

	public VideoOutputPanel() {
		super(new MigLayout("ins 10", "[150:150:150]10[]", ""), LocalizedText.video_output_settings);

		{//Video size select
			add(new JLabel(LocalizedText.video_size+" :"), "alignx right");

			cbVideoSize = new JComboBox<>(VideoOutputProperties.getStandardVideoSize());
			add(cbVideoSize, "wrap");	
		}

		{//Video output folder select
			add(new JLabel(LocalizedText.video_output_folder+" :"), "align right top");

			File currentDirectory = null;
			try {
				currentDirectory = new File(new File("."+File.separator).getCanonicalPath());
			} catch (IOException e) {e.printStackTrace();}
			
			fileChooser = new FileChooserField(
				currentDirectory,
				new FolderChooser(LocalizedText.choose_a_folder), 
				LocalizedText.choose_a_folder,
				LocalizedText.no_folder_selected
			);
			add(fileChooser);
		}
		
		VideoOutputControl videoOutputControl = new VideoOutputControl(this);
		cbVideoSize.addActionListener(videoOutputControl);
		fileChooser.addActionListener(videoOutputControl);
	}
	
	public JComboBox<String> getCbVideoSize() {
		return cbVideoSize;
	}

	public FileChooserField getFileChooser() {
		return fileChooser;
	}

}
