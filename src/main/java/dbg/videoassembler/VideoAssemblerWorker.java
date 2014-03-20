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
import dbg.ffmpeg.FFmpegVideoData;
import dbg.ui.LocalizedText;
import dbg.ui.videoassembler.VideoAssemblerWindow;

public class VideoAssemblerWorker extends SwingWorker<File, String> {

	private final VideoAssemblerWindow window;
	
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
		
      FFmpegVideoData options = new FFmpegVideoData();
      options.setSize(videoSize);
			
      FFmpegConcat concat = new FFmpegConcat(sequence, videoOutput, options) {
          @Override
          public void setProgress(String arg0, int arg1) {
              if(arg0 != null)
                 this.VideoAssemblerWorker.publish(arg0);
              this.VideoAssemblerWorker.setProgress(arg1);
          }
      };
      concat.execute();
			
			//Done
			setProgress(100);
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
