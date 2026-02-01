package network.implementations.quarto;

import java.io.IOException;
import java.net.InetAddress;
import java.util.EnumSet;
import java.util.Scanner;
import network.protocol.ExtendedProtocol;

/**
 * Extension of {@code QuartoClient} which intends to let a user set up their choices only once and
 * then automatically queue and play {@code QuartoGame}s on repeat.
 *
 * @see game.implementations.quarto.QuartoGame
 * @see QuartoClient
 * @see network.interfaces.Server
 */
public final class AutoPlayQuartoClient extends QuartoClient {
    private String queueName;

    /**
     * Constructs a new {@code AutoPlayQuartoClient} that attempts to connect to the {@code Sever}
     * on the provided IP address and port number.
     * @param address The IP address to connect to
     * @param port The port number to connect to
     * @param scanner The scanner instance to use for this {@code AutoPlayQuartoClient}
     * @throws IOException Thrown if there is an issue connecting to the {@code Server}
     */
    public AutoPlayQuartoClient(InetAddress address, int port, Scanner scanner) throws IOException {
        super(address, port, scanner);
    }

    @Override
    public void handleHello(EnumSet<ExtendedProtocol> ignored) {
        printlnOut("Successfully connected to server");
        askLogin();
    }

    @Override
    public void handleAcceptedLogin() {
        super.handleAcceptedLogin();
        askStrategy();
        printOut("Enter the name of the queue to repeatedly join: ");
        queueName = scanner.nextLine();
        serverHandler.sendQueueRequest(queueName);
        printlnOut("All set up!");
    }

    @Override
    public void handleRejectedLogin() {
        super.handleRejectedLogin();
        askLogin();
    }

    @Override
    public void handleNewGame(String myUsername, String username1, String username2) {
        super.handleNewGame(myUsername, username1, username2);
        askMove();
    }

    @Override
    public void handleMove(int[] values) {
        super.handleMove(values);
        askMove();
    }

    @Override
    public void handleGameOver() {
        super.handleGameOver();
        serverHandler.sendQueueRequest(queueName);
    }
}
