package ui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

import ui.LocalizedText;
import ui.filechooser.FolderChooser;
import ui.filechooser.MediaFileChooser;
import net.miginfocom.swing.MigLayout;

public class VideoOutputPanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> cbVideoSize;
	
	private File videoOutputFolder;

	private JLabel lblVideoOutputFolderName;
	
	public VideoOutputPanel() {
		super(new MigLayout("ins 10", "[150:150:150]10[]", ""), LocalizedText.video_output_settings);
		
		String[] videoSizes = {"640×360", "1024×576", "1280×720 (720p)", "1440x900", "1600×900", "1920x1080 (1080p)"};

		try {
			videoOutputFolder = new File(new File("."+File.separator).getCanonicalPath());
		} catch (IOException e) {e.printStackTrace();}
		
		{//Video size select
			add(new JLabel(LocalizedText.video_size+" :"), "alignx right");
			
			cbVideoSize = new JComboBox<String>(videoSizes);
			add(cbVideoSize, "wrap");	
		}
		
		{//Video output folder select
			add(new JLabel(LocalizedText.video_output_folder+" :"), "align right");
			
			lblVideoOutputFolderName = new JLabel(LocalizedText.no_folder_selected);
			add(lblVideoOutputFolderName, "wrap, gap 10 10");
					
			JButton chooseOutputFolder = new JButton(LocalizedText.choose_a_folder);
			chooseOutputFolder.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					FolderChooser folderDialog = new FolderChooser(LocalizedText.choose_a_folder);
					
					if(folderDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						update(folderDialog.getSelectedFile());	
				}
			});
			add(chooseOutputFolder, "skip 1");
		}
	}
	
	public void update(File videoOutputFolder) {
		this.videoOutputFolder = videoOutputFolder;
		if(videoOutputFolder != null) {
			FileSystemView view = FileSystemView.getFileSystemView();
			lblVideoOutputFolderName.setText(videoOutputFolder.getName());
			lblVideoOutputFolderName.setIcon(view.getSystemIcon(videoOutputFolder)); 
			lblVideoOutputFolderName.setToolTipText(videoOutputFolder.getAbsolutePath());
			repaint();
		}
	}
	
	public String getSelectedSize() {
		return ((String) cbVideoSize.getSelectedItem()).replaceAll("\\s\\(\\d+p\\)", "");
	}
}
