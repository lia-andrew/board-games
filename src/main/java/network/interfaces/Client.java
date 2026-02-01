package network.interfaces;

import java.util.EnumSet;
import network.protocol.ExtendedProtocol;

/**
 * Interface used to represent a client which has the responsibility of supporting the user by
 * displaying relevant updates from the {@code Sever}.
 *
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 * @see game.interfaces.Player
 * @see Server
 */
public interface Client {
    /**
     * Handles the final step of the hello-handshake by parsing the provided {@code EnumSet} to
     * determine the protocol extensions that both the {@code Client} and {@code Server} support.
     * @param serverExtensions The protocol extensions that the {@code Server} supports
     */
    void handleHello(EnumSet<ExtendedProtocol> serverExtensions);

    /**
     * Handles state-change due to a successful login and informs the user of such.
     */
    void handleAcceptedLogin();

    /**
     * Informs the user of an unsuccessful login.
     */
    void handleRejectedLogin();

    /**
     * Handles the creation of a new {@code Game} and informs the user of such.
     * @param myUsername The username the user successfully logged in with
     * @param username1 The username of the {@code Player} with the first turn
     * @param username2 The username of the {@code Player} with the second turn
     */
    void handleNewGame(String myUsername, String username1, String username2);

    /**
     * Handles the advancement of the current {@code Game} by playing the {@code Move} represented
     * by the provided array of values.
     * @param values The array-representation of the {@code Move} which will be played
     */
    void handleMove(int[] values);

    /**
     * Handles the termination of the current {@code Game} and informs the user of such.
     */
    void handleGameOver();

    /**
     * Handles a disconnect from the {@code Sever} and informs the user of such.
     */
    void handleDisconnect();

    /**
     * Prints the output to the implementation-specific stdout with a terminating newline.
     * @param output The output to be printed
     */
    void printlnOut(String output);

    /**
     * Prints the output to the implementation-specific stdout without a terminating newline.
     * @param output The output to be printed
     */
    void printOut(String output);

    /**
     * Prints the output to the implementation-specific stderr with a terminating newline.
     * @param output The output to be printed
     */
    void printErr(String output);
}
