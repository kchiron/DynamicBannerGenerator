package dbg.ui.settings;

import com.ezware.dialog.task.TaskDialogs;
import dbg.Application;
import dbg.data.property.PropertyManager;
import dbg.ui.LocalizedText;
import dbg.ui.settings.panel.*;
import dbg.ui.videoassembler.VideoAssemblerWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SettingsWindow extends JFrame {

	private static SettingsWindow instance;

	public static SettingsWindow getInstance() {
		if (instance == null)
			instance = new SettingsWindow();
		return instance;
	}

	private SettingsWindow() {
		super(LocalizedText.get("settings"));
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

			JButton btnLaunchVideoAssembler = new JButton(LocalizedText.get("assemble_video"));
			btnLaunchVideoAssembler.setHorizontalAlignment(JButton.LEFT);
			btnLaunchVideoAssembler.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					final VideoAssemblerWorker vaWindow = new VideoAssemblerWorker(PropertyManager.getSequence(), PropertyManager.getVideoOutputProperties(), SettingsWindow.this);
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
						TaskDialogs.build(SettingsWindow.this, LocalizedText.get("video_assembly_error"), "")
								.title(LocalizedText.get("error"))
								.showException(ex);
					}
				}
			});
			pnlBottom.add(btnLaunchVideoAssembler);

			// Save button
			JButton btnSave = new JButton(LocalizedText.get("action.save_settings"));
			pnlBottom.add(btnSave);
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent paramActionEvent) {
					Application.save();
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


		final SystemTray tray = SystemTray.getSystemTray();
		TrayIcon trayIcon = null;

		//Initialize System tray menu
		if (SystemTray.isSupported()) {
			final PopupMenu minimizedPopupMenu;
			minimizedPopupMenu = new PopupMenu();
			MenuItem maximize = new MenuItem(LocalizedText.get("open_setting_window"));
			maximize.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					maximize();
				}
			});
			minimizedPopupMenu.add(maximize);
			MenuItem quit = new MenuItem(LocalizedText.get("action.quit"));
			quit.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					Application.exit();
				}
			});
			minimizedPopupMenu.add(quit);

			trayIcon = new TrayIcon(new ImageIcon(Application.class.getResource("icon.png")).getImage());
			trayIcon.setPopupMenu(minimizedPopupMenu);
		}

		//Add System stray menu
		try {
			if(trayIcon == null) throw new NullPointerException();

			tray.add(trayIcon);
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent paramWindowEvent) {
					minimize();
					Application.save();
				}
			});
		} catch (Exception e) {
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent paramWindowEvent) {
					toFront();
					requestFocus();
					switch (
							JOptionPane.showOptionDialog(
									SettingsWindow.this,
									LocalizedText.get("confirm_close"),
									"",
									JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE,
									null,
									new String[]{LocalizedText.get("action.save_n_quit"), LocalizedText.get("action.quit"), LocalizedText.get("action.cancel")},
									LocalizedText.get("action.save"))
							) {
						case 1:
							Application.exit();
						case 0:
							Application.saveAndExit();
						default:
							return;
					}
				}
			});
		}
	}

	public void maximize() {
		this.setVisible(true);
	}

	public void minimize() {
		this.setVisible(false);
	}

}
