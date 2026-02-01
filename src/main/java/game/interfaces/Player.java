package game.interfaces;

/**
 * Interface used to represent a {@code Player} who can play a {@code Game}. It is possible that
 * similar {@code Game}s could reuse a {@code Player} implementation since asking a user what move
 * to play often follows a standard procedure.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Move
 * @see Piece
 */
public interface Player<T extends Enum<T> & Piece> {
    /**
     * Asks this {@code Player} to choose a {@code Move} to complete their turn.
     * @param game The {@code Game} in which the returned {@code Move} will be played
     * @return The {@code Move} which this {@code Player} decided to play.
     * @param <U> A generic type that represents the type of {@code Move}s that can be played with
     *          {@code Piece}s of type {@link T}
     * @param <V> A generic type that represents the type of {@code Game}s that can be played with
     *          {@code Piece}s of type {@link T}
     */
    <U extends Move<T>, V extends Game<T>> U takeTurn(V game);

    /**
     * Returns the name of this {@code Player}; set when initialized.
     * @return This {@code Player}'s name
     */
    String getName();
}
