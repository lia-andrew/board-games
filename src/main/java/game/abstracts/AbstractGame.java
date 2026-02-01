package game.abstracts;

import game.interfaces.*;
import java.util.EnumSet;
import java.util.Objects;

/**
 * Abstract class used to provide basic functionality that is expected to be shared between
 * {@code Game} implementations.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Player
 */
public abstract class AbstractGame<T extends Enum<T> & Piece> implements Game<T> {
    protected final Player<T>[] players;
    protected Board<T> board;
    protected EnumSet<T> validPieces;
    protected byte turn = 0;
    protected int turnCounter = 1;

    /**
     * Constructs a new {@code AbstractGame}, with the arguments becoming the {@code Player}s of
     * this new {@code AbstractGame}.
     * @param player1 The provided {@code Player}, who will take the first turn
     * @param player2 The provided {@code Player}, who will take the second turn
     */
    @SuppressWarnings("unchecked")
    protected AbstractGame(Player<T> player1, Player<T> player2) {
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
    }

    /**
     * Constructs a copy of the provided {@code AbstractGame}, deep-copying the fields available in
     * this abstract class and requiring extending classes to provide the implementation-specific
     * deep-copying.
     * @param original The original {@code AbstractGame} which the constructed copy is based off of
     */
    protected AbstractGame(AbstractGame<T> original) {
        players = original.players.clone();
        board = original.board.deepCopy();
        validPieces = EnumSet.copyOf(original.validPieces);
        turn = original.turn;
        turnCounter = original.turnCounter;
    }

    @Override
    public <U extends Move<T>> void doMove(U move) {
        board.setField(move.getIndex(), move.getPiece());
        turn ^= 1;
        turnCounter++;
    }

    @Override
    public final EnumSet<T> getValidPieces() {
        return validPieces;
    }

    @Override
    public final Player<T> getActivePlayer() {
        return players[turn];
    }

    @Override
    public final int getTurnCount() {
        return turnCounter;
    }

    @Override
    public final String getBoardDisplay() {
        return board.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractGame<?> that = (AbstractGame<?>) o;
        return turnCounter == that.turnCounter && Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turnCounter);
    }
}
