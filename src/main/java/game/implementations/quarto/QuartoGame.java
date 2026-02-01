package game.implementations.quarto;

import game.abstracts.AbstractGame;
import game.interfaces.Move;
import game.interfaces.Player;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

//TODO implement checkIfQuarto helper method to reduce duplicate code

/**
 * Implementation of {@code AbstractGame} which defines {@code QuartoPiece} as the type of
 * {@code Piece} to be used in a {@code QuartoGame}.
 *
 * @see AbstractGame
 * @see QuartoPiece
 * @see Player
 * @see game.interfaces.Piece
 */
public final class QuartoGame extends AbstractGame<QuartoPiece> {
    private static final int BOARD_LENGTH = 4;

    private QuartoPiece nextPiece;
    private boolean quarto = false;
    private boolean calledQuarto = false;

    /**
     * Constructs a new {@code QuartoGame}, with the arguments becoming the {@code Player}s of
     * this new {@code QuartoGame}.
     * @param player1 The provided {@code Player}, who will take the first turn
     * @param player2 The provided {@code Player}, who will take the second turn
     */
    public QuartoGame(Player<QuartoPiece> player1, Player<QuartoPiece> player2) {
        super(player1, player2);
        board = new QuartoBoard(BOARD_LENGTH);
        validPieces = EnumSet.complementOf(EnumSet.of(QuartoPiece.EMPTY, QuartoPiece.QUARTO));
        nextPiece = QuartoPiece.EMPTY;
    }

    /**
     * Constructs a copy of the provided {@code QuartoGame}, deep-copying the fields specified in
     * this implementation and otherwise using the parent's, {@code AbstractGame}'s, copy
     * constructor.
     * @param original The original {@code QuartoGame} which the constructed copy is based off of
     */
    private QuartoGame(QuartoGame original) {
        super(original);
        nextPiece = original.nextPiece;
        quarto = original.quarto;
        calledQuarto = original.calledQuarto;
    }

    @Override
    public QuartoGame deepCopy() {
        return new QuartoGame(this);
    }

    @Override
    public <U extends Move<QuartoPiece>> void doMove(U move) {
        super.doMove(new QuartoMove(nextPiece, move.getIndex()));
        checkIfQuarto(move.getIndex(), nextPiece.ordinal());
        setNextPiece(move.getPiece());
    }

    /**
     * Advances the game state by checking if the {@code QuartoPiece} described by its bitmask,
     * {@code pieceMask}, lies on a 4-long line of {@code QuartoPiece}s sharing at least one
     * attribute. If so, the {@code quarto} variable will be updated to reflect such.
     * @param index The index of the {@code QuartoPiece} to check
     * @param pieceMask The bitmask of the {@code QuartoPiece} to check
     */
    private void checkIfQuarto(int index, int pieceMask) {
        if (quarto) {
            return;
        }

        quarto = true;
        // check row
        int mask = 0;
        int totalPositions = board.getTotalPositions();
        int quotient = index / BOARD_LENGTH;
        int start = quotient * BOARD_LENGTH;
        int end = start + BOARD_LENGTH;
        for (int i = start; i < end; i++) {
            QuartoPiece piece = board.getField(i);
            if (piece == QuartoPiece.EMPTY) {
                mask = 0b1111;
                break;
            }
            mask |= pieceMask ^ piece.ordinal();
        }
        if ((mask & 0b1111) != 0b1111) {
            return;
        }

        // check column
        mask = 0;
        int remainder = index - BOARD_LENGTH * quotient;
        for (int i = remainder; i < totalPositions; i += BOARD_LENGTH) {
            QuartoPiece piece = board.getField(i);
            if (piece == QuartoPiece.EMPTY) {
                mask = 0b1111;
                break;
            }
            mask |= pieceMask ^ piece.ordinal();
        }
        if ((mask & 0b1111) != 0b1111) {
            return;
        }

        // check diagonal
        if (quotient == remainder) {
            mask = 0;
            for (int i = 0; i < totalPositions; i += BOARD_LENGTH + 1) {
                QuartoPiece piece = board.getField(i);
                if (piece == QuartoPiece.EMPTY) {
                    mask = 0b1111;
                    break;
                }
                mask |= pieceMask ^ piece.ordinal();
            }
            if ((mask & 0b1111) != 0b1111) {
                return;
            }
        }

        // check diagonal
        if (remainder == totalPositions - 1 - quotient) {
            mask = 0;
            for (int i = BOARD_LENGTH - 1; i < totalPositions; i += BOARD_LENGTH - 1) {
                QuartoPiece piece = board.getField(i);
                if (piece == QuartoPiece.EMPTY) {
                    mask = 0b1111;
                    break;
                }
                mask |= pieceMask ^ piece.ordinal();
            }
            if ((mask & 0b1111) != 0b1111) {
                return;
            }
        }

        quarto = false;
    }

    /**
     * Advances the game state by removing the provided {@code QuartoPiece} from the pool of
     * playable {@code Piece}s and checks if other game advancements should be made according to
     * QUARTO game rules.
     * @param piece The {@code QuartoPiece} that is to be removed from the pool of playable
     *          {@code Piece}s
     */
    private void setNextPiece(QuartoPiece piece) {
        nextPiece = piece;
        validPieces.remove(piece);
        if (turnCounter == 2) {
            validPieces.add(QuartoPiece.QUARTO);
        } else if (piece == QuartoPiece.QUARTO) {
            calledQuarto = true;
        } else if (turnCounter == 17) {
            validPieces.add(QuartoPiece.EMPTY);
        } else if (piece == QuartoPiece.EMPTY) {
            validPieces.remove(QuartoPiece.QUARTO);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QuartoMove returnMoveIfValid(QuartoPiece piece, int index) {
        if (!isGameOver() && board.isValidField(index) &&
                board.getField(index) == QuartoPiece.EMPTY && validPieces.contains(piece)) {
            return new QuartoMove(piece, index);
        }
        return null;
    }

    @Override
    public boolean isGameOver() {
        return calledQuarto || validPieces.isEmpty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QuartoMove> getValidMoves() {
        if (isGameOver()) {
            return List.of();
        }

        List<QuartoMove> validMoves = new ArrayList<>();
        for (byte i = 0; i < board.getTotalPositions(); i++) {
            if (board.getField(i) == QuartoPiece.EMPTY) {
                for (QuartoPiece piece : validPieces) {
                    validMoves.add(new QuartoMove(piece, i));
                }
            }
        }
        return validMoves;
    }

    @Override
    public Player<QuartoPiece> getWinner() {
        if (!calledQuarto) {
            return null;
        }

        if (quarto) {
            turn ^= 1;
        }
        return players[turn];
    }
}
