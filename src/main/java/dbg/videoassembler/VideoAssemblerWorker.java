package dbg.videoassembler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.generated.HoroscopeElement;
import dbg.data.media.element.generated.WeatherElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.data.property.PropertyManager;
import dbg.ffmpeg.FFmpeg;
import dbg.ffmpeg.FileExtended;
import dbg.ui.LocalizedText;
import dbg.ui.videoassembler.VideoAssemblerWindow;

public class VideoAssemblerWorker extends SwingWorker<FileExtended, String> {

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
	protected FileExtended doInBackground() {
		try {
			MediaSequence sequence = PropertyManager.getSequence();
			FileExtended videoOutput = new FileExtended("output.mp4", false);
			
			// Folder for temporary video creation
			final File tmp = new File("tmp");
			if (!tmp.exists()) tmp.mkdir();
			else FileUtils.cleanDirectory(tmp);

			final int nbSteps = sequence.getElementsByClass(ImageElement.class).size()
				+ (sequence.size() - sequence.getElementsByClass(ImportedMediaElement.class).size()) // Nb generated elements
				+ sequence.size();

			//List of videos to concatenate
			ArrayList<FileExtended> videoToConcatenate = new ArrayList<FileExtended>();
			
			//Step one: convert everything in video format
			int i = 0;
			for (int j = 0; j < sequence.size(); j++) {
				if(isCancelled()) return null;
				
				MediaElement element = sequence.get(j);
				if (element instanceof ImageElement) {
					publish(LocalizedText.converting_image+" ["+element.getTitle()+"]");
					videoToConcatenate.add(
						FFmpeg.convertImageToVideo(
							tmp,
							element.getDuration(),
							((ImageElement)element).getFile(),
							"elem"+Integer.toString(j),
							false
						)
					);
					i++;
					
					setProgress(i*100/nbSteps);
				}
				else if(element instanceof HoroscopeElement) {
					publish();
					//TODO convert horoscope to video
				}
				else if(element instanceof WeatherElement) {
					publish();
					//TODO convert weather to video
				}
				else if(element instanceof VideoElement) {
					publish(LocalizedText.adding_video+" ["+element.getTitle()+"]");
					videoToConcatenate.add(((VideoElement)element).getFile());
				}
			}
			
			if(isCancelled()) return null;
			
			//Step two: concatenate every video in one video
			publish(LocalizedText.concatenating_videos);
			FFmpeg.concatenateVideos(videoOutput, videoToConcatenate.toArray(new FileExtended[videoToConcatenate.size()]));

			//Done
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
