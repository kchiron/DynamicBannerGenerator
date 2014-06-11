package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.PropertyManager;
import dbg.data.property.VideoOutputProperties;
import dbg.util.ActivityMonitor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

public class VideoAssemblerWorker extends SwingWorker<File, String> {

	private final Frame parent;

	private final VideoAssembler videoAssembler;

	private final VideoAssemblerWindow window;

	public VideoAssemblerWorker(MediaSequence mediaSequence, VideoOutputProperties videoOutputProperties, Frame parent) {
		this.parent = parent;

		this.videoAssembler = new VideoAssembler(mediaSequence, videoOutputProperties, new ActivityMonitor() {
			@Override
			public void setProgress(String message, int progress) {
				if (message != null)
					VideoAssemblerWorker.this.publish(message);
				VideoAssemblerWorker.this.setProgress(progress);
			}
		});

		window = new VideoAssemblerWindow(parent, this);

		addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					window.updateProgress((int) evt.getNewValue());
				}
			}
		});
	}

	public void setVisible(boolean b) {
		window.setVisible(b);
	}

	protected void close() {
		if (parent == null)
			System.exit(0);
		else {
			window.setVisible(false);
			window.dispose();
		}
	}

	@Override
	protected File doInBackground() throws Exception {
		return videoAssembler.call();
	}

	@Override
	protected void process(List<String> chunks) {
		window.updateText(chunks.get(chunks.size() - 1));
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
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
			add(new ImageElement("Moon", new File("src/test/resources/media-samples/moon.jpg"), 5));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
			add(new VideoElement("Earth", new File("src/test/resources/media-samples/earth.mts")));
		}});
		PropertyManager.setVideoOutputProperties(new VideoOutputProperties() {{
			setOutputFolder(new File(""));
			setIndexOfVideoSize(3);
		}});

		System.out.println(PropertyManager.getVideoOutputProperties().getOutputFolder().getAbsolutePath());

		final VideoAssemblerWorker a = new VideoAssemblerWorker(PropertyManager.getSequence(), PropertyManager.getVideoOutputProperties(), null);
		new Thread() {public void run(){a.setVisible(true);}}.start();
		System.out.println(1);
		a.execute();
	}
}
