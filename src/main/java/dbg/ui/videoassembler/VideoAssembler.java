package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.PropertyManager;
import dbg.data.property.VideoOutputProperties;
import dbg.ffmpeg.FFmpegConcat;
import dbg.ffmpeg.FFmpegVideoData;
import dbg.ui.LocalizedText;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Date;
import java.util.List;

public class VideoAssembler extends SwingWorker<File, String> {

	private final MediaSequence mediaSequence;
	private final VideoOutputProperties videoOutputProperties;
	private final Frame parent;

	// Window components
	private final JDialog window;
	private final JLabel lblCurrentTaskName;
	private final JProgressBar progressBar;

	public VideoAssembler(MediaSequence mediaSequence, VideoOutputProperties videoOutputProperties, Frame parent) {
		this.mediaSequence = mediaSequence;
		this.videoOutputProperties = videoOutputProperties;
		this.parent = parent;

		// Initialize window
		{
			window = new JDialog(parent, true);
			window.setTitle(LocalizedText.video_assembler);
			window.setUndecorated(true);

			((JComponent)window.getContentPane()).setBorder(
					BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)
			);

			Dimension d = new Dimension(400, 116);
			window.setBounds(new Rectangle(d));
			window.setMinimumSize(d);
			window.setMaximumSize(d);
			window.setResizable(false);
			window.setLocationRelativeTo(parent);
			window.setLayout(new MigLayout("insets 10", "[grow][]", "[]7[]7[]7[grow]"));

			// Window components
			{
				// Dialog title label
				JLabel lblTilte = new JLabel(LocalizedText.video_assembler);
				Font custom = lblTilte.getFont().deriveFont(15f).deriveFont(Font.BOLD);
				lblTilte.setFont(custom);
				lblTilte.setHorizontalAlignment(SwingConstants.CENTER);
				window.add(lblTilte, "cell 0 0 2 1, grow, alignx center");

				// Progress bar
				progressBar = new JProgressBar();
				progressBar.setMaximum(100);
				progressBar.setMinimum(0);
				window.add(progressBar, "cell 0 1 2 1,grow");

				// Current task label
				lblCurrentTaskName = new JLabel("");
				window.add(lblCurrentTaskName, "cell 0 2 2 1,grow");

				// Cancel button
				JButton btnCancel = new JButton(LocalizedText.cancel);
				window.add(btnCancel, "cell 1 3, alignx right, aligny bottom");

				btnCancel.addActionListener(new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!isDone() || !isCancelled())
							cancel(true);
						close();
					}
				});
			}
		}

		addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setValue(Math.max(0, Math.min(100, (int) evt.getNewValue())));
					window.repaint();
				}
			}
		});
	}

	public void setVisible(boolean b) {
		window.setVisible(b);
	}

	private void close() {
		if (parent == null)
			System.exit(0);
		else {
			window.setVisible(false);
			window.dispose();
		}
	}

	@Override
	protected File doInBackground() {
		try {
			final Date date = new java.util.Date();
			final File videoOutput = new File(videoOutputProperties.getOutputFolder().getAbsoluteFile(), "concat-" + date.getTime());

			final FFmpegVideoData options = new FFmpegVideoData();
			options.setSize(videoOutputProperties.getVideoSize());

			//TODO: check for error in DBG configuration to prevent error during assembly
			final FFmpegConcat concat = new FFmpegConcat(mediaSequence, videoOutput, options) {
				@Override
				public void setProgress(String message, int percent) {
					if (message != null)
						VideoAssembler.this.publish(message);
					VideoAssembler.this.setProgress(percent);
				}
			};

			//Done
			return concat.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void process(List<String> chunks) {
		lblCurrentTaskName.setText(chunks.get(chunks.size() - 1));
	}

	@Override
	protected void done() {
		close();
	}

	/*
	* Main method: Testing purpose only
	*/
	public static void main(String[] args) {
		PropertyManager.setSequence(new MediaSequence() {{
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
			add(new ImageElement("Moon", new File("src/test/resources/media-samples/moon.jpg"), 5));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.ts")));
		}});
		PropertyManager.setVideoOutputProperties(new VideoOutputProperties() {{
			setOutputFolder(new File(""));
			setIndexOfVideoSize(3);
		}});

		System.out.println(PropertyManager.getVideoOutputProperties().getOutputFolder().getAbsolutePath());

		final VideoAssembler a = new VideoAssembler(PropertyManager.getSequence(), PropertyManager.getVideoOutputProperties(), null);
		new Thread() {public void run(){a.setVisible(true);}}.start();
		System.out.println(1);
		a.execute();
	}
}
