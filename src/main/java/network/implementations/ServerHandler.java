package network.implementations;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import network.Connection;
import network.interfaces.Client;
import network.interfaces.Handler;
import network.protocol.ExtendedProtocol;
import network.protocol.ExtendedProtocolUtil;
import network.protocol.GameOverReason;
import network.protocol.ProtocolUtil;

/**
 * Implementation of {@code Handler} which handles most of the {@code Sever} logic on the
 * {@code Client} side.
 *
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 * @see Client
 * @see Handler
 * @see network.interfaces.Server
 * @see Connection
 */
public final class ServerHandler implements Handler {
    private static final String HELLO = "Hello from client";

    private final Connection connection;
    private final Client client;
    private String username;

    /**
     * Constructs a new {@code ServerHandler} which attempts to establish a {@code Connection} with
     * a {@code Sever} via the provided IP address and port number. If successful, the hello
     * handshake is commenced, sending the protocol extensions that are provided.
     * @param address The IP address to attempt to connect to
     * @param port The port number to attempt to connect to
     * @param client The {@code Client} for which this {@code ServerHandler} is handling the
     *          {@code Server}-specific communication and logic
     * @param clientExtensions The protocol extensions that the {@code Client} supports
     * @throws IOException Thrown if there is an issue making a {@code Connection} to communicate
     *          with the {@code Server}
     */
    public ServerHandler(InetAddress address, int port, Client client,
                         EnumSet<ExtendedProtocol> clientExtensions) throws IOException {
        connection = new Connection(address, port, this);
        this.client = client;
        sendHello(clientExtensions);
    }

    /**
     * Commences the hello handshake with the connected {@code Server}.
     * @param clientExtensions The extensions to inform the {@code Server} are supported on the
     *          {@code Client}-side
     */
    private void sendHello(EnumSet<ExtendedProtocol> clientExtensions) {
        connection.writeOutput(ProtocolUtil.hello(HELLO, clientExtensions));
    }

    @Override
    public void readInput(String input) {
        String[] splitInput = ProtocolUtil.splitInput(input);
        switch (splitInput[0]) {
            case ProtocolUtil.HELLO -> handleHello(splitInput);
            case ProtocolUtil.LOGIN -> handleAcceptedLogin();
            case ProtocolUtil.ALREADY_LOGGED_IN -> handleRejectedLogin();
            case ProtocolUtil.LIST -> handleList(splitInput);
            case ProtocolUtil.NEW_GAME -> handleNewGame(splitInput);
            case ProtocolUtil.MOVE -> handleMove(splitInput);
            case ProtocolUtil.GAME_OVER -> handleGameOver(splitInput);
            case ProtocolUtil.ERROR -> handleError(splitInput);
            case ExtendedProtocolUtil.RANK -> handleRank(splitInput);
            case ExtendedProtocolUtil.CHAT -> handleChat(input);
            case ExtendedProtocolUtil.WHISPER -> handleWhisper(input);
            case ExtendedProtocolUtil.CANNOTWHISPER -> handleCannotWhisper(splitInput);
        }
    }

    /**
     * Handles receiving the terminal part of the hello handshake.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleHello(String[] splitInput) {
        client.handleHello(ProtocolUtil.parseHello(splitInput));
    }

    /**
     * Handles informing the {@code Client} that they have successfully logged in.
     */
    private void handleAcceptedLogin() {
        client.handleAcceptedLogin();
    }

    /**
     * Handles informing the {@code Client} that they have not successfully logged in.
     */
    private void handleRejectedLogin() {
        client.handleRejectedLogin();
    }

    /**
     * Handles informing the {@code Client} of the currently active players on the {@code Server}.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleList(String[] splitInput) {
        client.printlnOut("Active players: " + Arrays.stream(splitInput).skip(1)
                .collect(Collectors.joining(", ")));
    }

    /**
     * Handles informing the {@code Client} that they have joined a new {@code Game}.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleNewGame(String[] splitInput) {
        client.handleNewGame(username, splitInput[1], splitInput[2]);
    }

    /**
     * Handles informing the {@code Client} that a new {@code Move} has been played in their
     * {@code Game}.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleMove(String[] splitInput) {
        client.handleMove(Arrays.stream(splitInput).skip(1).mapToInt(Integer::parseInt).toArray());
    }

    /**
     * Handles informing the {@code Client} that their {@code Game} has ended.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleGameOver(String[] splitInput) {
        switch (GameOverReason.valueOf(splitInput[1])) {
            case GameOverReason.DISCONNECT -> client.printlnOut("You won by disconnect!");
            case GameOverReason.DRAW -> client.printlnOut("It was a draw!");
            case GameOverReason.VICTORY -> {
                if (splitInput[2].equals(username)) {
                    client.printlnOut("Outright victory!");
                } else {
                    client.printlnOut("You lost. Better luck next time!");
                }
            }
        }
        client.handleGameOver();
    }

    /**
     * Handles informing the {@code Client} that an error occurred on the {@code Server}-side when
     * processing their request.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleError(String[] splitInput) {
        client.printErr("Error message from server: " + splitInput[1]);
    }

    /**
     * Handles informing the {@code Client} of the rankings of active and inactive {@code Client}s
     * on the {@code Server}.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleRank(String[] splitInput) {
        client.printlnOut("Player wins: " +
                                  IntStream.iterate(1, i -> i < splitInput.length, i -> i + 2)
                                          .mapToObj(i -> splitInput[i] + "->" + splitInput[i + 1])
                                          .collect(Collectors.joining(", ")));
    }

    /**
     * Handles informing the {@code Client} of a new global message received.
     * @param input A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleChat(String input) {
        String[] splitInput = ExtendedProtocolUtil.splitNamedMessage(input);
        client.printlnOut("Global message from " + splitInput[1] + ": " + splitInput[2]);
    }

    /**
     * Handles informing the {@code Client} of a new whisper message received.
     * @param input A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleWhisper(String input) {
        String[] splitInput = ExtendedProtocolUtil.splitNamedMessage(input);
        client.printlnOut("Whisper message from " + splitInput[1] + ": " + splitInput[2]);
    }

    /**
     * Handles informing the {@code Client} that their request to deliver a whisper message was not
     * successful.
     * @param splitInput A message from the {@code Server}, sent over the {@code Connection}
     */
    private void handleCannotWhisper(String[] splitInput) {
        client.printlnOut("Your whisper to " + splitInput[1] + " could not be delivered");
    }

    @Override
    public void handleDisconnect() {
        client.handleDisconnect();
    }

    /**
     * Handles sending a log in request to the {@code Server}.
     * @param newUsername The username to attempt logging in with
     */
    public void sendLoginRequest(String newUsername) {
        this.username = newUsername;
        connection.writeOutput(ProtocolUtil.login(newUsername));
    }

    /**
     * Handles sending a request to list the active {@code Client}s to the {@code Server}.
     */
    public void sendListRequest() {
        connection.writeOutput(ProtocolUtil.list());
    }

    /**
     * Handles sending a request to join the default queue to the {@code Server}.
     */
    public void sendQueueRequest() {
        connection.writeOutput(ProtocolUtil.queue());
    }

    /**
     * Handles sending a request to join a named queue to the {@code Server}.
     * @param queue The name of the queue to join
     */
    public void sendQueueRequest(String queue) {
        connection.writeOutput(ExtendedProtocolUtil.queue(queue));
    }

    /**
     * Handles sending a request to play a {@code Move} to the {@code Server}.
     * @param values The array representation of the {@code Move} to play
     */
    public void sendMoveRequest(int[] values) {
        connection.writeOutput(ProtocolUtil.move(values));
    }

    /**
     * Handles sending a request to list the rankings of active and inactive {@code Client}s to the
     * {@code Server}.
     */
    public void sendRankRequest() {
        connection.writeOutput(ExtendedProtocolUtil.rank());
    }

    /**
     * Handles sending a request to send a global message to the {@code Server}.
     * @param message The message to send
     */
    public void sendChatRequest(String message) {
        connection.writeOutput(ExtendedProtocolUtil.chat(message));
    }

    /**
     * Handles sending a request to send a whisper message to a specific {@code Client} to the
     * {@code Server}.
     * @param receiver The name of the {@code Client} who should receive the message
     * @param message The message to send
     */
    public void sendWhisperRequest(String receiver, String message) {
        connection.writeOutput(ExtendedProtocolUtil.whisper(receiver, message));
    }

    /**
     * Returns the username of the {@code Client}.
     * @return The username of the {@code Client}
     */
    public String getUsername() {
        return username;
    }
}
