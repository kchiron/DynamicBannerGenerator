package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.property.VideoOutputProperties;
import dbg.ffmpeg.FFmpegConcat;
import dbg.ffmpeg.FFmpegVideoData;
import dbg.util.ActivityMonitor;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;

class VideoAssembler implements Callable<File> {

	private final MediaSequence mediaSequence;
	private final VideoOutputProperties videoOutputProperties;
	private final ActivityMonitor monitor;

	public VideoAssembler(MediaSequence mediaSequence, VideoOutputProperties videoOutputProperties, ActivityMonitor monitor) {
		this.mediaSequence = mediaSequence;
		this.videoOutputProperties = videoOutputProperties;
		this.monitor = monitor;
	}

	@Override
	public File call() throws Exception {
		try {
			final Date date = new java.util.Date();
			final File videoOutput = new File(videoOutputProperties.getOutputFolder().getAbsoluteFile(), "concat-" + date.getTime());

			final FFmpegVideoData options = new FFmpegVideoData();
			options.setSize(videoOutputProperties.getVideoSize());


			final FFmpegConcat concat = new FFmpegConcat(mediaSequence, videoOutput, options);

			//Done
			return concat.execute(monitor);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
