package data.media.element.imported;

import ffmpeg.FFmpeg;
import ffmpeg.FileExtended;

public class VideoElement extends InportedMediaElement {

	private static final long serialVersionUID = 1L;

	public VideoElement(String title, FileExtended inportedFile) {
		super(title, inportedFile, FFmpeg.getVideoDuration(inportedFile));
	}
	
	@Override
	public void setInportedFile(FileExtended inportedFile) {
		super.setInportedFile(inportedFile);
		setDuration(FFmpeg.getVideoDuration(inportedFile));
	}
}
