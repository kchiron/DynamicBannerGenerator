package dbg.data.media.element.imported;

import dbg.ffmpeg.FFmpeg;

import java.io.File;

public class VideoElement extends ImportedMediaElement {

	private static final long serialVersionUID = 1L;

	public VideoElement(String title, File inportedFile) {
		super(title, inportedFile, (inportedFile != null ? FFmpeg.getVideoDuration(inportedFile) : 0));
	}

	@Override
	public void setFile(File inportedFile) {
		if (inportedFile != null) {
			super.setFile(inportedFile);
			setDuration(FFmpeg.getVideoDuration(inportedFile));
		}
	}
}
