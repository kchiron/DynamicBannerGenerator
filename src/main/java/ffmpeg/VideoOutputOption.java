package ffmpeg;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class VideoOutputOption {
	
	private LinkedHashMap<String, CommandArgument> properties;
	private String fileName;
	private String extension;
	
	
	public VideoOutputOption(String size, String bitRate, String fps, String codec, String extension) {
		this(null, null, null, codec, extension, null);
	}

	public VideoOutputOption(String size, String bitRate, String fps, String codec, String extension, String fileName) {
		properties = new LinkedHashMap<String, CommandArgument>(4);
		
		properties.put("size", new CommandArgument("-s", size));
		properties.put("bitRate", new CommandArgument("-b:v", bitRate));
		properties.put("fps", new CommandArgument("-r", fps));
		properties.put("codec", new CommandArgument("-c:v", codec));
		
		this.extension = extension;
		this.fileName = fileName;
	}

	public void setSize(String size) {
		properties.get("size").setValue(size);
	}

	public void setBitRate(String bitRate) {
		properties.get("bitRate").setValue(bitRate);
	}

	public void setFps(String fps) {
		properties.get("fps").setValue(fps);
	}

	public void setCodec(String codec) {
		properties.get("codec").setValue(codec);
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String[] toArgs(String fileName) {
		return toString().split(" ");
	}
	
	@Override
	public String toString() {
		String args = "";
		Map.Entry<String, CommandArgument> property;
		
		Iterator<Map.Entry<String, CommandArgument>> it = properties.entrySet().iterator();
		while(it.hasNext()) {
			property = it.next();
			
			if(!property.getValue().isEmpty())
				args += property.getValue().toString() + " ";
		}
		
		if(fileName != null && extension != null)
			args += fileName + "." + extension;
		
		return args;
	}
	
}