package dbg.control;

import dbg.data.property.PropertyManager;
import dbg.data.property.VideoOutputProperties;
import dbg.ui.settings.form.filechooser.FileChooserField;
import dbg.ui.settings.panel.VideoOutputPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class VideoOutputControl implements ActionListener {

	private final VideoOutputPanel videoOutputPanel;
	
	private VideoOutputProperties properties;
	
	public VideoOutputControl(VideoOutputPanel videoOutputPanel) {
		this.videoOutputPanel = videoOutputPanel;
		
		properties = PropertyManager.getVideoOutputProperties();
		this.videoOutputPanel.getCbVideoSize().setSelectedIndex(properties.getIndexOfVideoSize());
		this.videoOutputPanel.getFileChooser().setFile(properties.getOutputFolder());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		final Object source = e.getSource();
		
		if(source.equals(videoOutputPanel.getCbVideoSize())) {
			@SuppressWarnings("unchecked")
			final int indexOfVideoSize = ((JComboBox<String>)source).getSelectedIndex();
			properties.setIndexOfVideoSize(indexOfVideoSize);
		}
		else if(source.equals(videoOutputPanel.getFileChooser())) {
			final File outputFolder = ((FileChooserField)source).getFile();
			properties.setOutputFolder(outputFolder);
		}
	}
}
