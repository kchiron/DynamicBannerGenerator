package dbg.videoassembler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.property.PropertyManager;
import dbg.ffmpeg.FFmpeg;
import dbg.ffmpeg.FFmpegConcat;
import dbg.ui.LocalizedText;
import dbg.ui.videoassembler.VideoAssemblerWindow;

public class VideoAssemblerWorker extends SwingWorker<File, String> {

	private VideoAssemblerWindow window;
	
	public VideoAssemblerWorker(final VideoAssemblerWindow window) {
		this.window = window;
		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if("progress".equals(evt.getPropertyName())) {
					if(window != null) 
						window.updateProgress((int)evt.getNewValue());
					else
						System.out.println("[STEP "+evt.getNewValue()+"]");
				}
			}
		});
	}
	
	@Override
	protected File doInBackground() {
		try {
			MediaSequence sequence = PropertyManager.getSequence();
			File videoOutput = new File("output");
			
			// Folder for temporary video creation
			final File tmp = new File("tmp");
			if (!tmp.exists()) tmp.mkdir();
			else FileUtils.cleanDirectory(tmp);

			final int nbSteps = sequence.getElementsByClass(ImageElement.class).size()
				+ (sequence.size() - sequence.getElementsByClass(ImportedMediaElement.class).size()) // Nb generated elements
				+ sequence.size();

			//List of videos to concatenate
			ArrayList<File> videoToConcatenate = new ArrayList<File>();
		
			//Step two: concatenate every video in one video
			publish(LocalizedText.concatenating_videos);
			String videoSize = PropertyManager.getVideoOutputProperties().getVideoSize();
			
			//TODO: concat
			//FFmpegConcat.concatMediaSequenceToVideo(sequence, videoSize , videoOutput);
			
			//Done
			int i = 0;
			i += sequence.size();
			setProgress(i*100/nbSteps);
			return videoOutput;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void process(List<String> chunks) {
		for(String str : chunks) {
			if(window != null) 
				window.updateMessage(str);
			else 
				System.out.println(str);
		}
	}

	@Override
	protected void done() {
		super.done();
		
	}
}
