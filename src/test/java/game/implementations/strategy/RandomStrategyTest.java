package game.implementations.strategy;

import game.implementations.GenericPlayer;
import game.implementations.quarto.QuartoGame;
import game.implementations.quarto.QuartoMove;
import game.implementations.quarto.QuartoPiece;
import game.interfaces.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class which tests correct functionality of {@code RandomStrategy}.
 *
 * @see RandomStrategy
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 */
public final class RandomStrategyTest {
    private static final RandomStrategy<QuartoPiece> RANDOM_STRATEGY = new RandomStrategy<>();
    private QuartoGame game;

    /**
     * Performs setup needed for the unit tests in this class.
     */
    @BeforeEach
    void setUpRandomStrategy() {
        Player<QuartoPiece> p1 = new GenericPlayer<>("p1", RANDOM_STRATEGY);
        Player<QuartoPiece> p2 = new GenericPlayer<>("p2", RANDOM_STRATEGY);
        game = new QuartoGame(p1, p2);
    }

    /**
     * Tests that asking for a new {@code Move} before a {@code Game} has ended returns a valid
     * {@code Move}.
     */
    @Test
    @Timeout(5)
    void determineMove_whenQuartoGameNotOver_returnsValidMove() {
        do {
            QuartoMove move = RANDOM_STRATEGY.determineMove(game);
            assertNotNull(move);
            assertNotNull(game.returnMoveIfValid(move.getPiece(), move.getIndex()));
            game.doMove(move);
        } while (!game.isGameOver());
    }

    /**
     * Tests that asking for a new {@code Move} after a {@code Game} has ended returns {@code null}.
     */
    @Test
    void determineMove_whenQuartoGameOver_returnsNull() {
        game.doMove(new QuartoMove(QuartoPiece.LIGHT_SMALL_ROUND_SOLID, 0));
        game.doMove(new QuartoMove(QuartoPiece.QUARTO, 0));

        assertTrue(game.isGameOver());
        assertNull(RANDOM_STRATEGY.determineMove(game));
    }
}
