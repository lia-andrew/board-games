package runnable;

import exceptions.IllegalInitializationException;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import option.Option;
import option.ServerOption;

/**
 * Program entry point used to run a {@code Server}.
 *
 * @see exceptions.IllegalInitializationException
 * @see network.interfaces.Server
 */
public final class RunServer {
    /**
     * An intentionally private constructor used to signify that this class should never be
     * initialized from other files. If the reflection API is used to access this constructor, an
     * {@code IllegalInitializationException} will be thrown to ensure the inability to initialize.
     */
    private RunServer() {
        throw new IllegalInitializationException();
    }

    /**
     * Program entry point that asks a user which {@code Server} to run and on what port to do so.
     * @param args The command-line options passed in when the program is run; currently unused
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                Option.selectOption(ServerOption.class, scanner).getConstructor()
                        .newInstance(RunnableUtil.askPort(scanner));
            } catch (InvocationTargetException _) {
                System.out.println("That port is currently in use, please try again");
                continue;
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
            break;
        } while (true);
        scanner.close();
    }
}
