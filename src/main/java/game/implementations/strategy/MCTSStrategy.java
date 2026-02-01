package game.implementations.strategy;

import game.interfaces.*;
import java.util.ArrayList;
import java.util.List;

//TODO node cache, instant return fully converged, parallelized

/**
 * Implementation of {@code Strategy} which employs the
 * <a href="https://en.wikipedia.org/wiki/Monte_Carlo_tree_search">
 * <cite>Monte-Carlo tree search</cite></a> algorithm to determine a {@code Move}.
 * @param <T> A generic type that unifies the generic classes in the {@link game} package
 *
 * @see Game
 * @see Move
 * @see Player
 * @see Strategy
 */
public final class MCTSStrategy<T extends Enum<T> & Piece> implements Strategy<T> {
    private static final float TIMEOUT = 5f;

    @Override
    public <U extends Move<T>, V extends Game<T>> U determineMove(V game) {
        Node<U> root = new Node<>(game, null, null);
        double end = System.nanoTime() + TIMEOUT * Math.pow(10, 9);
        while (System.nanoTime() < end) {
            Node<U> node = root;
            while (node.unplayedMoves.isEmpty() && !node.game.isGameOver()) {
                node = node.getBestChild();
            }

            if (!(node.game.isGameOver() || node.unplayedMoves.isEmpty())) {
                node = node.expand();
            }
            node.propagateResult(node.rollout());
        }

        int maxTrials = -1;
        U bestMove = null;
        for (Node<U> child : root.children) {
            if (child.trials > maxTrials) {
                maxTrials = child.trials;
                bestMove = child.parentMove;
            }
        }
        return bestMove;
    }

    /**
     * Class to represent a node within a tree-structure, specifically for MCTS.
     * @param <U> A generic type that represents the type of {@code Move}s that can be played
     *          with {@code Piece}s of type {@link T}
     */
    private final class Node<U extends Move<T>> {
        private final Game<T> game;
        private final Player<T> activePlayer;
        private final Node<U> parent;
        private final U parentMove;
        private final List<Node<U>> children;
        private final List<U> unplayedMoves;
        private int trials = 0;
        private float score = 0;

        /**
         * Constructs a new {@code Node}, using the provided arguments as a way to maintain a
         * tree-structure.
         * @param game The {@code Game} state which this {@code Node} represents
         * @param parent The {@code Node} one level above this {@code Node}
         * @param parentMove The {@code Move} the parent {@code Node} made to reach the provided
         *          {@code Game} state
         */
        private Node(Game<T> game, Node<U> parent, U parentMove) {
            this.game = game;
            this.activePlayer = game.getActivePlayer();
            this.parent = parent;
            this.parentMove = parentMove;
            children = new ArrayList<>();
            unplayedMoves = game.getValidMoves();
        }

        /**
         * Selects and plays a yet to be played {@code Move}, creates a new child {@code Node} to
         * represent this new part of the tree, and returns this child.
         * @return The yet to be unexplored child {@code Node}
         */
        private Node<U> expand() {
            U move = unplayedMoves.removeLast();
            Game<T> copy = game.deepCopy();
            copy.doMove(move);
            Node<U> child = new Node<>(copy, this, move);
            children.add(child);
            return child;
        }

        /**
         * Uses an Exploitation vs Exploration heuristic to select the child {@code Node} which is
         * most worth exploring.
         * @return The best child {@code Node} to explore, according to the heuristic
         */
        private Node<U> getBestChild() {
            double bestScore = Integer.MIN_VALUE;
            Node<U> bestChild = null;
            for (Node<U> child : children) {
                if (child.trials == 0) {
                    return child;
                }
                float exploit = child.score / child.trials;
                double explore = 1.4 * Math.sqrt(Math.log(trials) / child.trials);
                double totalScore = exploit + explore;
                if (totalScore > bestScore) {
                    bestScore = totalScore;
                    bestChild = child;
                }
            }
            return bestChild;
        }

        /**
         * Plays out a {@code Game}, starting with the {@code Game} state which this
         * {@code Node} represents, and returns the winning {@code Player} once a terminal state has
         * been reached.
         * @return The {@code Player} who won in the reached terminal {@code Game} state
         */
        private Player<T> rollout() {
            Game<T> copy = game.deepCopy();
            while (true) {
                if (copy.isGameOver()) {
                    return copy.getWinner();
                }
                List<U> validMoves = copy.getValidMoves();
                copy.doMove(validMoves.get((int) (Math.random() * validMoves.size())));
            }
        }

        /**
         * Updates the relevant attributes of this {@code Node} and all parent {@code Node}s,
         * recursively, according to the winning {@code Player} of a {@code rollout} in order to
         * keep the {@code getBestChild} heuristic correct.
         * @param winner The {@code Player} who is used to determine whether this {@code Node}
         *               should be rewarded or penalized
         */
        private void propagateResult(Player<T> winner) {
            trials++;
            if (winner != activePlayer) {
                //win
                score += 1;
            } else if (winner == null) {
                //draw
                score += Float.MIN_VALUE;
            }
            //            else {
            //                loss
            //                score -= 1;
            //            }
            if (parent != null) {
                parent.propagateResult(winner);
            }
        }
    }
}
