package network.implementations;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.EnumSet;
import network.Connection;
import network.interfaces.Handler;
import network.interfaces.Server;
import network.protocol.ExtendedProtocol;
import network.protocol.ExtendedProtocolUtil;
import network.protocol.GameOverReason;
import network.protocol.ProtocolUtil;

/**
 * Implementation of {@code Handler} which handles most of the {@code Client} logic on the
 * {@code Server} side.
 *
 * @see game.interfaces.Game
 * @see game.interfaces.Move
 * @see game.interfaces.Player
 * @see network.interfaces.Client
 * @see Handler
 * @see Server
 * @see Connection
 */
public final class ClientHandler implements Handler {
    private final Connection connection;
    private final Server server;
    private EnumSet<ExtendedProtocol> clientExtensions;
    private String username;
    private String lastQueue;

    /**
     * Constructs a new {@code ClientHandler} which communicates over the provided {@code Socket}.
     * @param socket The provided socket used to communicate with the {@code Client}
     * @param server The {@code Server} for which this {@code ClientHandler} is handling the
     *          {@code Client}-specific communication and logic
     * @throws IOException Thrown if there is an issue making a {@code Connection} to communicate
     *          with the {@code Client}
     */
    public ClientHandler(Socket socket, Server server) throws IOException {
        connection = new Connection(socket, this);
        this.server = server;
        username = server.getDefaultUsername();
    }

    @Override
    public void readInput(String input) {
        String[] splitInput = ProtocolUtil.splitInput(input);
        switch (splitInput[0]) {
            case ProtocolUtil.HELLO -> handleHello(splitInput);
            case ProtocolUtil.LOGIN -> handleLogin(splitInput);
            case ProtocolUtil.LIST -> handleList(splitInput);
            case ProtocolUtil.QUEUE -> handleQueue(splitInput);
            case ProtocolUtil.MOVE -> handleMove(splitInput);
            case ExtendedProtocolUtil.RANK -> handleRank(splitInput);
            case ExtendedProtocolUtil.CHAT -> handleChat(input);
            case ExtendedProtocolUtil.WHISPER -> handleWhisper(input);
            default -> sendError("Unknown command: " + splitInput[0]);
        }
    }

    /**
     * Handles receiving the initial part of the hello handshake.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleHello(String[] splitInput) {
        if (splitInput.length == 1) {
            sendError("No client description in HELLO");
        } else {
            clientExtensions = ProtocolUtil.parseHello(splitInput);
            connection.writeOutput(
                    ProtocolUtil.hello(server.getHello(), server.getServerExtensions()));
            server.printDebug("Successful HELLO");
        }
    }

    /**
     * Handles receiving a request to login.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleLogin(String[] splitInput) {
        if (!username.equals(server.getDefaultUsername())) {
            connection.writeOutput(ProtocolUtil.error("Already completed LOGIN"));
            server.printDebug(username + " requested LOGIN again");
        } else if (splitInput.length == 1) {
            sendError("No username in LOGIN");
        } else if (splitInput.length > 2) {
            sendError("Too many arguments in LOGIN");
        } else if (server.isNameTaken(splitInput[1])) {
            connection.writeOutput(ProtocolUtil.rejectLogin());
            server.printDebug("Rejected LOGIN with name: " + splitInput[1]);
        } else {
            username = splitInput[1];
            connection.writeOutput(ProtocolUtil.acceptLogin());
            server.printDebug("New LOGIN: " + username);
        }
    }

    /**
     * Handles receiving a request to list all {@code Client}s currently connected to the
     * {@code Server}.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleList(String[] splitInput) {
        if (splitInput.length != 1) {
            sendError("Too many arguments in LIST");
        } else if (isLoggedIn("LIST")) {
            connection.writeOutput(ProtocolUtil.list(server.getUsernames()));
            server.printDebug(username + " requested LIST");
        }
    }

    /**
     * Handles receiving a request to join a queue.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleQueue(String[] splitInput) {
        if (splitInput.length > 2 || (splitInput.length == 2 &&
                !clientExtensions.contains(ExtendedProtocol.NAMEDQUEUES))) {
            sendError("Too many arguments in QUEUE");
        } else if (server.isInGame(this)) {
            sendError("Must finish game before QUEUE");
        } else if (isLoggedIn("QUEUE")) {
            String queue = splitInput.length == 1 ? server.getDefaultQueue() : splitInput[1];
            if (lastQueue == null) {
                lastQueue = queue;
                server.handleJoinQueue(this, queue);
                server.printDebug(username + " joined a QUEUE");
            } else if (lastQueue.equals(queue)) {
                server.handleLeaveQueue(this, queue);
                lastQueue = null;
                server.printDebug(username + " left a QUEUE");
            } else {
                server.handleLeaveQueue(this, lastQueue);
                server.handleJoinQueue(this, queue);
                lastQueue = queue;
                server.printDebug(username + " changed QUEUE");
            }
        }
    }

    /**
     * Handles receiving a request to make a {@code Move}.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleMove(String[] splitInput) {
        if (isLoggedIn("MOVE")) {
            try {
                server.handleMove(this,
                                  Arrays.stream(splitInput).skip(1).mapToInt(Integer::parseInt)
                                          .toArray());
            } catch (NumberFormatException _) {
                sendError("Wrong argument type in MOVE");
            }
        }
    }

    /**
     * Handles receiving a request to list the {@code Server}'s {@code Client}-ranking information.
     * @param splitInput A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleRank(String[] splitInput) {
        if (!clientExtensions.contains(ExtendedProtocol.RANK)) {
            sendError("Please add RANK to your extensions and try again");
        } else if (splitInput.length != 1) {
            sendError("Too many arguments in RANK");
        } else if (isLoggedIn("RANK")) {
            connection.writeOutput(ExtendedProtocolUtil.rank(server.getRankInfo()));
            server.printDebug(username + " requested RANK");
        }
    }

    /**
     * Handles receiving a request to send a chat message.
     * @param input A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleChat(String input) {
        if (!clientExtensions.contains(ExtendedProtocol.CHAT)) {
            sendError("Please add CHAT to your extensions and try again");
        } else if (isLoggedIn("CHAT")) {
            String[] splitInput = ExtendedProtocolUtil.splitUnnamedMessage(input);
            server.handleChat(this, splitInput[1]);
            server.printDebug(username + " requested CHAT with message: " + splitInput[1]);
        }
    }

    /**
     * Handles receiving a request to send a whisper message.
     * @param input A message from the {@code Client}, sent over the {@code Connection}
     */
    private void handleWhisper(String input) {
        if (!clientExtensions.contains(ExtendedProtocol.CHAT)) {
            sendError("Please add CHAT to your extensions and try again");
        } else if (isLoggedIn("WHISPER")) {
            String[] splitInput = ExtendedProtocolUtil.splitNamedMessage(input);
            server.handleWhisper(this, splitInput[1], splitInput[2]);
            server.printDebug(username + " requested WHISPER with message: " + splitInput[2]);
        }
    }

    /**
     * Checks if the {@code Client} is logged in, and if not sends an error saying that the
     * {@code Client} must be logged in before requesting the provided command.
     * @param command The command that was requested by the {@code Client}
     */
    private boolean isLoggedIn(String command) {
        if (username.equals(server.getDefaultUsername())) {
            sendError("Must LOGIN before " + command);
            return false;
        }
        return true;
    }

    /**
     * Handles informing the {@code Client} that they have joined a new {@code Game}.
     * @param name1 The first {@code Player} in the new {@code Game}
     * @param name2 THe second {@code Player} in the new {@code Game}
     */
    public void sendNewGame(String name1, String name2) {
        lastQueue = null;
        connection.writeOutput(ProtocolUtil.newGame(name1, name2));
    }

    /**
     * Handles informing the {@code Client} that a new {@code Move} has been played in their
     * {@code Game}.
     * @param values The array representation of the {@code Move} that has been played
     */
    public void sendMove(int[] values) {
        connection.writeOutput(ProtocolUtil.move(values));
    }

    /**
     * Handles informing the {@code Client} that their {@code Game} has ended in a draw.
     */
    public void sendGameOver() {
        connection.writeOutput(ProtocolUtil.gameOver());
    }

    /**
     * Handles informing the {@code Client} that their {@code Game} has decisively ended.
     * @param reason The reason for the {@code Game}'s end
     * @param winner The winner of the {@code Game}
     */
    public void sendGameOver(GameOverReason reason, String winner) {
        connection.writeOutput(ProtocolUtil.gameOver(reason, winner));
    }

    /**
     * Handles informing the {@code Client} of a chat message that has been sent to all active
     * {@code Client}s.
     * @param sender The name of the sender of the chat message
     * @param message The message that has been sent
     */
    public void sendGlobalChat(String sender, String message) {
        connection.writeOutput(ExtendedProtocolUtil.globalChat(sender, message));
    }

    /**
     * Handles informing the {@code Client} of a whisper message that has been sent to them.
     * @param sender The name of the sender of the whisper message
     * @param message The message that has been sent
     */
    public void sendWhisper(String sender, String message) {
        connection.writeOutput(ExtendedProtocolUtil.whisper(sender, message));
    }

    /**
     * Handles informing the {@code Client} that their request to send a whisper message could not
     * be done due to the requested user not being logged in.
     * @param receiver The name of the user that was the intended receiver of the whisper message
     */
    public void sendCannotWhisper(String receiver) {
        connection.writeOutput(ExtendedProtocolUtil.cannotWhisper(receiver));
    }

    /**
     * Handles informing the {@code Client} that an error has occurred.
     * @param description The reason for the error
     */
    public void sendError(String description) {
        connection.writeOutput(ProtocolUtil.error(description));
        server.printDebug(username + "->" + description);
    }

    @Override
    public void handleDisconnect() {
        server.handleDisconnect(this, lastQueue);
    }

    /**
     * Checks if the {@code Client} supports receiving messages.
     * @return {@code True}: The {@code Client} can receive messages <br>
     *          {@code False}: The {@code Client} cannot receive messages
     */
    public boolean canChat() {
        return clientExtensions.contains(ExtendedProtocol.CHAT);
    }

    /**
     * Returns the username of the {@code Client}.
     * @return The {@code Client}'s username
     */
    public String getUsername() {
        return username;
    }
}
