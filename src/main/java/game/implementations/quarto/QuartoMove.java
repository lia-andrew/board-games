package game.implementations.quarto;

import game.abstracts.AbstractMove;

/**
 * Implementation of {@code AbstractMove} which defines {@code QuartoPiece} as the type of
 * {@code Piece} in a {@code QuartoMove}.
 *
 * @see AbstractMove
 * @see QuartoGame
 * @see QuartoPiece
 * @see game.interfaces.Piece
 */
public final class QuartoMove extends AbstractMove<QuartoPiece> {
    /**
     * Constructs a new {@code QuartoMove} by directly calling the parent's, {@code AbstractMove}'s,
     * constructor.
     * @param piece The {@code QuartoPiece} of this new {@code QuartoMove}
     * @param index The index of this new {@code QuartoMove}
     */
    public QuartoMove(QuartoPiece piece, int index) {
        super(piece, index);
    }
}
