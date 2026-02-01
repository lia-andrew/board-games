package network.abstracts;

import game.interfaces.Game;
import game.interfaces.Piece;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import network.protocol.ExtendedProtocol;
import network.protocol.GameOverReason;
import exceptions.SimultaneousDisconnectException;
import network.implementations.ClientHandler;
import network.interfaces.Server;

//TODO Concurrent collections inplace of synchronized
//TODO Implement rank helper method to separate concerns from implementations

/**
 * Abstract class used to provide basic functionality that is expected to be shared between
 * {@code Sever} implementations.
 * @param <T> A generic type that unifies the generic classes in the {@link network} package
 * @param <U> A generic type to define the type of {@code Game}s that are playable given the other
 *           generic type, {@code T}
 *
 * @see Game
 * @see network.interfaces.Client
 * @see Server
 */
public abstract class AbstractServer<T extends Enum<T> & Piece, U extends Game<T>>
        implements Server {
    protected static String hello;
    private static final EnumSet<ExtendedProtocol> SERVER_EXTENSIONS =
            EnumSet.of(ExtendedProtocol.NAMEDQUEUES, ExtendedProtocol.RANK, ExtendedProtocol.CHAT);
    private static final String DEFAULT_USERNAME = "ANONYMOUS";
    private static final String DEFAULT_QUEUE = "DEFAULT";

    protected final ServerSocket socket;
    protected final Map<ClientHandler, U> clients = Collections.synchronizedMap(new HashMap<>());
    protected final Map<String, List<ClientHandler>> namedQueues =
            Collections.synchronizedMap(new HashMap<>());
    protected final Map<String, Integer> userWins = new HashMap<>();

    /**
     * Constructs a new {@code AbstractServer} that listens on the provided port number.
     * @param port The port number to listen on
     * @throws IOException Thrown if there is an issue with opening the server socket
     */
    protected AbstractServer(int port) throws IOException {
        socket = new ServerSocket(port);
        printDebug("The server is running on port: " + socket.getLocalPort());
        acceptConnections();
    }

    /**
     * Listens on this {@code Server}'s port number for {@code Client}s attempting to connect. If
     * the server socket is unexpectedly closed, this {@code Server} is terminated.
     */
    private void acceptConnections() {
        while (!socket.isClosed()) {
            try {
                clients.put(new ClientHandler(socket.accept(), this), null);
                printDebug("New connection");
            } catch (IOException e) {
                printDebug("Error accepting connection: " + e.getMessage());
            }
        }
        printDebug("Server socket unexpectedly closed. Goodbye!");
        System.exit(0);
    }

    @Override
    public final void handleJoinQueue(ClientHandler handler, String queue) {
        synchronized (namedQueues) {
            if (!namedQueues.containsKey(queue)) {
                namedQueues.put(queue, new LinkedList<>());
            }
            List<ClientHandler> namedQueue = namedQueues.get(queue);
            namedQueue.add(handler);
            if (canStartGame(namedQueue)) {
                startGame(namedQueue);
            }
        }
    }

    /**
     * Checks if there are enough {@code Client}s in queue to start a new {@code Game}.
     * @param queue The queue to check
     * @return {@code True}: there are enough {@code Client}s to start a new {@code Game} <br>
     *          {@code False}: there are not enough {@code Client}s to start a new {@code Game}
     */
    private boolean canStartGame(List<ClientHandler> queue) {
        return queue.size() > 1;
    }

    /**
     * Starts a new {@code Game} between the {@code Client}s that are at the front of the provided
     * queue.
     * @param queue The queue which is used to get the {@code Client}s to start the new {@code Game}
     */
    protected abstract void startGame(List<ClientHandler> queue);

    @Override
    public final void handleLeaveQueue(ClientHandler handler, String queue) {
        synchronized (namedQueues) {
            List<ClientHandler> namedQueue = namedQueues.get(queue);
            if (namedQueue.size() == 1) {
                namedQueues.remove(queue);
            } else {
                namedQueue.remove(handler);
            }
        }
    }

    @Override
    public final void handleChat(ClientHandler handler, String message) {
        synchronized (clients) {
            clients.keySet().stream().filter(other -> !other.equals(handler) && other.canChat())
                    .forEach(other -> other.sendGlobalChat(handler.getUsername(), message));
        }
    }

    @Override
    public final void handleWhisper(ClientHandler handler, String username, String message) {
        synchronized (clients) {
            ClientHandler receiver = clients.keySet().stream()
                    .filter(other -> other.getUsername().equals(username) && other.canChat())
                    .findAny().orElse(null);
            if (receiver != null) {
                receiver.sendWhisper(handler.getUsername(), message);
            } else {
                handler.sendCannotWhisper(username);
            }
        }
    }

    @Override
    public final void handleDisconnect(ClientHandler handler, String queue) {
        U game = clients.remove(handler);
        try {
            synchronized (game) {
                ClientHandler winner;
                synchronized (clients) {
                    winner = clients.entrySet().stream()
                            .filter(entry -> game.equals(entry.getValue())).findAny()
                            .orElseThrow(SimultaneousDisconnectException::new).getKey();
                }
                winner.sendGameOver(GameOverReason.DISCONNECT, winner.getUsername());
                clients.put(winner, null);
                printDebug(winner.getUsername() + " won by disconnect");
            }
        } catch (NullPointerException _) {
            List<ClientHandler> namedQueue = namedQueues.get(queue);
            if (namedQueue != null) {
                namedQueue.remove(handler);
            }
        } catch (SimultaneousDisconnectException _) {
            printDebug(handler.getUsername() + " disconnected at the same time as their opponent");
        }
        printDebug(handler.getUsername() + " disconnected");
    }

    @Override
    public final boolean isNameTaken(String username) {
        synchronized (clients) {
            return clients.keySet().stream()
                    .anyMatch(handler -> handler.getUsername().equals(username));
        }
    }

    @Override
    public final boolean isInGame(ClientHandler handler) {
        return clients.get(handler) != null;
    }

    @Override
    public final String getHello() {
        return hello;
    }

    @Override
    public final EnumSet<ExtendedProtocol> getServerExtensions() {
        return SERVER_EXTENSIONS;
    }

    @Override
    public final String getDefaultUsername() {
        return DEFAULT_USERNAME;
    }

    @Override
    public final List<String> getUsernames() {
        synchronized (clients) {
            return clients.keySet().stream().map(ClientHandler::getUsername)
                    .filter(username -> !username.equals(DEFAULT_USERNAME)).toList();
        }
    }

    @Override
    public final Set<Map.Entry<String, Integer>> getRankInfo() {
        return userWins.entrySet();
    }

    @Override
    public final String getDefaultQueue() {
        return DEFAULT_QUEUE;
    }

    @Override
    public void printDebug(String message) {
        System.out.println(message);
    }
}
