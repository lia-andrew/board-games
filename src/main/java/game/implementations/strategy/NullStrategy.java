package game.implementations.strategy;

import game.interfaces.Game;
import game.interfaces.Move;
import game.interfaces.Piece;
import game.interfaces.Strategy;

/**
 * Implementation of {@code Strategy} which intentionally returns {@code null}. This implementation
 * should be used as a model for {@code Player}s which should not be able to make a {@code Move}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Move
 * @see game.interfaces.Player
 * @see Strategy
 */
public final class NullStrategy<T extends Enum<T> & Piece> implements Strategy<T> {
    @Override
    public <U extends Move<T>, V extends Game<T>> U determineMove(V game) {
        return null;
    }
}
