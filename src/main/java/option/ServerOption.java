package option;

import java.lang.reflect.Constructor;
import network.implementations.quarto.QuartoServer;
import network.interfaces.Server;

/**
 * Implementation of {@code Option} which enumerates the choices a user has when selecting a
 * {@code Server} to run.
 *
 * @see network.interfaces.Server
 * @see Option
 */
public enum ServerOption implements Option {
    QUARTO(QuartoServer.class);

    private final Constructor<?> constructor;

    /**
     * Constructs a new {@code ServerOption} with the provided {@code Class} being used to retrieve
     * the related constructor.
     * @param clazz The {@code Class} used to retrieve the related constructor.
     */
    ServerOption(Class<? extends Server> clazz) {
        Constructor<?> temp;
        try {
            temp = clazz.getConstructor(int.class);
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
