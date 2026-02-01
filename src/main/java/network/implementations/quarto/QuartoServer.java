package network.implementations.quarto;

import game.implementations.GenericPlayer;
import game.implementations.quarto.QuartoGame;
import game.implementations.quarto.QuartoMove;
import game.implementations.quarto.QuartoPiece;
import game.interfaces.Player;
import java.io.IOException;
import java.util.List;
import network.protocol.GameOverReason;
import exceptions.InvalidServerStateException;
import network.abstracts.AbstractServer;
import network.implementations.ClientHandler;
import game.implementations.strategy.NullStrategy;
import game.interfaces.Strategy;

/**
 * Implementation of {@code AbstractServer} which defines {@code QuartoGame} as the type of
 * {@code Game} to be played in a {@code QuartoServer}.
 *
 * @see QuartoGame
 * @see game.interfaces.Game
 * @see AbstractServer
 */
public final class QuartoServer extends AbstractServer<QuartoPiece, QuartoGame> {
    static {
        hello = "Hello from Quarto server";
    }

    /**
     * Constructs a new {@code QuartoServer} that listens on the provided port number.
     * @param port The port number to listen on
     * @throws IOException Thrown if there is an issue with opening the server socket
     */
    public QuartoServer(int port) throws IOException {
        super(port);
    }

    @SuppressWarnings("null")
    @Override
    protected void startGame(List<ClientHandler> queue) {
        ClientHandler handler1 = queue.removeFirst();
        ClientHandler handler2 = queue.removeFirst();

        String name1 = handler1.getUsername();
        String name2 = handler2.getUsername();

        Strategy<QuartoPiece> nullStrategy = new NullStrategy<>();
        QuartoGame game = new QuartoGame(new GenericPlayer<>(name1, nullStrategy),
                                         new GenericPlayer<>(name2, nullStrategy));

        clients.put(handler1, game);
        clients.put(handler2, game);

        handler1.sendNewGame(name1, name2);
        handler2.sendNewGame(name1, name2);
        printDebug("New game between " + handler1.getUsername() + " and " + handler2.getUsername());
    }

    @Override
    public void handleMove(ClientHandler handler, int[] values) {
        QuartoGame game = clients.get(handler);
        try {
            synchronized (game) {
                if (!game.getActivePlayer().getName().equals(handler.getUsername())) {
                    handler.sendError("Please wait for your turn");
                    return;
                }

                boolean isFirstTurn = game.getTurnCount() == 1;
                if (values.length > 2 || (values.length == 2 && isFirstTurn) ||
                        (values.length == 1 && !isFirstTurn)) {
                    handler.sendError("Wrong number of arguments in MOVE");
                    return;
                }

                ClientHandler other;
                synchronized (clients) {
                    other = clients.entrySet().stream()
                            .filter(entry -> game.equals(entry.getValue()) &&
                                    !entry.getKey().equals(handler)).findAny()
                            .orElseThrow(InvalidServerStateException::new).getKey();
                }

                QuartoMove newMove;
                if (values.length == 1) {
                    newMove = game.returnMoveIfValid(QuartoPiece.values()[values[0]], 0);
                } else {
                    newMove = game.returnMoveIfValid(QuartoPiece.values()[values[1]], values[0]);
                }

                if (newMove != null) {
                    game.doMove(newMove);
                    handler.sendMove(values);
                    other.sendMove(values);
                } else {
                    handler.sendError("Invalid move");
                    return;
                }
                printDebug(handler.getUsername() + " requested MOVE");

                if (game.isGameOver()) {
                    clients.put(handler, null);
                    clients.put(other, null);

                    Player<QuartoPiece> winner = game.getWinner();
                    if (winner != null) {
                        handler.sendGameOver(GameOverReason.VICTORY, winner.getName());
                        other.sendGameOver(GameOverReason.VICTORY, winner.getName());

                        userWins.put(winner.getName(),
                                     userWins.getOrDefault(winner.getName(), 0) + 1);
                    } else {
                        handler.sendGameOver();
                        other.sendGameOver();
                    }
                    printDebug("Game between " + handler.getUsername() + " and " +
                                       other.getUsername() + " finished; " +
                                       (winner != null ? winner.getName() : "nobody") + " won");
                }
            }
        } catch (NullPointerException _) {
            handler.sendError("You are not in a game, please join a queue and wait");
        }
    }
}
