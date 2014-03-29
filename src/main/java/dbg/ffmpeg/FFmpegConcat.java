package dbg.ffmpeg;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.generated.GeneratedMediaElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.exception.UnknownOperatingSystem;
import dbg.ffmpeg.FFmpeg.OutputProcessor;
import dbg.ui.LocalizedText;
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

public abstract class FFmpegConcat {

	private final MediaSequence mediaSequence;
	private final File outputFile;
	private final FFmpegVideoData outputVideoOptions;
	private final int MAX_STEP = 100;
	private int currentStep;

	public FFmpegConcat(MediaSequence mediaSequence, File outputFile, FFmpegVideoData outputVideoOptions) {
		super();
		this.mediaSequence = mediaSequence;
		this.outputFile = new File(outputFile.getAbsoluteFile().getParentFile(), FilenameUtils.getBaseName(outputFile.getName()) + ".mp4");
		this.outputVideoOptions = outputVideoOptions;
	}

	/**
	 * Concatenates a MediaSequence into a concatenated video file
	 *
	 * @return true if succeeded; false otherwise
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownOperatingSystem
	 * @throws ExecutionException
	 */
	public File execute() throws IOException, InterruptedException, UnknownOperatingSystem, ExecutionException {
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
		final Integer videoDuration = mediaSequence.getDuration();

		Thread actionAfterInteruption;

		//Start
		currentStep = 0;

		//TODO: find why concat fails if videoSize is different from the size of the concatenated elements

		/*
		 * Part one: convert every media element to same video format
		 */
		final List<File> concatFiles = new ArrayList<>();
		final List<File> temporaryGeneratedImages = new ArrayList<>();
		final File videoList = File.createTempFile("DBG-video-list", ".txt");
		setProgress(LocalizedText.converting_elements, currentStep);
		{
			final List<Future<Integer>> conversions = new ArrayList<>();
			final ExecutorService executor = Executors.newFixedThreadPool(3);

			FileWriter fs = new FileWriter(videoList);
			for (final MediaElement element : mediaSequence) {
				final List<String> ffArgs = new ArrayList<>();
				File mediaFile = null;
				FFmpegVideoData mediaFileMetaData = null;

				// If the media element is an image (or a soon generated image)
				if (element instanceof ImageElement || element instanceof GeneratedMediaElement) {
					if (element instanceof ImageElement)
						mediaFile = ((ImageElement) element).getFile();
					else {
						try {
							mediaFile = ((GeneratedMediaElement) element).generateImage();
							temporaryGeneratedImages.add(mediaFile);
						} catch (Exception e) {
							e.printStackTrace();
							//TODO : warn the user that the image could not be generated
						}
					}
					mediaFileMetaData = FFmpeg.getVideoData(mediaFile);

					if (mediaFile != null) {
						ffArgs.addAll(Arrays.asList(
								"-loop", "1",
								"-f", "image2",
								"-i", mediaFile.getAbsolutePath(),
								"-t", Integer.toString(element.getDuration()),
								"-r", "1"
						));

						// If the image is not same size as asked => resize during conversion to video
						if (!mediaFileMetaData.getSize().equals(videoSize)) {
							ffArgs.add("-s");
							ffArgs.add(videoSize);
							System.out.println("Resizing [" + element + "] from: " + mediaFileMetaData.getSize() + " to:" + videoSize);
						}

						ffArgs.add("-c:v");
						ffArgs.add("mpeg2video");
					}
				}
				// If the media element is a video
				else if (element instanceof VideoElement) {
					VideoElement video = ((VideoElement) element);
					mediaFile = video.getFile();
					mediaFileMetaData = FFmpeg.getVideoData(mediaFile);

					ffArgs.addAll(Arrays.asList(
							"-i", mediaFile.getAbsolutePath(),
							"-an"
					));

					// If the video is not same size as asked => resize during conversion
					//  => If a video need to be resized it also have to be reencoded (no fast conversion)
					boolean resized = false;
					if (!mediaFileMetaData.getSize().equals(videoSize)) {
						ffArgs.add("-s");
						ffArgs.add(videoSize);
						System.out.println("Resizing [" + element + "] from: " + mediaFileMetaData.getSize() + " to:" + videoSize);
						resized = true;
					}

					/*
					* conversion options (with fast conversion if possible)
					*/
					// If the video is already in mpeg2video format => no encoding at all (fastest)
					if (!resized && mediaFileMetaData.getCodec().contains("mpeg2video")) {
						ffArgs.add("-c:v");
						ffArgs.add("copy");
					}
					// If the video has mp4 codec => copy the video with few modification (faster)
					else if (!resized && (mediaFileMetaData.getCodec().contains("mpeg4") || mediaFileMetaData.getCodec().contains("h264"))) {
						ffArgs.addAll(Arrays.asList(
								"-c:v", "copy",
								"-bsf:v", "h264_mp4toannexb"
						));
					}
					// Else encode the video to mpeg2video (slower)
					else {
						ffArgs.add("-c:v");
						ffArgs.add("mpeg2video");
					}

				}

				// Prepare FFmpeg conversion and launch
				if (ffArgs.size() > 1) {
					if (mediaFileMetaData == null)
						if (!mediaFileMetaData.getSize().equals(videoSize)) {
							ffArgs.add("-s");
							ffArgs.add(videoSize);
							System.out.println("Resizing [" + element + "]");
						}

					ffArgs.add("-f");
					ffArgs.add("mpegts");

					File outputFile = File.createTempFile("DBG-tmp-video", ".ts");
					ffArgs.add(outputFile.getAbsolutePath());
					ffArgs.add("-y");

					concatFiles.add(outputFile);
					fs.write("file '" + outputFile.getAbsolutePath() + "'\n");

					// Launch FFmpeg conversion to video
					conversions.add(executor.submit(new Callable<Integer>() {
						@Override
						public Integer call() throws Exception {
							return FFmpeg.execute(ffArgs, null, null);
						}
					}));
				}
			}
			fs.close();

			//Actions to do if the process is killed
			actionAfterInteruption = new Thread() {
				@Override
				public void run() {
					// Delete temporary files and output file
					deleteAllFile(new ArrayList<File>() {{
						addAll(concatFiles);
						addAll(temporaryGeneratedImages);
						add(videoList);
						add(outputFile);
					}});
				}
			};
			Runtime.getRuntime().addShutdownHook(actionAfterInteruption);

			//Ensure that every media element were correctly converted to video
			int step = 0;
			final int MAX_PERCENT = 25;
			for (Future<Integer> conversion : conversions) {
				Integer res = null;
				try {
					res = conversion.get();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (res == null || res != 0) {
					System.out.println("FFMPEG[" + conversions.indexOf(conversion) + "] exit code : " + res);
					deleteAllFile(concatFiles);
					deleteAllFile(temporaryGeneratedImages);
					return null;
				}
				step++;
				setProgress(null, currentStep + (step * MAX_PERCENT) / conversions.size());
			}
			setProgress(null, currentStep += step);
		}

		/*
		 * Part two: concatenate video files
		 */
		int exitCode;
		setProgress(LocalizedText.concatenating_videos, currentStep);
		{
			List<String> ffArgs = new ArrayList<>(Arrays.asList(
					"-y",
					"-f", "concat",
					"-i", videoList.getAbsolutePath(),
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
						int step = 0;
						while ((line = processStdErr.readLine()) != null) {
							//System.err.println(line);
							Matcher m = p.matcher(line);
							if (m.find()) {
								int hour = Integer.parseInt(m.group(1));
								int minute = Integer.parseInt(m.group(2));
								int time = (int) Math.ceil(Double.parseDouble(m.group(3)));
								time += minute * 60 + hour * 60 * 60;

								step = (time * (MAX_STEP - currentStep)) / videoDuration;
								setProgress(null, currentStep + step);

								in = true;
							} else if (in) {
								currentStep = step;
								return null;
							}
						}
					} catch (IOException e) {}

					return null;
				}

			};
			try {
				exitCode = FFmpeg.execute(ffArgs, outputProcessor, null);
			} catch (InterruptedException e) {
				// Delete temporary files and output file
				actionAfterInteruption.start();

				throw e;
			}

			// Delete temporary files
			deleteAllFile(new ArrayList<File>() {{
				addAll(concatFiles);
				addAll(temporaryGeneratedImages);
				add(videoList);
			}});
		}

		setProgress(null, MAX_STEP);
		Runtime.getRuntime().removeShutdownHook(actionAfterInteruption);
		if (exitCode == 0)
			return outputFile;
		else
			System.out.println("FFmpeg exit code : " + exitCode);

		deleteAllFile(Arrays.asList(outputFile));
		return null;
	}

	/**
	 * Deletes all files given in parameter
	 * @param filesToDelete
	 */
	private boolean deleteAllFile(List<File> filesToDelete) {
		boolean worked = true;
		for (File file : filesToDelete)
			worked &= file.delete();
		return worked;
	}

	public abstract void setProgress(String message, int progress);
}
