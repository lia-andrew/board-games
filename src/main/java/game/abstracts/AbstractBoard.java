package game.abstracts;

import game.interfaces.Board;
import game.interfaces.Piece;
import java.util.Arrays;

/**
 * Abstract class used to provide basic functionality that is expected to be shared between
 * {@code Board} implementations.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Board
 */
public abstract class AbstractBoard<T extends Piece> implements Board<T> {
    protected static final String DELIM = "    ";

    protected final int boardLength;
    protected final int totalPositions;
    protected T[] fields;

    /**
     * Constructs a new {@code AbstractBoard}, with side-length equal to the provided
     * {@code boardLength}.
     * @param boardLength The provided side-length of this {@code AbstractBoard}
     */
    protected AbstractBoard(int boardLength) {
        this.boardLength = boardLength;
        totalPositions = boardLength * boardLength;
    }

    /**
     * Constructs a copy of the provided {@code AbstractBoard}, deep-copying the fields available in
     * this abstract class and requiring extending classes to provide the implementation-specific
     * deep-copying.
     * @param original The original {@code AbstractBoard} which the constructed copy is based off of
     */
    @SuppressWarnings("CopyConstructorMissesField")
    protected AbstractBoard(AbstractBoard<T> original) {
        boardLength = original.boardLength;
        totalPositions = original.totalPositions;
    }

    @Override
    public final void setField(int index, T piece) {
        fields[index] = piece;
    }

    @Override
    public final T getField(int index) {
        return fields[index];
    }

    @Override
    public final int getTotalPositions() {
        return totalPositions;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractBoard<?> that = (AbstractBoard<?>) o;
        return Arrays.deepEquals(fields, that.fields);
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(fields);
    }
}