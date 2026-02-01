package game.abstracts;

import game.interfaces.Move;
import game.interfaces.Piece;

/**
 * Abstract class used to provide basic functionality that is expected to be shared between
 * {@code Move} implementations.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Move
 * @see Piece
 */
public abstract class AbstractMove<T extends Enum<T> & Piece> implements Move<T> {
    protected final T piece;
    protected final int index;

    /**
     * Constructs a new {@code AbstractMove} based on the provided {@code Piece} and index. Due to
     * this class being abstract, it should not be used by non-extending classes and is thus
     * protected.
     * @param piece The provided {@code Piece}, of generic type {@link T}
     * @param index The provided index
     */
    protected AbstractMove(T piece, int index) {
        this.piece = piece;
        this.index = index;
    }

    @Override
    public final T getPiece() {
        return piece;
    }

    @Override
    public final int getIndex() {
        return index;
    }

    @Override
    public int[] toArray() {
        return new int[]{index, piece.ordinal()};
    }
}
