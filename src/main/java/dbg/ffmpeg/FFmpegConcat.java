package dbg.ffmpeg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import dbg.data.media.MediaSequence;
import dbg.data.media.element.MediaElement;
import dbg.data.media.element.imported.ImageElement;
import dbg.data.media.element.imported.ImportedMediaElement;
import dbg.data.media.element.imported.VideoElement;
import dbg.exception.UnknownOperatingSystem;
import dbg.ffmpeg.FFmpeg.OutputProcessor;
import dbg.ui.LocalizedText;

public abstract class FFmpegConcat {
	
	private final MediaSequence mediaSequence;
	private final File outputFile;
	private final FFmpegVideoData outputVideoOptions;
	private final int MAX_STEP = 100;
	private int currentStep;
	
	public FFmpegConcat(MediaSequence mediaSequence, File outputFile, FFmpegVideoData outputVideoOptions) {
		super();
		this.mediaSequence = mediaSequence;
		this.outputFile = outputFile;
		this.outputVideoOptions = outputVideoOptions;
	}

	/**
	 * Concatenates a MediaSequence into a concateneted video file
	 * @return true if succeeded; false otherwise
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws UnknownOperatingSystem
	 * @throws ExecutionException
	 */
	public boolean execute() throws IOException, InterruptedException, UnknownOperatingSystem, ExecutionException {
		//final MediaSequence sequence = PropertyManager.getSequence();
		//final String videoSize = PropertyManager.getVideoOutputProperties().getVideoSize();
		final String videoBitRate = 
			(outputVideoOptions != null && outputVideoOptions.getBitRate() != null) ? 
				outputVideoOptions.getBitRate() : "450k" ;
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
				
		currentStep = 1;

		//Part one: convert every media element to same video format
		final List<File> concatFiles = new ArrayList<File>();
		final File videoList = File.createTempFile("DBG-video-list", ".txt");
		setProgress(LocalizedText.converting_elements, currentStep);
		{
			final List<Future<Integer>> convertions = new ArrayList<Future<Integer>>();
			final ExecutorService executor = Executors.newFixedThreadPool(3);
			
			FileWriter fs = new FileWriter(videoList);
			for(final MediaElement element: mediaSequence) {
				final List<String> ffArgs = new ArrayList<String>();
				
				if(element instanceof ImportedMediaElement) {
					if(element instanceof ImageElement) {
						ImageElement image = ((ImageElement)element);
						ffArgs.addAll(Arrays.asList(
							"-loop", "1",
							"-f", "image2",
							"-i", image.getFile().getAbsolutePath(),
							"-t", Integer.toString(image.getDuration()),
							"-r", "1",
							"-s", videoSize,
							"-c:v", "mpeg2video"
						));
					}
					else if(element instanceof VideoElement) {
						VideoElement video = ((VideoElement)element);
						FFmpegVideoData data = FFmpeg.getVideoData(video.getFile());
						
						ffArgs.addAll(Arrays.asList(
							"-i", video.getFile().getAbsolutePath(),
							"-an"
						));
						
						if(data.getCodec().contains("mpeg4") || data.getCodec().contains("h264")) {
							ffArgs.addAll(Arrays.asList(
								"-c:v", "copy",
								"-bsf:v", "h264_mp4toannexb"
							));
						}
						else if(data.getCodec().contains("mpeg2video")) {
							ffArgs.add("-c:v");
							ffArgs.add("copy");
						}
						else {
							ffArgs.add("-c:v");
							ffArgs.add("mpeg2video");
						}
					}
				}
				else {
					//TODO : Generate non imported elements
				}
				
				ffArgs.addAll(Arrays.asList(
					"-f", "mpegts"
				));
				
				File ouputFile = File.createTempFile("DBG-tmp-video", ".ts");
				ffArgs.add(ouputFile.getAbsolutePath());
				ffArgs.add("-y");

				concatFiles.add(ouputFile);
				fs.write("file '"+ouputFile.getAbsolutePath()+"'\n");
				
				convertions.add(executor.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						return FFmpeg.execute(ffArgs, null, null);
					}
				}));
			}
			fs.close();

			//Ensure that every media element were correctly converted to video
			for(Future<Integer> convertion: convertions) {
				int res = convertion.get();
				if(res != 0) return false;
			}
		}
		setProgress(null, currentStep+=5);
		
		//Part two: concatenate video files
		int exitCode;
		setProgress(LocalizedText.concatenating_videos, currentStep);
		{
			List<String> ffArgs = new ArrayList<String>(Arrays.asList(
				"-y",
				"-f", "concat",
				"-i", videoList.getAbsolutePath(),
				"-an",
				"-c:v", videoCodec,
				"-preset", "ultrafast",
				"-b:v", videoBitRate,
				"-r", videoFrameRate.toString(),
				"-s", videoSize,
				FilenameUtils.getBaseName(outputFile.getName())+".mp4"
			));
			
			//Run the ffmpeg concat 
			OutputProcessor<Object> outputProcessor = new OutputProcessor<Object>() {
				@Override
				public Object call() throws Exception {
					String line;
					try {
						Pattern p = Pattern.compile("\\s*frame=.*time=(\\d+):(\\d+):(\\d+.?\\d*)\\s.*");
						boolean in = false;
						while((line = processStdErr.readLine()) != null) {
							System.out.println(line);
							Matcher m = p.matcher(line);
							if(m.find()) {
								int hour = Integer.parseInt(m.group(1));
								int minute = Integer.parseInt(m.group(2));
								int time = (int)Math.ceil(Double.parseDouble(m.group(3)));
								time += minute * 60 + hour * 60 * 60;
								
								int step = (time * (MAX_STEP-currentStep)) / videoDuration;
								setProgress(null, currentStep + step);
								
								in = true;
							}
							else if(in) return null;
						}
					} catch(IOException e) {}
					return null;
				}
			
			};
			exitCode = FFmpeg.execute(ffArgs, outputProcessor, null);
			
			//Delete temporary Files
			videoList.delete();
			for(File file: concatFiles)
				file.delete();
		}

		return exitCode == 0;
	}
	
	public abstract void setProgress(String message, int progress);
}
