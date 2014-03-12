package dbg.ui.settings;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.panel.HoroscopePanel;
import dbg.ui.settings.panel.SequencePanel;
import dbg.ui.settings.panel.TabPanel;
import dbg.ui.settings.panel.VideoOutputPanel;
import dbg.ui.settings.panel.WeatherPanel;
import dbg.ui.videoassembler.VideoAssemblerWindow;
import dbg.videoassembler.VideoAssemblerWorker;

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
			weatherPanel.setEnabled(false);
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
			
			JButton btnLaunchVideoAssembler = new JButton(LocalizedText.assemble_video);
			btnLaunchVideoAssembler.setHorizontalAlignment(JButton.LEFT);
			btnLaunchVideoAssembler.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							VideoAssemblerWindow vaWindow = new VideoAssemblerWindow(SettingsWindow.this);
							VideoAssemblerWorker vaWorker = new VideoAssemblerWorker(vaWindow);
							vaWindow.setVisible(true);
							vaWorker.execute();

							try {
								vaWorker.get();
							} catch (InterruptedException ex) {
								JOptionPane.showMessageDialog(vaWindow, LocalizedText.video_assembly_canceled);
							} catch (ExecutionException ex) {
								JOptionPane.showMessageDialog(vaWindow, LocalizedText.video_assembly_error, LocalizedText.error, JOptionPane.ERROR_MESSAGE);
							}
							
							vaWindow.setVisible(false);
							vaWindow.dispose();
						}
					});
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
