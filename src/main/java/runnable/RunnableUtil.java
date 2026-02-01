package runnable;

import exceptions.IllegalInitializationException;
import java.net.InetAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Utility class which provides methods to ask for user input; to be used by program entry points.
 *
 * @see exceptions.IllegalInitializationException
 */
public final class RunnableUtil {
    /**
     * An intentionally private constructor used to signify that this class should never be
     * initialized from other files. If the reflection API is used to access this constructor, an
     * {@code IllegalInitializationException} will be thrown to ensure the inability to initialize.
     */
    private RunnableUtil() {
        throw new IllegalInitializationException();
    }

    /**
     * Asks the user what is the desired port. Performs error-checking to ensure the input could be
     * a valid port number.
     * @param scanner The {@code Scanner} which is to be used to retrieve user input
     * @return The port number that the user chose
     */
    public static int askPort(Scanner scanner) {
        int port;
        do {
            System.out.print("Input the port number to connect to: ");
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (port < 0 || port > 65535) {
                    throw new IllegalArgumentException();
                }
                return port;
            } catch (IllegalArgumentException | NoSuchElementException _) {
                System.out.println("Please input a valid port number");
            }
        } while (true);
    }

    /**
     * Asks the user what is the desired IP address. Performs error-checking to ensure the input
     * could be a valid IP address.
     * @param scanner The {@code Scanner} which is to be used to retrieve user input
     * @return The IP address that the user chose
     */
    public static InetAddress askIpAddress(Scanner scanner) {
        do {
            System.out.print("Input the IP address to connect to (leave blank for localhost): ");
            try {
                return InetAddress.ofLiteral(scanner.nextLine());
            } catch (StringIndexOutOfBoundsException _) {
                return InetAddress.getLoopbackAddress();
            } catch (IllegalArgumentException _) {
                System.out.println("Please input an IP address in a valid format (e.g. 127.0.0.1)");
            }
        } while (true);
    }
}
