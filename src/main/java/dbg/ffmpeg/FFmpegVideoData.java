package dbg.ffmpeg;

/**
 * Data object storing video data retrieved by FFmpeg
 */
public class FFmpegVideoData {
	private Integer duration;
	private String codec;
	private String size;
	private String bitRate;
	private Integer frameRate;

	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getCodec() {
		return codec;
	}
	public void setCodec(String codec) {
		this.codec = codec;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getBitRate() {
		return bitRate;
	}
	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}
	public Integer getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(Integer frameRate) {
		this.frameRate = frameRate;
	}
}