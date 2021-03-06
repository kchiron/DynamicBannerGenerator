package dbg.ui.settings.panel;

import dbg.control.HoroscopeControl;
import dbg.ui.LocalizedText;
import dbg.ui.settings.form.UnitJSpinner;
import dbg.ui.settings.form.filechooser.FileChooserField;
import dbg.ui.settings.form.filechooser.MediaFileChooser;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class HoroscopePanel extends TabContentPanel {

	private static final long serialVersionUID = 1L;

	private final FileChooserField fileChooser;
	private final UnitJSpinner displayTime;
	private final UnitJSpinner signsPerPage;

	private final HoroscopeControl horoscopeControl;

	public HoroscopePanel(SequencePanel sequencePanel) {
		super(new MigLayout("ins 10", "[right,180:180:180]10[]", ""), LocalizedText.get("horoscope_settings"));

		{//Number of signs per page
			add(new JLabel(LocalizedText.get("sign_per_page") + " :"));
			signsPerPage = new UnitJSpinner(2, 3);
			add(signsPerPage, "wrap");
		}

		{//Display time
			add(new JLabel(LocalizedText.get("display_time") + " :"));
			displayTime = new UnitJSpinner("sec", 0, null);
			add(displayTime, "wrap");
		}

		{//Background image select
			add(new JLabel(LocalizedText.get("background_image") + " :"), "ay top");

			fileChooser = new FileChooserField(
				null, 
				new MediaFileChooser(LocalizedText.get("choose_an_image"), MediaFileChooser.Type.IMAGE),
				LocalizedText.get("choose_an_image"),
				LocalizedText.get("no_file_selected")
			);
			add(fileChooser);
		}

		this.horoscopeControl = new HoroscopeControl(sequencePanel, this);
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

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			fileChooser.checkError();
			horoscopeControl.updateSequenceHoroscopeElement();
		}
	}
}
