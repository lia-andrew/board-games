package exceptions;

/**
 * Exception to be used as an idiomatic way to get an {@link java.util.Optional} value
 * or else be thrown.
 */
public final class InvalidServerStateException extends RuntimeException {
    /**
     * Initializes a new {@code InvalidServerStateException} by using the parent's,
     * {@link RuntimeException}, constructor.
     */
    public InvalidServerStateException() {
        super("The server entered an unrecoverable state");
    }
}
