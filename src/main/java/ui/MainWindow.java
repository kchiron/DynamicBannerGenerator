package ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;
import ui.custom.TabPanel;
import ui.panel.HoroscopePanel;
import ui.panel.SequencePanel;
import ui.panel.VideoPanel;
import ui.panel.WeatherPanel;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private SequencePanel sequencePanel;
	private VideoPanel videoPanel;
	private WeatherPanel weatherPanel;
	private HoroscopePanel horoscopePanel;
	
	private TabPanel tabPanel;
	
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 469);
		setLocationRelativeTo(null);
		
		getContentPane().setLayout(new MigLayout("ins 10, gap 5px 5px", "[][grow]", "[grow][]"));
		
		//Top part
		initTopPanel();
		getContentPane().add(sequencePanel, "cell 0 0,grow");
		getContentPane().add(tabPanel, "cell 1 0,grow");		
		
		//Bottom part
		JPanel bottomJPanel = initBottomPanel();
		getContentPane().add(bottomJPanel, "cell 1 1,alignx right");
		
		setVisible(true);
	}
	
	private void initTopPanel() {
		{ //Left side of the main window
			sequencePanel  = new SequencePanel();
			sequencePanel.setPreferredSize(new Dimension());
		}
		
		{ //Right side of the main window
			videoPanel = new VideoPanel();
			weatherPanel = new WeatherPanel();
			horoscopePanel = new HoroscopePanel();
			
			tabPanel = new TabPanel(weatherPanel, horoscopePanel, videoPanel);
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
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//Loading English text interface
		//LocalizedText.loadLanguage("/en.lang");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
