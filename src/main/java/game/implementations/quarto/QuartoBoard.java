package game.implementations.quarto;

import game.abstracts.AbstractBoard;
import java.util.Arrays;

//TODO make dynamic BOARD_DISPLAY & SEPARATOR

/**
 * Implementation of {@code AbstractBoard} which defines {@code QuartoPiece} as the type of
 * {@code Piece} on a {@code QuartoBoard}.
 *
 * @see AbstractBoard
 * @see QuartoPiece
 * @see game.interfaces.Piece
 */
public final class QuartoBoard extends AbstractBoard<QuartoPiece> {
    private static final String[] BOARD_DISPLAY =
            {" 00 | 01 | 02 | 03 ", " 04 | 05 | 06 | 07 ", " 08 | 09 | 10 | 11 ",
                    " 12 | 13 | 14 | 15 "};
    private static final String SEPARATOR = "----+----+----+----";

    /**
     * Constructs a new {@code QuartoBoard}, with side-length equal to the provided
     * {@code boardLength}.
     * @param boardLength The provided side-length of this {@code QuartoBoard}
     */
    public QuartoBoard(int boardLength) {
        super(boardLength);
        fields = new QuartoPiece[totalPositions];
        Arrays.fill(fields, QuartoPiece.EMPTY);
    }

    /**
     * Constructs a copy of the provided {@code QuartoBoard}, deep-copying the fields specified in
     * this implementation and otherwise using the parent's, {@code AbstractBoard}'s, copy
     * constructor.
     * @param original The original {@code QuartoBoard} which the constructed copy is based off of
     */
    private QuartoBoard(QuartoBoard original) {
        super(original);
        fields = new QuartoPiece[totalPositions];
        System.arraycopy(original.fields, 0, fields, 0, totalPositions);
    }

    @Override
    public QuartoBoard deepCopy() {
        return new QuartoBoard(this);
    }

    @Override
    public boolean isValidField(int index) {
        return index > -1 && index < totalPositions;
    }

    @Override
    public String toString() {
        StringBuilder display = new StringBuilder();
        for (int i = 0; i < boardLength; i++) {
            StringBuilder row = new StringBuilder();
            int rowVal = i * boardLength;
            for (int j = 0; j < boardLength; j++) {
                String pieceChars = getField(rowVal + j).getDisplay();
                if (pieceChars.equals("DRAW")) {
                    row.append(DELIM);
                } else {
                    row.append(pieceChars);
                }
                if (j < boardLength - 1) {
                    row.append("|");
                }
            }
            display.append(row).append(DELIM).append(BOARD_DISPLAY[i]);
            if (i < boardLength - 1) {
                display.append("\n").append(SEPARATOR).append(DELIM).append(SEPARATOR).append("\n");
            }
        }
        return display.toString();
    }
}
