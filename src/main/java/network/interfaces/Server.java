package network.interfaces;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import network.protocol.ExtendedProtocol;
import network.implementations.ClientHandler;

/**
 * Interface to represent a server which has the responsibility of managing multiple {@code Client}s
 * and allowing them to play {@code Game}s and/or communicate with each other.
 *
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 * @see ClientHandler
 * @see Client
 */
public interface Server {
    /**
     * Handles a {@code Client} requesting to join a queue. If there is another {@code Client}
     * waiting for a match, then a new {@code Game} will be started between them.
     * @param handler The {@code ClientHandler} representing the {@code Client}
     * @param queue The name of the queue the {@code Client} requested to join
     */
    void handleJoinQueue(ClientHandler handler, String queue);

    /**
     * Handles a {@code Client} requesting to leave a queue.
     * @param handler The {@code ClientHandler} representing the {@code Client}
     * @param queue The name of the queue the {@code Client} requested to leave
     */
    void handleLeaveQueue(ClientHandler handler, String queue);

    /**
     * Handles a {@code Move} played by a {@code Client} to advance their {@code Game}. If this
     * causes the {@code Game} to reach a terminal state, the participating {@code Client}s will be
     * notified.
     * @param handler The {@code ClientHandler} representing the {@code Client} who played the
     *          {@code Move}
     * @param values The array-representation of the {@code Move} to be played
     */
    void handleMove(ClientHandler handler, int[] values);

    /**
     * Handles a chat message being sent by a {@code Client} to all other {@code Client}s.
     * @param handler The {@code ClientHandler} representing the {@code Client} who sent the message
     * @param message The chat message to be sent
     */
    void handleChat(ClientHandler handler, String message);

    /**
     * Handles a whisper message being sent by a {@code Client} to a specific {@code Client} that
     * has the same username as the provided one.
     * @param handler The {@code ClientHandler} representing the {@code Client} who sent the message
     * @param username The username of the {@code Client} who the message should be sent to
     * @param message The whisper message to be sent
     */
    void handleWhisper(ClientHandler handler, String username, String message);

    /**
     * Handles a {@code Client} disconnecting and ensures that they are no longer part of a
     * {@code Game} nor in a queue.
     * @param handler The {@code ClientHandler} representing the {@code Client} who disconnected
     * @param queue The last queue that the {@code Client} joined
     */
    void handleDisconnect(ClientHandler handler, String queue);

    /**
     * Checks if any {@code Client} is currently logged in with the provided username.
     * @param username The username being checked
     * @return {@code True}: a {@code Client} is logged in with the provided username <br>
     *          {@code False}: no {@code Client} is logged in with the provided username
     */
    boolean isNameTaken(String username);

    /**
     * Checks if a specific {@code Client} is currently playing a {@code Game}.
     * @param handler The {@code ClientHandler} representing the {@code Client} who is to be checked
     * @return {@code True}: the {@code Client} is currently playing a {@code Game} <br>
     *          {@code False}: the {@code Client} is not currently playing a {@code Game}
     */
    boolean isInGame(ClientHandler handler);

    /**
     * Returns this {@code Server}'s hello message.
     * @return This {@code Server}'s hello message
     */
    String getHello();

    /**
     * Returns this {@code Server}'s implemented protocol extensions.
     * @return This {@code Server}'s implemented protocol extensions
     */
    EnumSet<ExtendedProtocol> getServerExtensions();

    /**
     * Returns this {@code Server}'s default username; used to name a {@code Client} who has not yet
     * logged in.
     * @return This {@code Server}'s default username
     */
    String getDefaultUsername();

    /**
     * Returns the usernames of all {@code Client}s currently connected to this {@code Server}.
     * @return The usernames of all {@code Client}s currently connected to this {@code Server}
     */
    List<String> getUsernames();

    /**
     * Returns the wins associated with each username that has been used to log in. Note that this
     * includes {@code Client}s who are not currently logged in.
     * @return The wins associated with each username that has been used to log in
     */
    Set<Map.Entry<String, Integer>> getRankInfo();

    /**
     * Returns this {@code Server}'s default queue name; used to queue up {@code Client}s who have
     * not asked for a specific queue.
     * @return This {@code Server}'s default queue name
     */
    String getDefaultQueue();

    /**
     * Prints the debug message to the implementation-specific stdout.
     * @param message The output to be printed
     */
    void printDebug(String message);
}
