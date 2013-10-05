package exception;

public class IncorrectVideoSizeException extends Exception {

	public IncorrectVideoSizeException(String message) {
		super(message);
	}

	public IncorrectVideoSizeException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public IncorrectVideoSizeException() {}
}
