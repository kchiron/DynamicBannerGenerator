package dbg.ui.videoassembler;

import dbg.data.media.MediaSequence;
import dbg.data.media.MediaSequenceImagesGenerator;
import dbg.data.property.VideoOutputProperties;
import dbg.ffmpeg.FFmpeg;
import dbg.ffmpeg.FFmpegConcat;
import dbg.ffmpeg.FFmpegVideoData;
import dbg.util.ActivityMonitor;
import dbg.util.Logger;
import dbg.util.ProgressState;
import dbg.util.TemporaryFileHandler;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;

class VideoAssembler implements Callable<File> {

	private final MediaSequence mediaSequence;
	private final VideoOutputProperties videoOutputProperties;
	private final ActivityMonitor monitor;
	private final TemporaryFileHandler temporaryFileHandler;

	public VideoAssembler(MediaSequence mediaSequence, VideoOutputProperties videoOutputProperties, ActivityMonitor monitor) {
		this.mediaSequence = mediaSequence;
		this.videoOutputProperties = videoOutputProperties;
		this.monitor = monitor;
		this.temporaryFileHandler = new TemporaryFileHandler();
	}

	@Override
	public File call() throws Exception {
		try {
			final Date date = new java.util.Date();
			final File videoOutput = new File(videoOutputProperties.getOutputFolder().getAbsoluteFile(), "concat-" + date.getTime());

			final FFmpegVideoData options = new FFmpegVideoData();
			options.setSize(videoOutputProperties.getVideoSize());

			FFmpeg.initialize();

			ProgressState state = new ProgressState(monitor);

			final String assemblyID = String.valueOf(System.currentTimeMillis() / 1000);
			final Logger logger = new Logger(assemblyID, "Video-assembly");

			final MediaSequenceImagesGenerator imagesGenerator =
					new MediaSequenceImagesGenerator(temporaryFileHandler, mediaSequence, logger);

			return new FFmpegConcat(
					imagesGenerator.generate(new ProgressState(state.newState(null, 25))),
					videoOutput,
					options,
					temporaryFileHandler,
					logger
			).execute(new ProgressState(state.newState(null, 75)));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			temporaryFileHandler.clearAllFiles();
		}
	}
}
