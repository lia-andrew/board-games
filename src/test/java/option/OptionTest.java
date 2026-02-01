package option;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import game.implementations.strategy.RandomStrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Test class which tests correct functionality of the {@code Option} interface.
 *
 * @see Option
 */
public final class OptionTest {
    /**
     * Tests that the expected {@code Option} is returned when correct input is used.
     */
    @Test
    void selectOption_whenExpectedValues_returnsEnum() {
        Scanner scanner = new Scanner("RANDOM");
        assertSame(StrategyOption.RANDOM, Option.selectOption(StrategyOption.class, scanner));
    }

    /**
     * Tests that the expected {@code Option} is returned when first incorrect input is used, then
     * correct input is used.
     */
    @Test
    void selectOption_whenInvalidInput_askAgainThenReturnsEnum() {
        Scanner scanner = new Scanner("Lorem Ipsum\nRANDOM");
        assertSame(StrategyOption.RANDOM, Option.selectOption(StrategyOption.class, scanner));
    }

    /**
     * Tests that getting a new instance of an {@code Option} is an instance of the right class
     * and does not error.
     * @throws InvocationTargetException Thrown if making a new instance causes an error
     * @throws InstantiationException Thrown if making a new instance causes an error
     * @throws IllegalAccessException Thrown if making a new instance causes an error
     */
    @Test
    void getConstructor_always_canGetNewInstance()
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        assertEquals(RandomStrategy.class,
                     StrategyOption.RANDOM.getConstructor().newInstance().getClass());
    }
}
