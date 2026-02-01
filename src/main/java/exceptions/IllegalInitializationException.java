package exceptions;

/**
 * Exception to be used as a preventative measure against needless initialization of a class.
 */
public final class IllegalInitializationException extends RuntimeException {
    /**
     * Initializes a new {@code IllegalInitializationException} by using the parent's,
     * {@link RuntimeException}, constructor.
     */
    public IllegalInitializationException() {
        super("Initialization of this class should not occur");
    }
}
