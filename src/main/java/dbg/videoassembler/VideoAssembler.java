package dbg.videoassembler;

public class VideoAssembler {

	public static VideoAssembler _instance;
	
	private VideoAssembler() {
		
	}
	
	public static VideoAssembler initialize() {
		if(_instance == null) _instance = new VideoAssembler();
		return _instance;
	}
	
	
	
}
