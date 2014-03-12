package dbg.ui.videoassembler;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;

import net.miginfocom.swing.MigLayout;
import dbg.ui.LocalizedText;
import dbg.videoassembler.VideoAssemblerWorker;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.JLabel;

public class VideoAssemblerWindow extends JDialog {

	private static final long serialVersionUID = 1L;
	private JLabel lblCurrentTaskName;
	private JProgressBar progressBar;

	public VideoAssemblerWindow(JFrame parent) {
		super();
		setTitle(LocalizedText.video_assembler);
		setModal(true);
		getContentPane().setLayout(new MigLayout("insets 5", "[grow][]", "[grow][][]"));
		
		lblCurrentTaskName = new JLabel("");
		getContentPane().add(lblCurrentTaskName, "cell 0 0 2 1,grow");
		
		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		getContentPane().add(progressBar, "cell 0 1 2 1,grow");
		
		JButton btnCancel = new JButton(LocalizedText.cancel);
		getContentPane().add(btnCancel, "cell 1 2,alignx right");
		
		Dimension d = new Dimension(270, 116);
		setBounds(new Rectangle(d));
		setMinimumSize(d);
		setMaximumSize(new Dimension(600, 116));
		setLocationRelativeTo(parent);
		
		/*
		//Prevent window closing 
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent paramWindowEvent) {
				requestFocus();
				toFront();
				
				switch (
					JOptionPane.showOptionDialog(
						this, 
						LocalizedText.confirm_close, 
						"", 
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.WARNING_MESSAGE,
						null, 
						new String[]{LocalizedText.save, LocalizedText.quit, LocalizedText.cancel},
						LocalizedText.save)
				) {
					case 2: return;
					case 1: exit();
					case 0: window.saveAndExit();
					default: return;
				}
			}
		});*/
	}
	
	public void updateMessage(String str) {
		lblCurrentTaskName.setText(str);
		repaint();
	}
	
	public void updateProgress(int percent) {
		progressBar.setValue(percent);
		repaint();
	}
	
	public static void main(String[] args) {
		VideoAssemblerWindow a = new VideoAssemblerWindow(null);
		a.setVisible(true);
	}
}
