package dbg.ui.settings;

import com.ezware.dialog.task.TaskDialogs;
import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.panel.*;
import dbg.ui.videoassembler.VideoAssembler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SettingsWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public SettingsWindow() {
		super(LocalizedText.settings);
		setLayout(new BorderLayout());

		{//Top part
			//Left side of the main window (SequencePanel)
			SequencePanel sequencePanel = new SequencePanel(this);
			Dimension d = sequencePanel.getPreferredSize();
			d.width = 200;
			sequencePanel.setPreferredSize(d);
			getContentPane().add(sequencePanel);

			//Right side of the main window (TabPanel)
			VideoOutputPanel videoOutputPanel = new VideoOutputPanel();
			WeatherPanel weatherPanel = new WeatherPanel(sequencePanel);
			weatherPanel.setEnabled(false);
			HoroscopePanel horoscopePanel = new HoroscopePanel(sequencePanel);
			TabPanel tabPanel = new TabPanel(weatherPanel, horoscopePanel, videoOutputPanel);

			//Split pane
			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sequencePanel, tabPanel);
			split.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			split.setContinuousLayout(true);
			getContentPane().add(split, BorderLayout.CENTER);
		}

		{//Bottom part
			final JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			JButton btnLaunchVideoAssembler = new JButton(LocalizedText.assemble_video);
			btnLaunchVideoAssembler.setHorizontalAlignment(JButton.LEFT);
			btnLaunchVideoAssembler.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final VideoAssembler vaWindow = new VideoAssembler(PropertyManager.getSequence(), PropertyManager.getVideoOutputProperties(), SettingsWindow.this);
					//Launching the video assembler in a separated thread so that it won't be blocked by the modal that will open
					new Thread() {
						public void run() {
							vaWindow.execute();
						}
					}.start();
					vaWindow.setVisible(true); // <= blocking this current window thread since vaWindow is a modal
					try {
						vaWindow.get();
					} catch (Exception ex) {
						TaskDialogs.build(SettingsWindow.this, LocalizedText.video_assembly_error, "")
								.title(LocalizedText.error)
								.showException(ex);
					}
				}
			});
			pnlBottom.add(btnLaunchVideoAssembler);

			// Save button
			JButton btnSave = new JButton(LocalizedText.save_settings);
			pnlBottom.add(btnSave);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					save();
				}
			});

			// Make "Save & Quit" button default
			getRootPane().setDefaultButton(btnSave);

			// Display bottom panel
			getContentPane().add(pnlBottom, BorderLayout.SOUTH);
		}

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(640, 420));
		setBounds(100, 100, 673, 425);
		setLocationRelativeTo(null);
	}

	public void save() {
		try {
			PropertyManager.saveToFile();
			JOptionPane.showMessageDialog(SettingsWindow.this, LocalizedText.configuration_saved_success, LocalizedText.saving, JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(SettingsWindow.this, LocalizedText.configuration_saved_failed, LocalizedText.saving, JOptionPane.ERROR_MESSAGE);
		}
	}

	public void saveAndExit() {
		save();
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		System.exit(0);
	}
}
