package ffmpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.CustomArrayUtil;
import exception.UnknownOperatingSystem;

/**
 * FFmpeg class encapsulating the ffmpeg native executables specific of each platform.
 * @author gcornut
 */
public class FFmpeg extends ExecutableCommand {

	private static String pathToExecutable = null;
	private static String defaultSize;
	private static String defaultBitRate;

	/**
	 * Initializes the class by searching the suitable ffmpeg executable for the detected operating system (and architecture) 
	 * @throws IOException if the ffmpeg executable is not found
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 */
	private static void initialize() throws IOException, UnknownOperatingSystem {
		defaultSize = "1440x900";
		defaultBitRate = "450k";
	
		String path = "ffmpeg-native";
		System.getProperty("os.name").toLowerCase();

		String os = (System.getProperty("os.name") + " " + System.getProperty("os.arch")).toLowerCase();

		if(os.contains("mac"))
			path = path + File.separator + "ffmpeg-mac";
		else if(os.contains("win")) {
			path = path + File.separator + "ffmpeg-w" + (os.contains("64") ? "64" : "32") + ".exe";
		}
		else throw new UnknownOperatingSystem("No executable available for this operating system");

		pathToExecutable = path;

		if(!(new File(pathToExecutable)).exists())
			throw new IOException("Executable not found");
	}

	/**
	 * Executes the ffmpeg program
	 * @param arguments arguments passed to the ffmpeg program
	 * @param processor a output processor that will handle the stout and stderr results (optional, can be null)
	 * @throws IOException if the ffmpeg executable id not found
	 * @throws InterruptedException if the fmmpeg have been interrupted
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 * @return exit value of the ffmpeg program
	 */
	public static int execute(String[] arguments, OutputProcessor processor) throws IOException, InterruptedException, UnknownOperatingSystem {
		if(pathToExecutable == null) initialize();

		String cmd[];

		if(arguments != null)  {
			cmd = CustomArrayUtil.mergeArray(new String[]{pathToExecutable}, arguments);

			for (String p: cmd)
				System.out.print(p+" ");
			System.out.println();
		} 
		else {
			cmd = new String[]{pathToExecutable};
		}

		ProcessBuilder pb = new ProcessBuilder(cmd);
		Process p = pb.start();

		p.waitFor();

		if(processor != null) {
			processor.process(
				new BufferedReader(new InputStreamReader(p.getErrorStream())),
				new BufferedReader(new InputStreamReader(p.getInputStream()))
			);
		}

		return p.exitValue();
	}

	/** Executes the ffmpeg program without output processor  */
	public static int execute(String[] arguments) throws IOException, InterruptedException, UnknownOperatingSystem {
		return execute(arguments, null);
	}

	/**
	 * Gets the duration of a video in seconds
	 * @param video
	 * @return
	 */
	public static int getVideoDuration(FileExtended video) {
		String[] args  = new String[]{
			"-i",
			video.getPath()
		};
		final ArrayList<Integer> time = new ArrayList<Integer>();

		OutputProcessor processor = (new FFmpeg()).new OutputProcessor() {
			public void process(BufferedReader stderr, BufferedReader stdout) throws IOException{
				String line;
				while((line = stderr.readLine()) != null) {
					if(line.contains("Duration")) {
						Pattern p = Pattern.compile("\\s+Duration:\\s(\\d{2}):(\\d{2}):(\\d{2})");
						Matcher m = p.matcher(line);

						if (m.find()) {
							final int hours = Integer.parseInt(m.group(1));
							final int minutes = Integer.parseInt(m.group(2));
							final int seconds = Integer.parseInt(m.group(3));
							time.add(hours*60*60 + minutes*60 + seconds);
							break;
						}
					}
				}
			}
		};
		try {
			FFmpeg.execute(args, processor);
		} catch (IOException | InterruptedException | UnknownOperatingSystem e) {
			e.printStackTrace();
		}
		
		return time.size() > 0 ? time.get(0) : -1;
	}

	/**
	 * Concatenates a list of videos into one video.
	 * @param inputVideoFiles output video file
	 * @param inputVideoFiles list of input video files
	 * @return the exit code of ffmpeg
	 * @throws IOException if the ffmpeg executable id not found
	 * @throws InterruptedException if the fmmpeg have been interrupted
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 */
	public static int concatenateVideos(FileExtended outputVideoFile, FileExtended... inputVideoFiles) throws IOException, InterruptedException, UnknownOperatingSystem {
		final String size = defaultSize;
		final String bitRate = defaultBitRate;
		final String fps = "21";

		final VideoOutputOption tsFormatOption = new VideoOutputOption(size, bitRate, fps, "mpeg2video", "ts");

		FileExtended[] tmpVideoFiles = new FileExtended[inputVideoFiles.length];

		//First convert all videos to mpeg2 transport streams (if not already done)
		for(int i = 0; i < inputVideoFiles.length; i++) {
			FileExtended inputVideo = inputVideoFiles[i];

			if(!inputVideo.getName().endsWith("ts")) {
				String inputFolder = inputVideo.getParent();
				String tmpVideo = (inputFolder != null ? inputFolder + File.separator : "") + "tmp_" + inputVideo.getNameWithoutExtension();
				tsFormatOption.setFileName(tmpVideo);

				String arguments = "-y -i " + inputVideo.getPath() + " " + tsFormatOption.toString();

				if(execute(arguments.split(" ")) != 0) 
					return -1;

				tmpVideoFiles[i] = new FileExtended(tmpVideo + ".ts");
			}
			else tmpVideoFiles[i] = inputVideoFiles[i];
		}

		final String outputFolder = outputVideoFile.getParent() + File.separator;

		//List video to concatenate into a file
		File videoList = new File(outputFolder+"videoList.txt");
		FileWriter fs = new FileWriter(videoList);
		for(FileExtended video: tmpVideoFiles) {
			fs.write("file '"+video.getPath()+"'\n");
		}
		fs.close();

		String arguments = "-y -f concat -i "+videoList.getPath();

		//Defining output format and name
		final String outputFileName = outputFolder + outputVideoFile.getNameWithoutExtension();
		VideoOutputOption mp4FormatOption = new VideoOutputOption(size, bitRate, fps, "libx264", "mp4", outputFileName);
		arguments += " " + mp4FormatOption.toString();

		int exitcode = execute(arguments.split(" "));

		boolean deleteWorked = true;
		deleteWorked &= videoList.delete();
		//Delete temporary Files
		for(FileExtended tmpVideoFile: tmpVideoFiles) {
			deleteWorked &= tmpVideoFile.delete();
		}

		return deleteWorked ? exitcode : -1;
	}

	/**
	 * Convert an image to a video with a defined duration in seconds. The video will take the name of the image
	 * @param outputFolder pthe output folder
	 * @param duration duration of the video in seconds
	 * @param inputImage input image file
	 * @return The output video if all went good; null otherwise
	 * @throws IOException if the ffmpeg executable id not found
	 * @throws InterruptedException if the fmmpeg have been interrupted
	 * @throws UnknownOperatingSystem if no ffmpeg executable is available for the detected operating system
	 */
	public static FileExtended convertImageToVideo(File outputFolder, int duration, FileExtended inputImage) throws IOException, InterruptedException, UnknownOperatingSystem {
		final String imageName = inputImage.getNameWithoutExtension();
		final String videoPath = outputFolder.getPath() + File.separator + imageName;

		final String videoOptions = (new VideoOutputOption(defaultSize, defaultBitRate, "1", "mpeg2video", "ts", videoPath)).toString();

		final String args = "-loop 1 -f image2 -i "+inputImage.getPath()+" -t "+duration+" "+ videoOptions +" -y";

		if(execute(args.split(" ")) == 0)
			return new FileExtended(videoPath+".ts");
		else
			return null;
		//ffmpeg -loop 1 -f image2 -i IN.jpg -s 1440x900 -c:v libx264 -t 10 -b:v 400k -r 21 OUT.mp4 -y
		//ffmpeg -loop 1 -f image2 -i NIK_001.jpg -s 1440x900 -c:v mpeg2video -t 10 -b:v 400k -r 1 NIK_001.ts -y
	}


	/** Ouput processor class used to process stdout output and stderr output from the ffmpeg command */
	public abstract class  OutputProcessor {
		public abstract void process(BufferedReader stderr, BufferedReader stdout) throws IOException;
	}
}
