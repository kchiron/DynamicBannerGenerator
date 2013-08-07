package exception;

public class ZeroOrNegativeNumberException extends Exception {

	public ZeroOrNegativeNumberException(String message) {
		super(message);
	}

	public ZeroOrNegativeNumberException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ZeroOrNegativeNumberException() {}
}
