package dbg.ui.settings;

import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.panel.*;
import dbg.ui.videoassembler.VideoAssembler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	public SettingsWindow() {
		super(LocalizedText.settings);
		getContentPane().setLayout(new BorderLayout());

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
			final JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			JButton btnLaunchVideoAssembler = new JButton(LocalizedText.assemble_video);
			btnLaunchVideoAssembler.setHorizontalAlignment(JButton.LEFT);
			btnLaunchVideoAssembler.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final VideoAssembler vaWindow = new VideoAssembler(PropertyManager.getSequence(), PropertyManager.getVideoOutputProperties(), SettingsWindow.this);
					//Launching the video assembler in a separeted thread so that it won't be blocked by the modal that will open
					new Thread() {public void run(){vaWindow.execute();}}.start();
					vaWindow.setVisible(true); // <= blocking this current window thread since vaWindow is a modal
				}
			});
			bottom.add(btnLaunchVideoAssembler);

			JButton btnCancel = new JButton(LocalizedText.cancel);
			bottom.add(btnCancel);

			JButton btnOK = new JButton(LocalizedText.ok);
			bottom.add(btnOK);

			getRootPane().setDefaultButton(btnOK);

			getContentPane().add(bottom, BorderLayout.SOUTH);

			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					saveAndExit();
				}
			});

			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					dispose();
				}
			});
		}

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(640, 420));
		setBounds(100, 100, 673, 425);
		setLocationRelativeTo(null);
	}

	public void saveAndExit() {
		PropertyManager.saveToFile();
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		System.exit(0);
	}
}
