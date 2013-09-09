package ui.panel;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import ui.LocalizedText;
import ui.custom.TabContentPanel;
import net.miginfocom.swing.MigLayout;

public class VideoPanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<String> cbVideoSize;
	
	public VideoPanel() {
		super(new MigLayout("ins 10"), LocalizedText.video_settings);
		
		String[] videoSizes = {"640×360", "1024×576", "1280×720", "1600×900"};
		cbVideoSize = new JComboBox(videoSizes);
		
		add(new JLabel(LocalizedText.video_size+" :"), "alignx right,gapx 0px 10px");
		add(cbVideoSize, "alignx center");
	}
	
	public String getSelectedSize() {
		return (String) cbVideoSize.getSelectedItem();
	}
}
