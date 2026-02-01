package game.abstracts;

import game.implementations.GenericPlayer;
import game.interfaces.Game;
import game.interfaces.Piece;
import game.interfaces.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import game.implementations.strategy.RandomStrategy;

/**
 * Abstract test class which creates a template used to test correct functionality of
 * {@code AbstractGame} implementations.
 * @param <T> A generic type that defines the type of {@code Piece}s used to make a {@code Move} in
 *           this {@code AbstractGame}
 *
 * @see AbstractGame
 * @see Game
 * @see game.interfaces.Move
 * @see Piece
 */
public abstract class AbstractGameTest<T extends Enum<T> & Piece> {
    protected final RandomStrategy<T> randomStrategy = new RandomStrategy<>();
    protected final Player<T> player1 = new GenericPlayer<>("p1", randomStrategy);
    protected final Player<T> player2 = new GenericPlayer<>("p2", randomStrategy);
    protected Game<T> game;

    /**
     * Performs setup needed for the unit tests in this class.
     */
    @BeforeEach
    protected abstract void setUpGame();

    /**
     * Tests that the initial state after construction is as expected.
     */
    @Test
    protected abstract void testInitialState();

    /**
     * Tests that randomly playing out the full {@code Game} does not cause any errors and keeps
     * invariants.
     */
    @RepeatedTest(100)
    @Timeout(5)
    protected abstract void testFullGame();

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
     * Tests that playing a {@code Move} that should not end the game advances the {@code Game}
     * state as expected.
     */
    @Test
    protected abstract void doMove_whenNonTerminatingMove_continuesGame();

    /**
     * Tests that playing a {@code Move} that should end the game terminates the {@code Game} as
     * expected.
     */
    @Test
    protected abstract void doMove_whenTerminatingMove_endsGame();

    /**
     * Tests that a valid {@code Move} is recognized as a valid {@code Move}.
     */
    @Test
    protected abstract void returnMoveIfValid_whenValidMove_returnsMove();

    /**
     * Tests that an invalid {@code Move} is recognized as an invalid {@code Move}.
     */
    @Test
    protected abstract void returnMoveIfValid_whenInvalidMove_returnsNull();
}
