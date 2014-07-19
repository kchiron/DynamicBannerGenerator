package dbg.ffmpeg;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.exception.UnknownOperatingSystem;
import dbg.ffmpeg.FFmpeg.OutputProcessor;
import dbg.ui.LocalizedText;
import dbg.util.ActivityMonitor;
import dbg.util.Logger;
import dbg.util.ProgressState;
import dbg.util.TemporaryFileHandler;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FFmpegConcat {

	private final MediaSequence sequence;
	private final TemporaryFileHandler temporaryFileHandler;
	private final Logger logger;
	private final File outputFile;
	private final FFmpegVideoData outputVideoOptions;

	public FFmpegConcat(MediaSequence sequence, File outputFile, FFmpegVideoData outputVideoOptions, TemporaryFileHandler temporaryFileHandler, Logger logger) {
		this.sequence = sequence;
		this.temporaryFileHandler = temporaryFileHandler;
		this.logger = logger;
		this.outputFile = new File(outputFile.getAbsoluteFile().getParentFile(), FilenameUtils.getBaseName(outputFile.getName()) + ".mp4");
		this.outputVideoOptions = outputVideoOptions;
	}

	/**
	 * Concatenates a MediaSequence into a concatenated video file
	 */
	public File execute(final ProgressState progressState) throws IOException, InterruptedException, UnknownOperatingSystem, ExecutionException {
		//final MediaSequence sequence = PropertyManager.getSequence();
		//final String videoSize = PropertyManager.getVideoOutputProperties().getVideoSize();
		final String videoBitRate =
				(outputVideoOptions != null && outputVideoOptions.getBitRate() != null) ?
						outputVideoOptions.getBitRate() : "450k";
		final String videoSize =
				(outputVideoOptions != null && outputVideoOptions.getSize() != null) ?
						outputVideoOptions.getSize() : "1440x900";
		final String videoCodec =
				(outputVideoOptions != null && outputVideoOptions.getCodec() != null) ?
						outputVideoOptions.getCodec() : "libx264";
		final Integer videoFrameRate =
				(outputVideoOptions != null && outputVideoOptions.getFrameRate() != null) ?
						outputVideoOptions.getFrameRate() : 24;

		final int videoDuration = sequence.getDuration();


		logger.info("Preparing media elements");
		/*
		 * Part one: convert every media element to same video format
		 */
		//final List<File> concatFiles = new ArrayList<>();
		final File videoListFile = temporaryFileHandler.createTempFile("DBG-video-list.txt");

		{
			final ActivityMonitor conversionMonitor =  progressState.newState(LocalizedText.get("converting_elements"), 25);
			conversionMonitor.setProgress(null, 5);

				final List<Future<Conversion>> conversions = new ArrayList<>();
			final CompletionService<Conversion> completionService = new ExecutorCompletionService<>(
					Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1)
			);

			FileWriter fs = null;
			try {
				fs = new FileWriter(videoListFile);

				for (final MediaElement e : sequence) {
					if (!(e instanceof ImportedMediaElement)) continue;

					final ImportedMediaElement element = (ImportedMediaElement) e;
					final File mediaFile = element.getFile();

					if (!mediaFile.exists()) {
						logger.error("File not found => " + mediaFile.getAbsolutePath());
						continue;
					}

					final List<String> ffArgs = new ArrayList<>();

					//Images conversion parameters
					if (element instanceof ImageElement) {
						ffArgs.addAll(Arrays.asList(
								"-loop", "1",
								"-f", "image2",
								"-i", mediaFile.getAbsolutePath(),
								"-t", Integer.toString(element.getDuration()),
								"-r", "1"
						));

						ffArgs.add("-s");
						ffArgs.add(videoSize);

						ffArgs.add("-c:v");
						ffArgs.add("mpeg2video");
					}
					// Video conversion parameters
					else if (element instanceof VideoElement) {
						FFmpegVideoData mediaFileMetaData = FFmpeg.getVideoData(mediaFile);

						ffArgs.addAll(Arrays.asList(
								"-i", mediaFile.getAbsolutePath(),
								"-an"
						));

						// If the video is not same size as asked => resize during conversion
						//  => If a video need to be resized it also have to be re-encoded (no fast conversion)
						boolean resized = false;
						if (!mediaFileMetaData.getSize().equals(videoSize)) {
							ffArgs.add("-s");
							ffArgs.add(videoSize);
							resized = true;
						}

						// If the video is already in mpeg2video format => no encoding at all (fastest)
						if (!resized && mediaFileMetaData.getCodec().contains("mpeg2video")) {
							ffArgs.add("-c:v");
							ffArgs.add("copy");
						}
						// If the video has mp4 codec => copy the video with few modification (faster)
                        /*else if (!resized && (mediaFileMetaData.getCodec().contains("mpeg4") || mediaFileMetaData.getCodec().contains("h264"))) {
                            ffArgs.addAll(Arrays.asList(
                                    "-c:v", "copy",
                                    "-bsf:v", "h264_mp4toannexb"
                            ));
                        }*/
						// Else encode the video to mpeg2video (slower)
						else {
							ffArgs.add("-c:v");
							ffArgs.add("mpeg2video");
						}
					}

					// Prepare FFmpeg conversion and launch
					if (ffArgs.size() > 1) {

						ffArgs.add("-f");
						ffArgs.add("mpegts");

						File outputFile = temporaryFileHandler.createTempFile("DBG-tmp-video.ts");
						ffArgs.add(outputFile.getAbsolutePath());
						ffArgs.add("-y");

						fs.write("file '" + outputFile.getAbsolutePath() + "'\n");

						// Launch FFmpeg conversion to video
						final OutputProcessor debugOutFile = new OutputProcessor() {
							@Override
							public Object call() throws Exception {
								String line;
								try (FileWriter writer = logger.newLogger(mediaFile.getName() + "-" + mediaFile.getAbsolutePath().hashCode()).getWriter()) {
									while ((line = processStdErr.readLine()) != null) {
										writer.append(line).append('\n');
									}
								} catch (IOException e) {
									e.printStackTrace();
								}
								return null;
							}
						};
						logger.info(mediaFile.getName() + " conversion starts");
						conversions.add(completionService.submit(new Conversion(ffArgs, debugOutFile, mediaFile.getName())));
					}
				}
			} catch (IOException e) {
				throw e;
			} finally {
				if (fs != null) fs.close();
			}

			//Ensure that every media element were correctly converted to video
			int i = 1;
			for (Future<Conversion> conversion : conversions) {
				conversionMonitor.setProgress(null, i * 100 / conversions.size());
				Conversion res = null;
				try {
					Future<Conversion> take = completionService.take();
					if (take != null) {
						res = take.get();
						logger.info(res.name + " conversion finished");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (res == null || res.returnCode != 0) {
					System.out.println("FFMPEG[" + conversions.indexOf(conversion) + "] exit code : " + res.returnCode);
					return null;
				}
				i++;
			}
		}

		/*
		 * Part two: concatenate video files
		 */
		int exitCode;
		logger.info("Concatenating all elements");
		{
			final ActivityMonitor concatMonitor = progressState.newState(LocalizedText.get("concatenating_videos"), 75);
			List<String> ffArgs = new ArrayList<>(Arrays.asList(
					"-y",
					"-f", "concat",
					"-i", videoListFile.getAbsolutePath(),
					"-an",
					"-c:v", videoCodec,
					"-preset", "ultrafast",
					//"-b:v", videoBitRate,
					"-r", videoFrameRate.toString(),
					"-s", videoSize,
					outputFile.getAbsolutePath()
			));

			//Run the ffmpeg concat
			OutputProcessor<Object> outputProcessor = new OutputProcessor<Object>() {

				@Override
				public Object call() throws Exception {
					String line;
					try {
						Pattern p = Pattern.compile("\\s*frame=.*time=(\\d+):(\\d+):(\\d+.?\\d*)\\s.*");
						boolean in = false;
						while ((line = processStdErr.readLine()) != null) {
							//System.err.println(line);
							Matcher m = p.matcher(line);
							if (m.find()) {
								int hour = Integer.parseInt(m.group(1));
								int minute = Integer.parseInt(m.group(2));
								int time = (int) Math.ceil(Double.parseDouble(m.group(3)));
								time += minute * 60 + hour * 60 * 60;

								concatMonitor.setProgress(null, time * 100 / videoDuration);

								in = true;
							} else if (in) {
								return null;
							}
						}
					} catch (IOException e) {
					}
					return null;
				}

			};
			exitCode = FFmpeg.execute(ffArgs, outputProcessor, null);
			concatMonitor.setProgress(null, 100);
		}

		//Runtime.getRuntime().removeShutdownHook(actionAfterInteruption);
		if (exitCode == 0)
			return outputFile;
		else
			System.out.println("FFmpeg exit code : " + exitCode);

		outputFile.delete();
		logger.info("Done");
		return null;
	}

	/**
	 * Keeps track of sequence element conversion
	 */
	private static class Conversion implements Callable<Conversion> {

		final List<String> ffArgs;
		final OutputProcessor debugOutFile;
		final String name;

		int returnCode;

		private Conversion(List<String> ffArgs, OutputProcessor debugOutFile, String name) {
			this.ffArgs = ffArgs;
			this.debugOutFile = debugOutFile;
			this.name = name;
		}

		@Override
		public Conversion call() throws Exception {
			returnCode = FFmpeg.execute(ffArgs, debugOutFile, null);
			return this;
		}
	}

}
