package network.interfaces;

/**
 * Interface used to represent a handler which has the responsibility of sending output to and
 * parsing input from a {@code Connection}.
 *
 * @see Client
 * @see Server
 * @see network.Connection
 */
public interface Handler {
    /**
     * Parses the input provided by a {@code Connection} and triggers any logic that should be
     * executed due to the type of received input.
     * @param input The input provided by a {@code Connection}
     */
    void readInput(String input);

    /**
     * Handles any logic that should be performed when the {@code Connection} between a
     * {@code Server} and {@code Client} closes.
     */
    void handleDisconnect();
}
