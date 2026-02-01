package game.implementations.strategy;

import game.interfaces.Game;
import game.interfaces.Move;
import game.interfaces.Piece;
import game.interfaces.Strategy;
import java.util.List;

/**
 * Implementation of {@code Strategy} which selects a random valid {@code Move}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Move
 * @see Strategy
 */
public final class RandomStrategy<T extends Enum<T> & Piece> implements Strategy<T> {
    @Override
    public <U extends Move<T>, V extends Game<T>> U determineMove(V game) {
        List<U> validMoves = game.getValidMoves();
        if (validMoves.isEmpty()) {
            return null;
        }
        return validMoves.get((int) (Math.random() * validMoves.size()));
    }
}
