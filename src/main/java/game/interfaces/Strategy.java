package game.interfaces;

//TODO implement factory pattern

/**
 * Interface used to represent a strategy which a {@code Player} can employ to choose a {@code Move}
 * when playing a {@code Game}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Move
 * @see Piece
 * @see Player
 */
public interface Strategy<T extends Enum<T> & Piece> {
    /**
     * Determines a {@code Move} to play according to the state of the provided {@code Game}.
     * @param game The {@code Game} which the {@code Move} is determined for
     * @return The {@code Move} which was chosen
     * @param <U> A generic type that represents the type of {@code Move}s that can be played
     *          with {@code Piece}s of type {@link T}
     * @param <V> A generic type that represents the type of {@code Game}s that can be played
     *          with {@code Piece}s of type {@link T}
     */
    <U extends Move<T>, V extends Game<T>> U determineMove(V game);
}