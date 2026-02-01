package game.interfaces;

//TODO implement toArray and fromArray in each implementation for better separation of concerns

/**
 * Interface used to represent a {@code Move} for each implemented {@code Game}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Piece
 */
public interface Move<T extends Piece> {
    /**
     * Returns an int-array representation of this {@code Move}, taking the {@code Piece}'s
     * ordinal value.
     * This method exists primarily for forward-compatability, in case a future implementation
     * requires more than two values to represent all relevant information of a {@code Move}.
     * @return The int-array representation of this {@code Move}
     */
    int[] toArray();

    /**
     * Returns the {@code Piece} of this {@code Move}, of generic type {@link T}; set when
     * initialized.
     * @return This {@code Move}'s piece, of generic type {@code T}
     */
    T getPiece();

    /**
     * Returns the index of this {@code Move}; set when initialized.
     * @return This {@code Move}'s index
     */
    int getIndex();
}
