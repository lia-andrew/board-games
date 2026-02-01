package game.implementations.quarto;

import game.abstracts.AbstractBoardTest;
import game.interfaces.Board;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Implementation of {@code AbstractBoardTest} which tests the correct functionality of
 * {@code QuartoBoard}.
 *
 * @see QuartoBoard
 * @see AbstractBoardTest
 */
public final class QuartoBoardTest extends AbstractBoardTest<QuartoPiece> {
    @Override
    @BeforeEach
    protected void setUpBoard() {
        boardLength = 4;
        board = new QuartoBoard(boardLength);
    }

    @Override
    @Test
    protected void testInitialState() {
        assertTrue(IntStream.range(0, board.getTotalPositions())
                           .allMatch(i -> QuartoPiece.EMPTY == board.getField(i)));
    }

    @Override
    @Test
    protected void deepCopy_whenNotChanged_isEqual() {
        Board<QuartoPiece> copy = board.deepCopy();
        assertEquals(board, copy);
        assertNotSame(board, copy);

        Set<Board<QuartoPiece>> boardSet = new HashSet<>();
        boardSet.add(board);
        boardSet.add(copy);
        assertEquals(1, boardSet.size());
    }

    @Override
    @Test
    protected void deepCopy_whenChanged_isNotEqual() {
        Board<QuartoPiece> copy = board.deepCopy();
        copy.setField(0, QuartoPiece.LIGHT_SMALL_ROUND_SOLID);
        assertNotEquals(board, copy);

        Set<Board<QuartoPiece>> boardSet = new HashSet<>();
        boardSet.add(board);
        boardSet.add(copy);
        assertEquals(2, boardSet.size());
    }

    @Override
    @Test
    protected void isValidField_whenInsideBoard_isTrue() {
        assertTrue(board.isValidField(0));
        assertTrue(board.isValidField(boardLength * boardLength - 1));
    }

    @Override
    @Test
    protected void isValidField_whenOutsideBoard_isFalse() {
        assertFalse(board.isValidField(Integer.MIN_VALUE));
        assertFalse(board.isValidField(-1));
        assertFalse(board.isValidField(boardLength * boardLength));
        assertFalse(board.isValidField(Integer.MAX_VALUE));
    }
}
