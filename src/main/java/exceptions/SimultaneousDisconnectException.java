package exceptions;

/**
 * Exception to be used in the niche case that the {@link network.interfaces.Server} entered an
 * unexpected state due to both {@link network.interfaces.Client}s participating in a
 * {@link game.interfaces.Game} disconnecting; disallowing letting each other know of their
 * opponent's lack of connection.
 */
public final class SimultaneousDisconnectException extends RuntimeException {
    /**
     * Initializes a new {@code SimultaneousDisconnectException} by using the parent's,
     * {@link RuntimeException}, constructor.
     */
    public SimultaneousDisconnectException() {
        super("The players of a game disconnected at the same time");
    }
}
