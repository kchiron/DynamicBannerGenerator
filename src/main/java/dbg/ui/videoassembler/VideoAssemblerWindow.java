package dbg.ui.videoassembler;

import dbg.ui.LocalizedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VideoAssemblerWindow extends JDialog {

	private final JLabel lblCurrentTaskName;
	private final JProgressBar progressBar;

	public VideoAssemblerWindow(Frame parent, final VideoAssemblerWorker associatedWorker) {
		// Initialize window
		super(parent, true);
		setTitle(LocalizedText.get("video_assembler"));
		setUndecorated(true);

		((JComponent) getContentPane()).setBorder(
			BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
		);

		Dimension d = new Dimension(400, 116);
		setBounds(new Rectangle(d));
		setMinimumSize(d);
		setMaximumSize(d);
		setResizable(false);
		setLocationRelativeTo(parent);
		setLayout(new MigLayout("insets 10", "[grow][]", "[]7[]7[]7[grow]"));

		// Window components
		{
			// Dialog title label
			JLabel lblTilte = new JLabel(LocalizedText.get("video_assembler"));
			Font custom = lblTilte.getFont().deriveFont(15f).deriveFont(Font.BOLD);
			lblTilte.setFont(custom);
			lblTilte.setHorizontalAlignment(SwingConstants.CENTER);
			add(lblTilte, "cell 0 0 2 1, grow, alignx center");

			// Progress bar
			progressBar = new JProgressBar();
			progressBar.setMaximum(100);
			progressBar.setMinimum(0);
			add(progressBar, "cell 0 1 2 1,grow");

			// Current task label
			lblCurrentTaskName = new JLabel("");
			add(lblCurrentTaskName, "cell 0 2 2 1,grow");

			// Cancel button
			JButton btnCancel = new JButton(LocalizedText.get("action.cancel"));
			add(btnCancel, "cell 1 3, alignx right, aligny bottom");

			btnCancel.addActionListener(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!associatedWorker.isDone() || !associatedWorker.isCancelled())
						associatedWorker.cancel(true);
					associatedWorker.close();
				}
			});
		}
	}

	public void updateProgress(int newValue) {
		progressBar.setValue(Math.max(0, Math.min(100, newValue)));
		repaint();
	}

	public void updateText(String text) {
		lblCurrentTaskName.setText(text);
	}
}
