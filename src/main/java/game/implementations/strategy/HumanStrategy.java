package game.implementations.strategy;

import game.interfaces.Game;
import game.interfaces.Move;
import game.interfaces.Piece;
import game.interfaces.Strategy;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Implementation of {@code Strategy} which asks the user to directly input their own {@code Move}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Move
 * @see Strategy
 */
public final class HumanStrategy<T extends Enum<T> & Piece> implements Strategy<T> {
    private static final Scanner SCANNER = new Scanner(System.in);

    @Override
    public <U extends Move<T>, V extends Game<T>> U determineMove(V game) {
        U move;
        do {
            int index = determineIndex();
            T piece = determinePiece(game.getValidPieces());
            move = game.returnMoveIfValid(piece, index);
        } while (move == null);
        return move;
    }

    /**
     * Asks the user to choose the index for their next {@code Move}.
     * @return The index the user chose
     */
    private int determineIndex() {
        do {
            try {
                System.out.print("Enter the index to play the next piece: ");
                return SCANNER.nextInt();
            } catch (InputMismatchException _) {
                System.out.println("Please input a valid integer");
            }
        } while (true);
    }

    /**
     * Asks the user to choose the {@code Piece} for their next {@code Move}.
     * @param validPieces The set of {@code Piece}s to choose from
     * @return The {@code Piece} the user chose
     */
    @SuppressWarnings("unchecked")
    private T determinePiece(EnumSet<T> validPieces) {
        byte count = 1;
        System.out.println("Remaining pieces:");
        for (T piece : validPieces) {
            System.out.print(count++ + ": " + piece.getDisplay() + ", ");
        }
        System.out.println();
        Piece[] pieces = new Piece[validPieces.size()];
        validPieces.toArray(pieces);

        do {
            System.out.print("Opponent's next piece: ");
            try {
                return (T) pieces[SCANNER.nextInt() - 1];
            } catch (NumberFormatException | InputMismatchException _) {
                System.out.println("Please input a valid integer");
            } catch (IndexOutOfBoundsException _) {
                System.out.println("Please input one of the listed integers");
            }
        } while (true);
    }
}
