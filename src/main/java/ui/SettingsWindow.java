package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import ui.panel.HoroscopePanel;
import ui.panel.SequencePanel;
import ui.panel.TabPanel;
import ui.panel.VideoOutputPanel;
import ui.panel.WeatherPanel;
import ui.util.UiUtils;

public class SettingsWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private SequencePanel sequencePanel;
	private VideoOutputPanel videoOutputPanel;
	private WeatherPanel weatherPanel;
	private HoroscopePanel horoscopePanel;
	
	private TabPanel tabPanel;
	
	public SettingsWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 425);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new MigLayout("ins 10, gap 5px 5px", "[30%][grow]", "[grow][]"));
		
		//Top part
		initTopPanel();
		getContentPane().add(sequencePanel, "cell 0 0,grow");
		getContentPane().add(tabPanel, "cell 1 0,grow");		
		
		//Bottom part
		JPanel bottomJPanel = initBottomPanel();
		getContentPane().add(bottomJPanel, "cell 1 1,alignx right");
		
		setMinimumSize(new Dimension(640, 400));
		
		setVisible(true);
	}
	
	private void initTopPanel() {
		{ //Left side of the main window
			sequencePanel  = new SequencePanel();
		}
		
		{ //Right side of the main window
			videoOutputPanel = new VideoOutputPanel();
			weatherPanel = new WeatherPanel();
			horoscopePanel = new HoroscopePanel();
			
			tabPanel = new TabPanel(weatherPanel, horoscopePanel, videoOutputPanel);
		}
	}
	
	private JPanel initBottomPanel() {
		FlowLayout fl_bottomJPanel = new FlowLayout(FlowLayout.RIGHT);
		JPanel bottomJPanel = new JPanel(fl_bottomJPanel);
		fl_bottomJPanel.setVgap(0);
		fl_bottomJPanel.setHgap(0);
		
		JButton btnCancel = new JButton(LocalizedText.cancel);
		bottomJPanel.add(btnCancel);
		
		JButton btnOK = new JButton(LocalizedText.ok);
		bottomJPanel.add(btnOK);
		
		getRootPane().setDefaultButton(btnOK);
		
		return bottomJPanel;
	}

	public static void main(String[] args) {
		UiUtils.initUIManager();
		
		//Loading English text interface
		//LocalizedText.loadLanguage("/en.lang");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingsWindow frame = new SettingsWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
