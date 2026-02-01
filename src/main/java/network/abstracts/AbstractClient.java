package network.abstracts;

import game.interfaces.Game;
import game.interfaces.Piece;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.stream.Collectors;
import network.protocol.ExtendedProtocol;
import network.protocol.ExtendedProtocolUtil;
import network.protocol.ProtocolUtil;
import network.implementations.ServerHandler;
import network.interfaces.Client;
import option.Option;
import option.StrategyOption;
import game.interfaces.Strategy;

/**
 * Abstract class used to provide basic functionality that is expected to be shared between
 * {@code Client} implementations.
 * @param <T> A generic type that unifies the generic classes in the {@link network} package
 *
 * @see Game
 * @see game.interfaces.Move
 * @see Strategy
 * @see Client
 * @see network.interfaces.Server
 */
public abstract class AbstractClient<T extends Enum<T> & Piece> implements Client {
    private static final EnumSet<ExtendedProtocol> CLIENT_EXTENSIONS =
            EnumSet.of(ExtendedProtocol.NAMEDQUEUES, ExtendedProtocol.RANK, ExtendedProtocol.CHAT);

    protected final Scanner scanner;
    protected ServerHandler serverHandler;
    protected Strategy<T> strategy;
    protected Game<T> game;
    protected int[] mistimedMove;
    private String extraCommands;

    /**
     * Constructs a new {@code AbstractClient} that attempts to connect to the {@code Sever} on the
     * provided IP address and port number.
     * @param address The IP address to connect to
     * @param port The port number to connect to
     * @param scanner The scanner instance to use for this {@code AbstractClient}
     * @throws IOException Thrown if there is an issue connecting to the {@code Server}
     */
    protected AbstractClient(InetAddress address, int port, Scanner scanner) throws IOException {
        serverHandler = new ServerHandler(address, port, this, CLIENT_EXTENSIONS);
        this.scanner = scanner;
    }

    @Override
    public void handleHello(EnumSet<ExtendedProtocol> serverExtensions) {
        printlnOut("Successfully connected to server");
        CLIENT_EXTENSIONS.retainAll(serverExtensions);
        extraCommands = CLIENT_EXTENSIONS.stream().map(ExtendedProtocol::getDescription)
                .collect(Collectors.joining(", "));
        showCommands();
        new Thread(this::askForInput).start();
    }

    /**
     * Displays the recognized commands that the user can input.
     */
    private void showCommands() {
        printlnOut("Enter: LOGIN, LIST, QUEUE, MOVE, EXIT [Extras: " + extraCommands + "]");
    }

    /**
     * Asks the user to input a command in an infinite loop, unless they choose to exit.
     */
    private void askForInput() {
        inputLoop:
        while (true) {
            switch (scanner.nextLine().toUpperCase()) {
                case ProtocolUtil.LOGIN -> askLogin();
                case ProtocolUtil.LIST -> serverHandler.sendListRequest();
                case ProtocolUtil.QUEUE -> askQueue();
                case ProtocolUtil.MOVE -> askMove();
                case ExtendedProtocolUtil.RANK -> askRank();
                case ExtendedProtocolUtil.CHAT -> askChat();
                case ExtendedProtocolUtil.WHISPER -> askWhisper();
                case "EXIT" -> {
                    break inputLoop;
                }
                default -> {
                    printErr("You input an unrecognized command");
                    printErr("Please review the following list and try again");
                    showCommands();
                }
            }
        }
        handleDisconnect();
    }

    /**
     * Asks the user to input a username and then attempts to log in with that username.
     */
    protected void askLogin() {
        printOut("Input your desired username: ");
        serverHandler.sendLoginRequest(scanner.nextLine());
    }

    /**
     * Attempts to join a queue; the specific queue to join is asked if NAMEDQUEUES is a supported
     * extension.
     */
    private void askQueue() {
        askStrategy();
        if (CLIENT_EXTENSIONS.contains(ExtendedProtocol.NAMEDQUEUES)) {
            printOut("Enter the name of the queue to join: ");
            serverHandler.sendQueueRequest(scanner.nextLine());
        } else {
            serverHandler.sendQueueRequest();
        }
    }

    /**
     * Asks the user which {@code Strategy} to employ in their next {@code Game}(s).
     */
    @SuppressWarnings("unchecked")
    protected void askStrategy() {
        try {
            strategy = (Strategy<T>) Option.selectOption(StrategyOption.class, scanner)
                    .getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Unexpected error: " + e.getMessage());
            handleDisconnect();
        }
    }

    /**
     * Determines the next {@code Move} to play through the use of the chosen {@code Strategy}, and
     * then attempts to play that {@code Move}.
     */
    protected void askMove() {
        if (game != null && game.getActivePlayer().getName().equals(serverHandler.getUsername())) {
            serverHandler.sendMoveRequest(game.getActivePlayer().takeTurn(game).toArray());
        } else {
            printlnOut("You cannot make a move yet, please wait");
        }
    }

    /**
     * Requests the RANK information.
     */
    private void askRank() {
        if (CLIENT_EXTENSIONS.contains(ExtendedProtocol.RANK)) {
            serverHandler.sendRankRequest();
        } else {
            printErr("Either the server or the client does not support RANK");
        }
    }

    /**
     * Asks the user to input a global message to send and then attempts to send that message.
     */
    private void askChat() {
        if (CLIENT_EXTENSIONS.contains(ExtendedProtocol.CHAT)) {
            printOut("Input the message to send: ");
            serverHandler.sendChatRequest(scanner.nextLine());
        } else {
            printErr("Either the server or the client does not support CHAT");
        }
    }

    /**
     * Asks the user a private message to send, and who to send it to, then attempts to send that
     * message.
     */
    private void askWhisper() {
        if (CLIENT_EXTENSIONS.contains(ExtendedProtocol.CHAT)) {
            printOut("Input the message to send: ");
            String message = scanner.nextLine();
            printOut("Input the name of the receiver: ");
            serverHandler.sendWhisperRequest(scanner.nextLine(), message);
        } else {
            printErr("Either the server or the client does not support WHISPER");
        }
    }

    @Override
    public void handleAcceptedLogin() {
        printlnOut("Successfully logged in");
    }

    @Override
    public void handleRejectedLogin() {
        printlnOut("That username was already taken, please input another");
    }

    @Override
    public void handleGameOver() {
        game = null;
    }

    @Override
    public void handleDisconnect() {
        printlnOut("You have been disconnected. Goodbye!");
        System.exit(0);
    }

    @Override
    public void printlnOut(String output) {
        System.out.println(output);
    }

    @Override
    public void printOut(String output) {
        System.out.print(output);
    }

    @Override
    public void printErr(String output) {
        System.err.println(output);
    }
}
