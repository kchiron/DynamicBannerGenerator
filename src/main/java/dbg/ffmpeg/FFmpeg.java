package dbg.ffmpeg;

import dbg.exception.UnknownOperatingSystem;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FFmpeg class encapsulating the ffmpeg native executables specific of each platform.
 *
 * @author gcornut
 */
public class FFmpeg {

	private static File ffmpegProgram = null;
	protected static final OutputProcessor<Object> debugPrint = new OutputProcessor<Object>() {
		@Override
		public Object call() throws Exception {
			String line;
			try {
				while ((line = processStdErr.readLine()) != null) {
					System.err.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	/**
	 * Initializes the class by searching the suitable ffmpeg executable for the detected operating system (and architecture)
	 *
	 * @throws IOException            if the ffmpeg executable is not found
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 */
	private synchronized static void initialize() throws UnknownOperatingSystem, IOException {
		String programName = "ffmpeg-" + detectOS();

		File nativeFolder = new File("native");
		if (!nativeFolder.exists()) nativeFolder.mkdir();
		File program = new File(nativeFolder, programName);

		if (!program.exists()) {
			InputStream original = null;
			try {
				original = FFmpeg.class.getResourceAsStream(programName);
				Files.copy(original, program.getAbsoluteFile().toPath());
				program.setExecutable(true);
			} catch (IOException e) {
				throw e;
			} finally {
				if (original != null) original.close();
			}
		}
		ffmpegProgram = program;

		/*
		InputStream original = FFmpeg.class.getResourceAsStream(programName);

		File program = File.createTempFile("ffmpeg", suffix);
		Files.copy(original, program.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
		program.setExecutable(true);
		ffmpegProgram = program;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				FFmpeg.ffmpegProgram.delete();
			}
		});
		original.close();
		*/
	}

	/**
	 * Executes the ffmpeg program
	 */
	@SuppressWarnings("unchecked")
	public static int execute(List<String> arguments, @SuppressWarnings("rawtypes") final OutputProcessor outputProcessor, final InputGenerator inputGenerator) throws IOException, InterruptedException, UnknownOperatingSystem {
		if (ffmpegProgram == null || !ffmpegProgram.exists()) initialize();

		List<String> ffmpegCommand = new ArrayList<>(arguments);
		ffmpegCommand.add(0, ffmpegProgram.getAbsolutePath());

		//System.out.println(ffmpegCommand);

		final ProcessBuilder pb = new ProcessBuilder(ffmpegCommand);
		final Process ffmpegProcess = pb.start();

		//If any, launch the input generator for the ffmpeg process
		if (inputGenerator != null) {
			inputGenerator.setOutputStreamToProcessInput(ffmpegProcess.getOutputStream());
			inputGenerator.start();
		}

		//If any, launch the output processor for the ffmpeg process
		if (outputProcessor != null) {
			outputProcessor.setProcessStreams(new BufferedReader(new InputStreamReader(ffmpegProcess.getErrorStream())), new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream())));
			outputProcessor.setFuture(Executors.newFixedThreadPool(1).submit(outputProcessor));
		}

		ffmpegProcess.waitFor();

		return ffmpegProcess.exitValue();
	}

	public static int execute(List<String> arguments) throws IOException, InterruptedException, UnknownOperatingSystem {
		return execute(arguments, null, null);
	}

	/**
	 * Gets the duration of a video in seconds
	 *
	 * @param video
	 * @return
	 */
	public static int getVideoDuration(File video) {
		return getVideoData(video).getDuration();
	}


	public static FFmpegVideoData getVideoData(File video) {
		List<String> args = Arrays.asList("-i", video.getPath());
		final FFmpegVideoData out = new FFmpegVideoData();

		OutputProcessor<FFmpegVideoData> processor = new OutputProcessor<FFmpegVideoData>() {
			@Override
			public FFmpegVideoData call() throws Exception {
				String line;
				try {
					Pattern pg = Pattern.compile("\\s+Duration:\\s(\\d{2}):(\\d{2}):(\\d{2})");
					Pattern pr = Pattern.compile("\\s+Stream.*Video:\\s(.*)");

					while ((line = processStdErr.readLine()) != null) {
						if (line.contains("Duration:")) {
							Matcher m = pg.matcher(line);

							if (m.find()) {
								final int hours = Integer.parseInt(m.group(1));
								final int minutes = Integer.parseInt(m.group(2));
								final int seconds = Integer.parseInt(m.group(3));
								out.setDuration(hours * 60 * 60 + minutes * 60 + seconds);
							}
						}
						if (line.contains("Video:")) {
							Matcher m = pr.matcher(line);

							if (m.find()) {
								String[] videoInfos = m.group(1).split(",");

								out.setCodec(videoInfos[0]);

								String size = videoInfos[2];
								size = size.replaceAll("^\\s", "").replaceAll("\\s.*$", "");
								out.setSize(size);

								String bitRate = videoInfos[3];
								bitRate = bitRate.replace(" ", "").replace("kb/s", "k");
								out.setBitRate(bitRate);

								return out;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return out;
			}
		};
		try {
			FFmpeg.execute(args, processor, null);
		} catch (IOException | InterruptedException | UnknownOperatingSystem e) {
			e.printStackTrace();
		}

		try {
			return processor.getFuture().get();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * Convert an image to a video with a defined duration in seconds and a given name
	 *
	 * @param outputFolder the output folder
	 * @param duration     duration of the video in seconds
	 * @param inputImage   input image file
	 * @param outputName   the name of the video that will be created (excluding extension)
	 * @return The output video if all went good; null otherwise
	 * @throws IOException            if the ffmpeg executable id not found
	 * @throws InterruptedException   if the fmmpeg have been interrupted
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 */
	public static File convertImageToVideo(File outputFolder, int duration, File inputImage, String outputName, boolean debug) throws IOException, InterruptedException, UnknownOperatingSystem {
		final String videoOutputPath = outputFolder.getPath() + File.separator + outputName + ".ts";

		final List<String> ffArgs = new ArrayList<>(Arrays.asList(
				"-loop", "1",
				"-f", "image2",
				"-i", inputImage.getPath(),
				"-t", "" + duration,
				"-c:v", "mpeg2video",
				"-r", "1",
				"-s", "1440x900",
				"-b:v", "450k",
				videoOutputPath,
				"-y"
		));

		if (execute(ffArgs, debug ? debugPrint : null, null) == 0)
			return new File(videoOutputPath);
		else
			return null;
		//ffmpeg -loop 1 -f image2 -i IN.jpg -s 1440x900 -c:v libx264 -t 10 -b:v 400k -r 21 OUT.mp4 -y
		//ffmpeg -loop 1 -f image2 -i NIK_001.jpg -s 1440x900 -c:v mpeg2video -t 10 -b:v 400k -r 1 NIK_001.ts -y
	}

	/**
	 * Ouput processor class used to process standard output and error output from the ffmpeg process.
	 */
	public static abstract class OutputProcessor<T> implements Callable<T> {
		private Future<T> future;
		protected BufferedReader processStdOut;
		protected BufferedReader processStdErr;

		public void setProcessStreams(BufferedReader processStdErr, BufferedReader processStdOut) {
			this.processStdErr = processStdErr;
			this.processStdOut = processStdOut;
		}

		public Future<T> getFuture() {
			return future;
		}

		public void setFuture(Future<T> future) {
			this.future = future;
		}
	}

	/**
	 * Input generator class used to generate a stream for the input of a ffmpeg process.
	 */
	public static abstract class InputGenerator extends Thread {
		protected OutputStream outputStreamToProcessInput;

		public void setOutputStreamToProcessInput(OutputStream outputStreamToProcessInput) {
			this.outputStreamToProcessInput = outputStreamToProcessInput;
		}
	}

	private static String detectOS() throws UnknownOperatingSystem {
		String os = (System.getProperty("os.name") + " " + System.getProperty("os.arch")).toLowerCase();
		System.out.println("[" + os + "]");

		UnknownOperatingSystem error = new UnknownOperatingSystem("No executable available for this operating system");

		if (os.contains("mac"))
			return "mac";
		else if (os.contains("win")) {
			System.out.println();
			return "win" + (os.contains("64") || System.getProperty("java.library.path").contains(" (x86)") ? "64" : "32") + ".exe";
		} else throw error;
	}
}
