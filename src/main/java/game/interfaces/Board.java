package game.interfaces;

/**
 * Interface used to represent a {@code Board} for each implemented {@code Game}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Piece
 */
public interface Board<T extends Piece> {
    /**
     * Returns a copy of this {@code Board} with a reference to a different memory location than
     * the original but in the exact same state. Non-primitive members are recursively
     * deep-copied.
     * @return A deep copy of this {@code Board}
     */
    Board<T> deepCopy();

    /**
     * Checks whether the provided index is within the bounds of this{@code Board}.
     * @param index The provided index which will be checked
     * @return {@code True}: the provided index is within this {@code Board}<br>
     *          {@code False}: the provided is not within this {@code Board}
     */
    boolean isValidField(int index);

    /**
     * Replaces the {@code Piece}, found at the provided index, to the provided {@code Piece}.
     * @param index The index of the value to change
     * @param piece The new {@code Piece}, of generic type {@link T}
     */
    void setField(int index, T piece);

    /**
     * Returns the {@code Piece} stored at the provided index.
     * @param index The provided index to retrieve the value from
     * @return The {@code Piece}, of generic type {@link T}, found at the provided index
     */
    T getField(int index);

    /**
     * Returns the number of total positions on this board.
     * This is normally equivalent to {@code BOARD_LENGTH * BOARD_LENGTH} due to most {@code Board}s
     * being square.
     * @return The total number of positions on this board
     */
    int getTotalPositions();
}
