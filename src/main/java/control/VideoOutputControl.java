package control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JComboBox;

import ui.form.filechooser.FileChooserField;
import ui.panel.VideoOutputPanel;
import data.property.PropertyManager;
import data.property.VideoOutputProperties;

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
