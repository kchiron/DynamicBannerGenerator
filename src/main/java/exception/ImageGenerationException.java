package exception;

public class ImageGenerationException extends Exception {

	public ImageGenerationException(String message) {
		super(message);
	}

	public ImageGenerationException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ImageGenerationException() {}
}
