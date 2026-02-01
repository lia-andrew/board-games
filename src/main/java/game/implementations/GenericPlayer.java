package game.implementations;

import game.interfaces.Game;
import game.interfaces.Move;
import game.interfaces.Piece;
import game.interfaces.Player;
import game.interfaces.Strategy;

/**
 * Implementation of {@code Player} that provides basic functionality that is expected to be
 * usable across implementations of {@code Game}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Player
 * @see Strategy
 */
public class GenericPlayer<T extends Enum<T> & Piece> implements Player<T> {
    private final String name;
    private final Strategy<T> strategy;

    /**
     * Constructs a new {@code GenericPlayer}, with the provided {@code Strategy} being used to take
     * turns.
     * @param name The name of this new {@code GenericPlayer}
     * @param strategy The {@code Strategy} that this {@code GenericPlayer} will use to take turns
     */
    public GenericPlayer(String name, Strategy<T> strategy) {
        this.name = name;
        this.strategy = strategy;
    }

    @Override
    public <U extends Move<T>, V extends Game<T>> U takeTurn(V game) {
        return strategy.determineMove(game);
    }

    @Override
    public final String getName() {
        return name;
    }
}
