package network.implementations.quarto;

import game.implementations.GenericPlayer;
import game.implementations.quarto.QuartoGame;
import game.implementations.quarto.QuartoMove;
import game.implementations.quarto.QuartoPiece;
import game.interfaces.Player;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import network.abstracts.AbstractClient;
import game.implementations.strategy.NullStrategy;
import game.interfaces.Strategy;

/**
 * Implementation of {@code AbstractClient} which defines {@code QuartoPiece} as the unifying type
 * of the used generic classes.
 *
 * @see AbstractClient
 * @see network.interfaces.Server
 */
public class QuartoClient extends AbstractClient<QuartoPiece> {
    /**
     * Constructs a new {@code QuartoClient} that attempts to connect to the {@code Sever} on the
     * provided IP address and port number.
     * @param address The IP address to connect to
     * @param port The port number to connect to
     * @param scanner The scanner instance to use for this {@code QuartoClient}
     * @throws IOException Thrown if there is an issue connecting to the {@code Server}
     */
    public QuartoClient(InetAddress address, int port, Scanner scanner) throws IOException {
        super(address, port, scanner);
    }

    @Override
    protected void askMove() {
        if (game != null && !game.isGameOver() &&
                game.getActivePlayer().getName().equals(serverHandler.getUsername())) {
            int[] moveVals = game.getActivePlayer().takeTurn(game).toArray();
            if (game.getTurnCount() == 1) {
                moveVals = new int[]{moveVals[1]};
            }
            serverHandler.sendMoveRequest(moveVals);
        } else {
            printlnOut("You cannot make a move yet, please wait");
        }
    }

    @Override
    public void handleNewGame(String myUsername, String username1, String username2) {
        printlnOut("A new Quarto game has started!");
        Player<QuartoPiece> player1;
        Player<QuartoPiece> player2;
        Strategy<QuartoPiece> nullStrategy = new NullStrategy<>();
        if (myUsername.equals(username1)) {
            player1 = new GenericPlayer<>(username1, strategy);
            player2 = new GenericPlayer<>(username2, nullStrategy);
            printlnOut("You are versus " + username2);
            printlnOut("You have the first turn.");
        } else {
            player1 = new GenericPlayer<>(username1, nullStrategy);
            player2 = new GenericPlayer<>(username2, strategy);
            printlnOut("You are versus " + username1);
            printlnOut("Your opponent has the first turn.");
        }
        game = new QuartoGame(player1, player2);
        if (mistimedMove != null) {
            handleMove(mistimedMove);
            mistimedMove = null;
        }
    }

    @Override
    public void handleMove(int[] values) {
        if (game == null) {
            mistimedMove = values;
            return;
        }
        printlnOut("A new move has been made!");
        QuartoPiece piece;
        if (values.length == 1) {
            piece = QuartoPiece.values()[values[0]];
            game.doMove(new QuartoMove(piece, 0));
        } else {
            piece = QuartoPiece.values()[values[1]];
            game.doMove(new QuartoMove(piece, values[0]));
            printlnOut("New move at index " + values[0]);
        }
        printlnOut(piece + " is the next piece to be played");
        printlnOut(game.getBoardDisplay());
    }
}
