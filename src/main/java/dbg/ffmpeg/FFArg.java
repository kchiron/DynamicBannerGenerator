package dbg.ffmpeg;

public class FFArg {
	private final String name;
	private String value;
	
	public FFArg(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public FFArg(String name) {
		this(name, null);
	}
	
	public boolean hasValue() {
		return this.value != null;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return new StringBuilder(name)
			.append(hasValue()?" ":"")
			.append(hasValue()?this.value:"")
			.toString();
	}
}
