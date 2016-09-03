package de.timgerdes.ai.algorithm;

import de.timgerdes.ai.control.Board;
import de.timgerdes.ai.control.Coordinate;
import de.timgerdes.ai.control.Direction;
import de.timgerdes.remote.RemoteBrowserController;

import java.util.ArrayList;

public class AlphaBetaAlgorithm extends GameAlgorithm {

    public AlphaBetaAlgorithm(RemoteBrowserController r) {
        super(r);
    }

    @Override
    public Direction findBestMove() {
        return (Direction) find(getBoard(), 7, Integer.MIN_VALUE, Integer.MAX_VALUE, true)[0];
    }

    public Object[] find(Board board, int depth, int alpha, int beta, boolean player) {
        Direction bestDirection = null;
        int bestScore;

        if(depth==0) {
            bestScore=heuristic(board);
        } else {
            if(player) {
                for(Direction direction : Direction.values()) {
                    Board workBoard = board.move(direction);

                    int points = workBoard.getScore() - board.getScore();

                    if(points == 0 && board.isEqual(workBoard)) {
                        continue;
                    }

                    int score = (int) find(workBoard, depth-1, alpha, beta, false)[1];

                    if(score > alpha) {
                        alpha = score;
                        bestDirection = direction;
                    }

                    if(beta <= alpha) { //beta cutoff
                        break;
                    }
                }

                bestScore = alpha;
            } else {
                ArrayList<Coordinate> emptyCells = board.getEmptyCells();
                int[] possibleValues = {2, 4};

                if(emptyCells.isEmpty()) {
                    bestScore = 0;
                }

                int i,j;
                node: for(Coordinate cell : emptyCells) {
                    i = cell.getX();
                    j = cell.getY();

                    for(int value : possibleValues) {
                        Board workBoard = board.clone();
                        workBoard.setCell(i, j, value);

                        int score = (int) find(workBoard, depth-1, alpha, beta, true)[1];

                        if(score < beta) {
                            beta = score;
                        }

                        if(beta <= alpha) { //aplha cutoff
                            break node;
                        }
                    }
                }

                bestScore = beta;

            }
        }

        return new Object[]{bestDirection, bestScore};
    }

}
