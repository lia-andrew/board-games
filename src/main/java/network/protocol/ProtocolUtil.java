package network.protocol;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Utility class which provides constants and methods to facilitate communication between a
 * {@code Server} and its {@code Client}s.
 *
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 * @see game.interfaces.Player
 * @see network.interfaces.Client
 * @see network.interfaces.Server
 */
public abstract class ProtocolUtil {
    //shared
    public static final String SEPARATOR = "~";
    public static final String HELLO = "HELLO";
    public static final String LOGIN = "LOGIN";
    public static final String LIST = "LIST";
    public static final String MOVE = "MOVE";
    public static final String ERROR = "ERROR";

    //server
    public static final String ALREADY_LOGGED_IN = "ALREADYLOGGEDIN";
    public static final String NEW_GAME = "NEWGAME";
    public static final String GAME_OVER = "GAMEOVER";

    //client
    public static final String QUEUE = "QUEUE";

    /**
     * Constructs a protocol message that is part of the initial setup handshake.
     * @param description A greeting and/or informative description of the sender
     * @param extensions The protocol extensions that are supported by the sender
     * @return A protocol message that is part of the initial setup handshake
     */
    public static String hello(String description, EnumSet<ExtendedProtocol> extensions) {
        return HELLO + SEPARATOR + description + SEPARATOR +
                extensions.stream().map(Enum::toString).collect(Collectors.joining(SEPARATOR));
    }

    /**
     * Splits a protocol message into separate, more easily usable, parts.
     * @param input The protocol message to split
     * @return A protocol message split separate, more easily usable, parts
     */
    public static String[] splitInput(String input) {
        return input.split(SEPARATOR);
    }

    /**
     * Parses a protocol message that is part of the initial setup handshake to determine what
     * protocol extensions the sender supports.
     * @param helloMessage The protocol message that is part of the initial setup handshake
     * @return The protocol extensions that the sender supports
     */
    public static EnumSet<ExtendedProtocol> parseHello(String[] helloMessage) {
        return Arrays.stream(helloMessage).skip(2).map(str -> {
                    try {
                        return ExtendedProtocol.valueOf(str);
                    } catch (IllegalArgumentException _) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ExtendedProtocol.class)));
    }

    /**
     * Constructs a protocol message to request to log into the {@code Server} with the provided
     * username.
     * @param username The username to log in with
     * @return A protocol message to request to log into the {@code Server} with the provided
     *          username
     */
    public static String login(String username) {
        return LOGIN + SEPARATOR + username;
    }

    /**
     * Constructs a protocol message to notify a {@code Client} of a successful login.
     * @return A protocol message to notify of a {@code Client} successful login
     */
    public static String acceptLogin() {
        return LOGIN;
    }

    /**
     * Constructs a protocol message to notify a {@code Client }of an unsuccessful login.
     * @return A protocol message to notify a {@code Client} of an unsuccessful login
     */
    public static String rejectLogin() {
        return ALREADY_LOGGED_IN;
    }

    /**
     * Constructs a protocol message to request a list of active {@code Client}s on the
     * {@code Server}.
     * @return A protocol message to request a list of active {@code Client}s on the {@code Server}
     */
    public static String list() {
        return LIST;
    }

    /**
     * Constructs a protocol message to send a list of active {@code Client}s on the {@code Server}.
     * @param usernames A list of active {@code Client}s on the {@code Server}
     * @return A protocol message to send a list of active {@code Client}s on the {@code Server}
     */
    public static String list(List<String> usernames) {
        return LIST + SEPARATOR + String.join(SEPARATOR, usernames);
    }

    /**
     * Constructs a protocol message to request to join an unnamed queue.
     * @return A protocol message to request to join an unnamed queue
     */
    public static String queue() {
        return QUEUE;
    }

    /**
     * Constructs a protocol message to inform {@code Client}s of a new {@code Game} that has
     * commenced.
     * @param player1 The name of the {@code Player} with the first turn in the new {@code Game}
     * @param player2 The name of the {@code Player} with the second turn in the new {@code Game}
     * @return A protocol message to inform of a new {@code Game} that has commenced
     */
    public static String newGame(String player1, String player2) {
        return NEW_GAME + SEPARATOR + player1 + SEPARATOR + player2;
    }

    /**
     * Constructs a protocol message to send a {@code Move} request.
     * @param values The array representation of the {@code Move} to play
     * @return A protocol message to send a {@code Move} request
     */
    public static String move(int[] values) {
        return MOVE + SEPARATOR + Arrays.stream(values).mapToObj(String::valueOf)
                .collect(Collectors.joining(SEPARATOR));
    }

    /**
     * Constructs a protocol message to inform {@code Client}s that their {@code Game} has ended in
     * a draw.
     * @return A protocol message to inform {@code Client}s that their {@code Game} has ended in a
     *          draw
     */
    public static String gameOver() {
        return GAME_OVER + SEPARATOR + GameOverReason.DRAW;
    }

    /**
     * Constructs a protocol message to inform {@code Client}s that their {@code Game} has ended in
     * with the provided reason.
     * @param reason The reason why the {@code Game} ended
     * @param winner The name of the {@code Player} who won
     * @return A protocol message to inform {@code Client}s that their {@code Game} has ended in
     *          with the provided reason
     */
    public static String gameOver(GameOverReason reason, String winner) {
        return GAME_OVER + SEPARATOR + reason + SEPARATOR + winner;
    }

    /**
     * Constructs a protocol message to inform a {@code Client} that their request could not be
     * processed for the provided reason.
     * @param description The reason why the request could not be processed
     * @return A protocol message to inform a {@code Client} that their request could not be
     *          processed for the provided reason
     */
    public static String error(String description) {
        return ERROR + SEPARATOR + description;
    }
}
