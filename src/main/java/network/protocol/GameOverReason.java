package network.protocol;

/**
 * Enumeration of the ways in which a {@code Game} can be terminated; to be used when notifying
 * {@code Client}s that their {@code Game} has ended.
 *
 * @see game.interfaces.Game
 * @see network.interfaces.Client
 */
public enum GameOverReason {
    VICTORY, DRAW, DISCONNECT
}
