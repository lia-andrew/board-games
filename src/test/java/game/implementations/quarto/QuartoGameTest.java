package game.implementations.quarto;

import game.abstracts.AbstractGameTest;
import game.interfaces.Game;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Implementation of {@code AbstractGameTest} which tests the correct functionality of
 * {@code QuartoGame}.
 *
 * @see QuartoGame
 * @see AbstractGameTest
 */
public final class QuartoGameTest extends AbstractGameTest<QuartoPiece> {
    @Override
    @BeforeEach
    protected void setUpGame() {
        game = new QuartoGame(player1, player2);
    }

    @Override
    @Test
    protected void testInitialState() {
        assertEquals(1, game.getTurnCount());
        assertSame(player1, game.getActivePlayer());
        assertEquals(16 * 16, game.getValidMoves().size()); //16 positions * 16 pieces
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
    }

    @Override
    @RepeatedTest(100)
    @Timeout(5)
    protected void testFullGame() {
        do {
            game.doMove(game.getActivePlayer().takeTurn(game));
        } while (!game.isGameOver());

        assertTrue(game.getTurnCount() > 2 && game.getTurnCount() < 19);
        assertTrue(game.getWinner() != null || game.getTurnCount() == 18);
        assertFalse(game.getValidPieces().contains(QuartoPiece.QUARTO));
    }

    @Override
    @Test
    protected void deepCopy_whenNotChanged_isEqual() {
        Game<QuartoPiece> copy = game.deepCopy();
        assertEquals(game, copy);
        assertNotSame(game, copy);

        Set<Game<QuartoPiece>> gameSet = new HashSet<>();
        gameSet.add(game);
        gameSet.add(copy);
        assertEquals(1, gameSet.size());
    }

    @Override
    @Test
    protected void deepCopy_whenChanged_isNotEqual() {
        Game<QuartoPiece> copy = game.deepCopy();
        copy.doMove(new QuartoMove(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        assertNotEquals(game, copy);

        Set<Game<QuartoPiece>> gameSet = new HashSet<>();
        gameSet.add(game);
        gameSet.add(copy);
        assertEquals(2, gameSet.size());
    }

    @Override
    @Test
    protected void doMove_whenNonTerminatingMove_continuesGame() {
        game.doMove(new QuartoMove(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        assertEquals(2, game.getTurnCount());
        assertSame(player2, game.getActivePlayer());
        assertEquals(256, game.getValidMoves().size()); // 16 positions * (15 pieces + QUARTO)
        assertFalse(game.isGameOver());
        assertNull(game.getWinner());
    }

    @Override
    @Test
    protected void doMove_whenTerminatingMove_endsGame() {
        game.doMove(new QuartoMove(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        game.doMove(new QuartoMove(QuartoPiece.QUARTO, 0));
        assertEquals(3, game.getTurnCount());
        assertSame(player1, game.getActivePlayer());
        assertTrue(game.getValidMoves().isEmpty());
        assertTrue(game.isGameOver());
        assertSame(player1, game.getWinner());
    }

    @Override
    @Test
    protected void returnMoveIfValid_whenValidMove_returnsMove() {
        assertNotNull(game.returnMoveIfValid(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        assertNotNull(game.returnMoveIfValid(QuartoPiece.DARK_SMALL_ROUND_SOLID, 0));
        assertNotNull(game.returnMoveIfValid(QuartoPiece.LIGHT_LARGE_ROUND_SOLID, 1));
    }

    @Override
    @Test
    protected void returnMoveIfValid_whenInvalidMove_returnsNull() {
        assertNull(game.returnMoveIfValid(QuartoPiece.QUARTO, 0));
        assertNull(game.returnMoveIfValid(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, -1));

        game.doMove(new QuartoMove(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        game.doMove(new QuartoMove(QuartoPiece.DARK_SMALL_ROUND_SOLID, 0));
        assertNull(game.returnMoveIfValid(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 1));
        assertNull(game.returnMoveIfValid(QuartoPiece.LIGHT_LARGE_ROUND_SOLID, 0));

        game.doMove(new QuartoMove(QuartoPiece.QUARTO, 1));
        assertNull(game.returnMoveIfValid(QuartoPiece.DARK_LARGE_ROUND_SOLID, 2));
    }
}
