package exception;

/**
 * Exception used to notify an unknown operating system
 * @author gcornut
 */
public class UnknownOperatingSystem extends Exception {

	private static final long serialVersionUID = 1L;

	public UnknownOperatingSystem(String message) {
		super(message);
	}
	
	public UnknownOperatingSystem(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnknownOperatingSystem(Throwable cause) {
		super(cause);
	}
	
}
