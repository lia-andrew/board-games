package game.interfaces;

import java.util.EnumSet;
import java.util.List;

/**
 * Interface used to represent a generic {@code Game}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Move
 * @see Piece
 * @see Player
 */
public interface Game<T extends Enum<T> & Piece> {
    /**
     * Returns a copy of this {@code Game} with a reference to a different memory location
     * than the original but in the exact same state. Non-primitive members are recursively
     * deep-copied.
     * @return A deep copy of this {@code Game}
     */
    Game<T> deepCopy();

    /**
     * Plays the provided {@code Move}, causing this {@code Game} to advance forward one
     * turn.
     * @param move The provided {@code Move}
     * @param <U> A generic type that represents the type of {@code Move}s that can be played
     *           on this {@code Game}, with {@code Piece}s of type {@link T}
     */
    <U extends Move<T>> void doMove(U move);

    /**
     * Checks if the provided {@code Piece}, of generic type {@link T}, and index describe a
     * {@code Move} that could legally be played in this current {@code Game}'s state. If legal,
     * a {@code Move}, of generic type {@code U}, is returned, representing the provided arguments.
     * If illegal, {@code null} is returned to signify such.
     * @param piece The provided {@code Piece}, of generic type {@code T}
     * @param index The provided index
     * @return A {@code Move}, if the provided arguments described a valid {@code Move}, else
     *          {@code null}
     * @param <U> A generic type that represents the type of {@code Move}s that can be played
     *          on this {@code Game}, with {@code Piece}s of type {@link T}
     */
    <U extends Move<T>> U returnMoveIfValid(T piece, int index);

    /**
     * Checks if this {@code Game} has reached a terminal state.
     * @return {@code True}: this game has finished <br>
     *          {@code False}: this game has not finished and is still playable
     */
    boolean isGameOver();

    /**
     * Checks and returns all valid, playable {@code Move}s for the current state of this
     * {@code Game}.
     * @return The list of all valid {@code Move}s
     * @param <U> A generic type that represents the type of {@code Move}s that can be played
     *           on this {@code Game}, with {@code Piece}s of type {@link T}
     */
    <U extends Move<T>> List<U> getValidMoves();

    /**
     * Checks and returns all valid {@code Piece}s for the current state.
     * @return The set of all valid {@code Piece}s for the current state of this {@code Game}
     */
    EnumSet<T> getValidPieces();

    /**
     * Returns the {@code Player} whose turn it is now. In other words, this is the {@code Player}
     * who did NOT take the most recent turn.
     * @return The {code Player} whose turn it is now
     */
    Player<T> getActivePlayer();

    /**
     * Returns the number representing the turn that is next to be played. This is the same as 1
     * more than the number of already played turns
     * @return The number of the turn that is to be played
     */
    int getTurnCount();

    /**
     * Returns the {@code Player} who has won this {@code Game}. If this {@code Game} has not yet
     * terminated or ended in a stalemate, {@code null} will be returned instead.
     * @return The {@code Player} who has won this {@code Game}
     */
    Player<T> getWinner();

    /**
     * Returns a human-friendly text representation of this {@code Game}'s playing {@code Board}; to
     * be used when printing to terminal or otherwise being shown to a user.
     * @return The human-friendly text representation of this {@code Game}
     */
    String getBoardDisplay();
}
