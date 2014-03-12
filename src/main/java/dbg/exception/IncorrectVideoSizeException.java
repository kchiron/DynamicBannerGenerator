package dbg.exception;

public class IncorrectVideoSizeException extends Exception {

	private static final long serialVersionUID = 1L;

	public IncorrectVideoSizeException(String message) {
		super(message);
	}

	public IncorrectVideoSizeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IncorrectVideoSizeException() {}
}
