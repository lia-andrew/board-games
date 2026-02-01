package game.implementations.quarto;

import game.interfaces.Piece;

//TODO implement better empty & draw representation

/**
 * Implementation of {@code Piece} which enumerates all the possibly-playable {@code Piece}s that
 * can be played in a {@code QuartoGame}. The order of the first 16 enumerations is important for
 * ordinal-related functionality and is noted at the side of each.
 *
 * @see QuartoGame
 * @see Piece
 */
public enum QuartoPiece implements Piece {
    LIGHT_SMALL_ROUND_SOLID("LSRS"), //0000
    DARK_SMALL_ROUND_SOLID("DSRS"), //1000
    LIGHT_LARGE_ROUND_SOLID("LLRS"), //0100
    DARK_LARGE_ROUND_SOLID("DLRS"), //1100
    LIGHT_SMALL_SQUARE_SOLID("LSSS"), //0010
    DARK_SMALL_SQUARE_SOLID("DSSS"), //1010
    LIGHT_LARGE_SQUARE_SOLID("LLSS"), //0110
    DARK_LARGE_SQUARE_SOLID("DLSS"), //1110
    LIGHT_SMALL_ROUND_HOLLOW("LSRH"), //0001
    DARK_SMALL_ROUND_HOLLOW("DSRH"), //1001
    LIGHT_LARGE_ROUND_HOLLOW("LLRS"), //0101
    DARK_LARGE_ROUND_HOLLOW("DLRH"), //1101
    LIGHT_SMALL_SQUARE_HOLLOW("LSSH"), //0011
    DARK_SMALL_SQUARE_HOLLOW("DSSH"), //1011
    LIGHT_LARGE_SQUARE_HOLLOW("LLSH"), //0111
    DARK_LARGE_SQUARE_HOLLOW("DLSH"), //1111
    QUARTO("QUARTO"), EMPTY("DRAW");

    private final String display;

    /**
     * Constructs a new {@code QuartoPiece} with the provided {@code display}.
     * @param display The human-friendly text representation that can be shown to the user
     */
    QuartoPiece(String display) {
        this.display = display;
    }

    @Override
    public String getDisplay() {
        return display;
    }
}
