package ui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;

import ui.LocalizedText;
import ui.filechooser.ImageFileChooser;
import ui.form.UnitJSpinner;
import net.miginfocom.swing.MigLayout;

public class HoroscopePanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	private File backgroundImageFile;
	private JLabel lblNameBackgroundImageFile;
	
	public HoroscopePanel() {
		super(new MigLayout("ins 10", "[150:150:150][]", ""), LocalizedText.horoscope_settings);
		
		add(new JLabel(LocalizedText.display_time+" :"), "alignx right,gapx 0px 10px");
		add(new UnitJSpinner("sec", 0, null), "wrap");
		
		
		final JButton btnSelectImage = new JButton(LocalizedText.select_an_image);
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageFileChooser fileDialog = new ImageFileChooser(LocalizedText.select_an_image);
				
				if(fileDialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					update(fileDialog.getSelectedFile());	
			}
		});
		
		add(new JLabel(LocalizedText.background_image+" :"), "alignx right,gapx 0px 10px");
		
		lblNameBackgroundImageFile = new JLabel(LocalizedText.no_file_selected);
		add(lblNameBackgroundImageFile, "wrap, gap 10 10");
		add(new JLabel());
		add(btnSelectImage, "wrap");
	}

	public File getBackgroundImageFile() {
		return backgroundImageFile;
	}

	public void update(File backgroundImageFile) {
		this.backgroundImageFile = backgroundImageFile;
		if(backgroundImageFile != null) {
			FileSystemView view = FileSystemView.getFileSystemView();
			lblNameBackgroundImageFile.setText(backgroundImageFile.getName());
			lblNameBackgroundImageFile.setIcon(view.getSystemIcon(backgroundImageFile)); 
			lblNameBackgroundImageFile.setToolTipText(backgroundImageFile.getAbsolutePath());
			repaint();
		}
	}
}
