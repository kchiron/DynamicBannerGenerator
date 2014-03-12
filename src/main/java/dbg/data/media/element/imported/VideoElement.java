package dbg.data.media.element.imported;

import dbg.ffmpeg.FFmpeg;
import dbg.ffmpeg.FileExtended;

public class VideoElement extends ImportedMediaElement {

	private static final long serialVersionUID = 1L;

	public VideoElement(String title, FileExtended inportedFile) {
		super(title, inportedFile, FFmpeg.getVideoDuration(inportedFile));
	}
	
	@Override
	public void setFile(FileExtended inportedFile) {
		super.setFile(inportedFile);
		setDuration(FFmpeg.getVideoDuration(inportedFile));
	}
}
