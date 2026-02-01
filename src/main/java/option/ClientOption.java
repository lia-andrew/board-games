package option;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.Scanner;
import network.implementations.quarto.AutoPlayQuartoClient;
import network.implementations.quarto.QuartoClient;
import network.interfaces.Client;

/**
 * Implementation of {@code Option} which enumerates the choices a user has when selecting a
 * {@code Client} to run.
 *
 * @see network.interfaces.Client
 * @see Option
 */
public enum ClientOption implements Option {
    QUARTO(QuartoClient.class), AUTOPLAY_QUARTO(AutoPlayQuartoClient.class);

    private final Constructor<?> constructor;

    /**
     * Constructs a new {@code ClientOption} with the provided {@code Class} being used to retrieve
     * the related constructor.
     * @param clazz The {@code Class} used to retrieve the related constructor.
     */
    ClientOption(Class<? extends Client> clazz) {
        Constructor<?> temp;
        try {
            temp = clazz.getConstructor(InetAddress.class, int.class, Scanner.class);
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
