package ui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import ui.ImageFileChooser;
import ui.LocalizedText;
import ui.custom.TabContentPanel;
import ui.custom.UnitJSpinner;
import net.miginfocom.swing.MigLayout;

public class HoroscopePanel extends TabContentPanel {
	
	private static final long serialVersionUID = 1L;
	private final ImageFileChooser backgroundImageFile;
	
	public HoroscopePanel() {
		super(new MigLayout("ins 10, fillx", "[][grow][]", "[][]"), LocalizedText.horoscope_settings);
		
		add(new JLabel(LocalizedText.display_time+" :"), "alignx right,gapx 0px 10px");
		add(new UnitJSpinner("sec"), "wrap");
		
		backgroundImageFile = new ImageFileChooser();
		
		final JButton btnSelectImage = new JButton(LocalizedText.select_an_image);
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(backgroundImageFile.showOpenDialog(null));
				System.out.println(backgroundImageFile.getSelectedFile());
			}
		});
		
		add(new JLabel(LocalizedText.background_image+" :"), "alignx right,gapx 0px 10px");
		add(btnSelectImage, "wrap");
	}
}
