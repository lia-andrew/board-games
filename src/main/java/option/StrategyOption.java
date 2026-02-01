package option;

import java.lang.reflect.Constructor;
import game.implementations.strategy.HumanStrategy;
import game.implementations.strategy.MCTSStrategy;
import game.implementations.strategy.RandomStrategy;
import game.interfaces.Strategy;

/**
 * Implementation of {@code Option} which enumerates the {@code Strategy} choices a user has when
 * playing a {@code Game}.
 *
 * @see game.interfaces.Game
 * @see Option
 * @see Strategy
 */
public enum StrategyOption implements Option {
    HUMAN(HumanStrategy.class), RANDOM(RandomStrategy.class), MCTS(MCTSStrategy.class);

    private final Constructor<?> constructor;

    /**
     * Constructs a new {@code StrategyOption} with the provided {@code Class} being used to
     * retrieve the related constructor.
     * @param clazz The {@code Class} used to retrieve the related constructor.
     */
    @SuppressWarnings("rawtypes")
    StrategyOption(Class<? extends Strategy> clazz) {
        Constructor<?> temp;
        try {
            temp = clazz.getConstructor();
        } catch (NoSuchMethodException _) {
            temp = null;
        }
        constructor = temp;
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }
}
