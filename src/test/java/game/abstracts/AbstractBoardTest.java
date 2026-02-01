package game.abstracts;

import game.interfaces.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Abstract test class which creates a template used to test correct functionality of
 * {@code AbstractBoard} implementations.
 * @param <T> A generic type that defines the type of {@code Piece}s placed on this
 *          {@code AbstractBoard}
 *
 * @see AbstractBoard
 * @see Piece
 */
public abstract class AbstractBoardTest<T extends Enum<T> & Piece> {
    protected int boardLength;
    protected AbstractBoard<T> board;

    /**
     * Performs setup needed for the unit tests in this class.
     */
    @BeforeEach
    protected abstract void setUpBoard();

    /**
     * Tests that the initial state after construction is as expected.
     */
    @Test
    protected abstract void testInitialState();

    /**
     * Tests that a deep copy is equal to the original when neither have been changed.
     */
    @Test
    protected abstract void deepCopy_whenNotChanged_isEqual();

    /**
     * Tests that a deep copy is not equal to the original when one has been changed.
     */
    @Test
    protected abstract void deepCopy_whenChanged_isNotEqual();

    /**
     * Tests that indices inside the range of {@code boardLength} are valid.
     */
    @Test
    protected abstract void isValidField_whenInsideBoard_isTrue();

    /**
     * Tests that indices outside the range of {@code boardLength} are invalid.
     */
    @Test
    protected abstract void isValidField_whenOutsideBoard_isFalse();
}
