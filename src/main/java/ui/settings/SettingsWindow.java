package ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import ui.LocalizedText;
import ui.panel.HoroscopePanel;
import ui.panel.SequencePanel;
import ui.panel.TabPanel;
import ui.panel.VideoOutputPanel;
import ui.panel.WeatherPanel;
import data.property.PropertyManager;

public class SettingsWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private final SequencePanel sequencePanel;
	
	private final TabPanel tabPanel;
	private final VideoOutputPanel videoOutputPanel;
	private final WeatherPanel weatherPanel;
	private final HoroscopePanel horoscopePanel;
	
	public SettingsWindow() {
		super(LocalizedText.settings);
		getContentPane().setLayout(new BorderLayout());

		{//Top part
			//Left side of the main window (SequencePanel)
			sequencePanel  = new SequencePanel(this);
			Dimension d = sequencePanel.getPreferredSize();
			d.width = 200;
			sequencePanel.setPreferredSize(d);
			getContentPane().add(sequencePanel);

			//Right side of the main window (TabPanel)
			videoOutputPanel = new VideoOutputPanel();
			weatherPanel = new WeatherPanel(sequencePanel);
			horoscopePanel = new HoroscopePanel(sequencePanel);
			tabPanel = new TabPanel(weatherPanel, horoscopePanel, videoOutputPanel);

			//Split pane
			JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sequencePanel, tabPanel);
			split.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
			split.setContinuousLayout(true);
			getContentPane().add(split, BorderLayout.CENTER);
		}	

		{//Bottom part
			final JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
					exit();
				}
			});	
		}

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setMinimumSize(new Dimension(640, 420));
		setBounds(100, 100, 673, 425);
		setLocationRelativeTo(null);
	}

	
	public void exit() {
		this.dispose();
	}
	
	public void saveAndExit() {
		PropertyManager.saveToFile();
		exit();
	}
}
