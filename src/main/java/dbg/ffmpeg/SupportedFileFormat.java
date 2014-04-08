package dbg.ffmpeg;

import java.util.Arrays;
import java.util.List;

/**
 * Keeps record of supported data.media file format in the program
 * @author gcornut
 */
public class SupportedFileFormat {

	private static String[] imageFormats = {"jpg", "jpeg", "png"};
	private static String[] videoFormats = {"mp4", "wmv", "mpeg4", "avi", "mts"};
	
	public static String[] getImageFormats() {
		return imageFormats;
	}
	
	public static String getImageFormatsString() {
		return join(imageFormats);
	}

	public static List<String> getImageFormatsList() {
		return Arrays.asList(imageFormats);
	}
	
	public static String[] getVideoFormats() {
		return videoFormats;
	}

	public static String getVideoFormatsString() {
		return join(videoFormats);
	}
	
	public static List<String> getVideoFormatsList() {
		return Arrays.asList(videoFormats);
	}
	
	private static String join(String[] array) {
		if(array.length == 0) return null;
		
		StringBuilder out = new StringBuilder();
		out.append(array[0]);
		
		for(int i = 1; i < array.length; i++)
			out.append(", ").append(array[i]);
		
		return out.toString();
	}
}
