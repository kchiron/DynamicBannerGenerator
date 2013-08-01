package ffmpeg;

public class CommandArgument {
	private String name;
	private String value;
	
	public CommandArgument(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public boolean isEmpty() {
		return value == null || value.trim() == "";
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return name + " " + value;
	}
}
