package dbg.data.media.element.imported;

import java.io.File;

import dbg.ffmpeg.FFmpeg;

public class VideoElement extends ImportedMediaElement {

	private static final long serialVersionUID = 1L;

	public VideoElement(String title, File inportedFile) {
		super(title, inportedFile, FFmpeg.getVideoDuration(inportedFile));
	}
	
	@Override
	public void setFile(File inportedFile) {
		super.setFile(inportedFile);
		setDuration(FFmpeg.getVideoDuration(inportedFile));
	}
}
