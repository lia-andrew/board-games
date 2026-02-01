package runnable;

import exceptions.IllegalInitializationException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import option.ClientOption;
import option.Option;

/**
 * Program entry point used to run a {@code Client}.
 *
 * @see exceptions.IllegalInitializationException
 * @see network.interfaces.Client
 * @see network.interfaces.Server
 */
public final class RunClient {
    /**
     * An intentionally private constructor used to signify that this class should never be
     * initialized from other files. If the reflection API is used to access this constructor, an
     * {@code IllegalInitializationException} will be thrown to ensure the inability to initialize.
     */
    private RunClient() {
        throw new IllegalInitializationException();
    }

    /**
     * Program entry point that asks a user which {@code Client} to run and what {@code Server} to
     * connect to.
     * @param args The command-line options passed in when the program is run; currently unused
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                Option.selectOption(ClientOption.class, scanner).getConstructor()
                        .newInstance(RunnableUtil.askIpAddress(scanner),
                                     RunnableUtil.askPort(scanner), scanner);
            } catch (InvocationTargetException _) {
                System.out.println("That IP and port combination was invalid, please try again");
                continue;
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
            break;
        } while (true);
    }
}
