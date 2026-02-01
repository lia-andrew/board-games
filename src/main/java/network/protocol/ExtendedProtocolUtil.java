package network.protocol;

import exceptions.IllegalInitializationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class which extends {@code ProtocolUtil} to provide constants and methods to facilitate
 * potentially useful but not necessary communication between a {@code Server} and its
 * {@code Client}s.
 *
 * @see IllegalInitializationException
 * @see network.interfaces.Client
 * @see network.interfaces.Server
 * @see ProtocolUtil
 */
public final class ExtendedProtocolUtil extends ProtocolUtil {
    public static final String RANK = "RANK";
    public static final String CHAT = "CHAT";
    public static final String WHISPER = "WHISPER";
    public static final String CANNOTWHISPER = "CANNOTWHISPER";
    //TODO add message encryption as another extension

    /**
     * An intentionally private constructor used to signify that this class should never be
     * initialized from other files. If the reflection API is used to access this constructor, an
     * {@code IllegalInitializationException} will be thrown to ensure the inability to initialize.
     */
    private ExtendedProtocolUtil() {
        throw new IllegalInitializationException();
    }

    /**
     * Constructs a protocol message to request the ranking information of active and inactive
     * {@code Client}s on the {@code Server}.
     * @return A protocol message to request the ranking information of active and inactive
     *          {@code Client}s on the {@code Server}.
     */
    public static String rank() {
        return RANK;
    }

    /**
     * Constructs a protocol message to send the ranking information of active and inactive
     * {@code Client}s on the {@code Server}.
     * @param userWins The set of {@code Client}s' rank information
     * @return A protocol message to send the ranking information of active and inactive
     *          {@code Client}s on the {@code Server}.
     */
    public static String rank(Set<Map.Entry<String, Integer>> userWins) {
        return RANK + SEPARATOR +
                userWins.stream().map(entry -> entry.getKey() + SEPARATOR + entry.getValue())
                        .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * Constructs a protocol message to request to send a global message to all {@code Client}s.
     * @param message The message to send
     * @return A protocol message to request to send a global message to all {@code Client}s
     */
    public static String chat(String message) {
        return CHAT + SEPARATOR + message;
    }

    /**
     * Splits a message which does not contain the name of the sender into separate, more easily
     * usable, parts.
     * @param input The received message
     * @return A message, without the name of the sender, split separate, more easily usable, parts
     */
    public static String[] splitUnnamedMessage(String input) {
        return input.split(ProtocolUtil.SEPARATOR, 2);
    }

    /**
     * Constructs a protocol message to send a global message to a {@code Client}.
     * @param username The name of the sender
     * @param message The message to send
     * @return A protocol message to send a global message to a {@code Client}
     */
    public static String globalChat(String username, String message) {
        return CHAT + SEPARATOR + username + SEPARATOR + message;
    }

    /**
     * Splits a message which contains the name of the sender into separate, more easily usable,
     * parts.
     * @param input The received message
     * @return A message, with the name of the sender, split separate, more easily usable, parts
     */
    public static String[] splitNamedMessage(String input) {
        return input.split(ProtocolUtil.SEPARATOR, 3);
    }

    /**
     * Constructs a protocol message both to send a whisper message to a {@code Client} with the
     * provided username.
     * @param username The username of the {@code Client} who should receive the message
     * @param message The message to send
     * @return A protocol message both to send a whisper message to a {@code Client} with the
     * provided username.
     */
    public static String whisper(String username, String message) {
        return WHISPER + SEPARATOR + username + SEPARATOR + message;
    }

    /**
     * Constructs a protocol message to inform a {@code Client} that their whisper message could not
     * be delivered.
     * @param username The username of the {@code Client} who could not receive the whisper message
     * @return A protocol message to inform a {@code Client} that their whisper message could not
     * be delivered.
     */
    public static String cannotWhisper(String username) {
        return CANNOTWHISPER + SEPARATOR + username;
    }

    /**
     * Constructs a protocol message to request to join a named queue.
     * @param queue The name of the queue to join
     * @return A protocol message to request to join a named queue
     */
    public static String queue(String queue) {
        return QUEUE + SEPARATOR + queue;
    }
}
