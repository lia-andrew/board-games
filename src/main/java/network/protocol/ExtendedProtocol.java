package network.protocol;

/**
 * Enumeration of the protocol extensions that the {@code Server} and/or {@code Client} can
 * implement and support when communicating with each other.
 *
 * @see network.interfaces.Client
 * @see network.interfaces.Server
 */
public enum ExtendedProtocol {
    NAMEDQUEUES("QUEUE with name"), RANK("RANK"), CHAT("CHAT, WHISPER");

    private final String description;

    /**
     * Constructs a new {@code ExtendedProtocol} instance with the provided description.
     * @param description The description of this {@code ExtendedProtocol} instance
     */
    ExtendedProtocol(String description) {
        this.description = description;
    }

    /**
     * Returns the description of this {@code ExtendedProtocol} instance.
     * @return The description of this {@code ExtendedProtocol} instance
     */
    public String getDescription() {
        return description;
    }
}
