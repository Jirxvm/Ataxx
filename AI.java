/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.ArrayList;
import java.util.Random;
import static ataxx.PieceColor.*;
import static ataxx.Board.*;


/** A Player that computes its own moves.
 *  @author J. Masoudi
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 4;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Return a list of moves for position BOARD with all possible
     *  legal moves for whose turn it is on. */
    private ArrayList<Move> legalMoves(Board board) {
        ArrayList<Move> goodMoves = new ArrayList();
        for (int index = 0; index < EXTENDED_SIDE * EXTENDED_SIDE; index += 1) {
            if (board.get(index) == board.whoseMove()) {
                for (int i = -2; i <= 2; i += 1) {
                    for (int j = -2; j <= 2; j += 1) {
                        int moveTo = Board.neighbor(index, i, j);
                        if (board.get(moveTo) == EMPTY) {
                            char c0 = (char) ((index - (EXTENDED_SIDE * 2 + 2))
                                    % EXTENDED_SIDE + 'a');
                            char r0 = (char) ((index - (EXTENDED_SIDE * 2 + 2))
                                    / EXTENDED_SIDE + '1');
                            char c1 = (char)
                                    ((moveTo - (EXTENDED_SIDE * 2 + 2))
                                            % EXTENDED_SIDE + 'a');
                            char r1 = (char)
                                    ((moveTo - (EXTENDED_SIDE * 2 + 2))
                                            / EXTENDED_SIDE + '1');
                            if (board.legalMove(c0, r0, c1, r1)) {
                                goodMoves.add(Move.move(c0, r0, c1, r1));
                            }
                        }
                    }
                }
            }
            if (board.legalMove(Move.pass())) {
                goodMoves.add(Move.pass());
            }
        }
        return goodMoves;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        ArrayList<Move> moves = legalMoves(board);
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }
        Move best = null;
        if (sense == 1) {
            int bestScore = -INFTY;
            for (int i = 0; i < moves.size(); i += 1) {
                board.makeMove(moves.get(i));
                int potential = minMax(board, depth - 1,
                        false, -1, alpha, beta);
                if (potential > bestScore) {
                    bestScore = potential;
                    best = moves.get(i);
                }
                if (potential > alpha) {
                    alpha = potential;
                }
                board.undo();
                if (alpha >= beta) {
                    return bestScore;
                }
            }
            if (saveMove) {
                _lastFoundMove = best;
            }
            return bestScore;
        } else {
            int bestScore = INFTY;
            for (int i = 0; i < moves.size(); i += 1) {
                board.makeMove(moves.get(i));
                int potential2 = minMax(board, depth - 1,
                    false, 1, alpha, beta);
                if (potential2 < bestScore) {
                    bestScore = potential2;
                    best = moves.get(i);
                }
                if (potential2 < beta) {
                    beta = potential2;
                }
                board.undo();
                if (alpha >= beta) {
                    return bestScore;
                }

            }
            if (saveMove) {
                _lastFoundMove = best;
            }
            return bestScore;
        }

    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        return board.redPieces() - board.bluePieces();

    }

    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}
