package cab302.viewer.exceptions;

public class BadImageFormatException extends Exception {
    /**
     * Exception if the image is not a Base64-encoded string or URL
     * @param message Message to send upon throw
     */
    public BadImageFormatException(String message) {
        super(message);
    }
}
