package ui.panel;

import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JSpinner;

import control.HoroscopeControl;
import net.miginfocom.swing.MigLayout;
import ui.LocalizedText;
import ui.form.UnitJSpinner;
import ui.form.filechooser.FileChooserField;
import ui.form.filechooser.MediaFileChooser;

public class HoroscopePanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private final FileChooserField fileChooser;
	private final UnitJSpinner displayTime;
	private final UnitJSpinner signsPerPage;

	public HoroscopePanel(SequencePanel sequencePanel) {
		super(new MigLayout("ins 10", "[right,180:180:180]10[]", ""), LocalizedText.horoscope_settings);

		{//Number of signs per page
			add(new JLabel(LocalizedText.sign_per_page+" :"));
			signsPerPage = new UnitJSpinner(1, 12);
			add(signsPerPage, "wrap");
		}

		{//Display time
			add(new JLabel(LocalizedText.display_time+" :"));
			displayTime = new UnitJSpinner("sec", 1, null);
			add(displayTime, "wrap");
		}

		{//Background image select
			add(new JLabel(LocalizedText.background_image+" :"), "ay top");

			fileChooser = new FileChooserField(
				null, 
				new MediaFileChooser(LocalizedText.choose_an_image, MediaFileChooser.Type.IMAGE), 
				LocalizedText.choose_an_image, 
				LocalizedText.no_file_selected
			);
			add(fileChooser);
		}
		
		ActionListener horoscopeControl = new HoroscopeControl(sequencePanel, this);
		displayTime.addActionListener(horoscopeControl);
		signsPerPage.addActionListener(horoscopeControl);
		fileChooser.addActionListener(horoscopeControl);
	}
	
	public FileChooserField getFileChooser() {
		return fileChooser;
	}

	public JSpinner getDisplayTime() {
		return displayTime.getSpinner();
	}

	public JSpinner getSignsPerPage() {
		return signsPerPage.getSpinner();
	}
}
