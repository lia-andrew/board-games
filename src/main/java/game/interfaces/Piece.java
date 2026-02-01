package game.interfaces;

/**
 * Interface used for all piece-sets of implemented {@code Game}s to share a common super-type.
 * The implementations of this interface are used to unify the generic classes in the {@link game}
 * package.
 *
 * @see Game
 */
public interface Piece {
    /**
     * Returns a human-friendly text representation of this {@code Piece}; to be used when printing
     * to terminal or otherwise being shown to a user.
     * @return The human-friendly text representation of this {@code Piece}
     */
    String getDisplay();
}
