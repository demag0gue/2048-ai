package de.timgerdes.ai.algorithm;

import de.timgerdes.ai.control.Board;
import de.timgerdes.ai.control.Coordinate;
import de.timgerdes.ai.control.Direction;
import de.timgerdes.remote.RemoteBrowserController;

import java.util.ArrayList;

public class MiniMaxAlgorithm extends GameAlgorithm {

    public MiniMaxAlgorithm(RemoteBrowserController r) {
        super(r);
    }

    @Override
    public Direction findBestMove() {
        Direction direction = (Direction) find(getBoard(), 7, true)[0];
        return (direction != null) ? direction : findBestMove();
    }

    private Object[] find(Board board, int depth, boolean player) {
        Direction bestDirection = null;
        int bestScore;

        if(depth == 0) {
            bestScore = heuristic(board);
        } else {
            if(player) {
                bestScore = Integer.MIN_VALUE;

                for(Direction direction : Direction.values()) {
                    Board workBoard = board.move(direction);
                    int points = workBoard.getScore() - board.getScore();

                    if(points == 0 && board.equals(workBoard)) //nothing happened..
                        continue;

                    int score = (int) find(workBoard, depth - 1, false)[1];

                    if(score > bestScore) {
                        bestDirection = direction;
                        bestScore = score;
                    }
                }
            } else {
                bestScore = Integer.MAX_VALUE;
                ArrayList<Coordinate> emptyCells = board.getEmptyCells();
                int[] values = {2, 4};
                int x,y;

                if(emptyCells.isEmpty())
                    bestScore = 0;

                for(Coordinate cell : emptyCells) {
                    x = cell.getX();
                    y = cell.getY();

                    for(int value : values) {
                        Board workBoard = board.clone();
                        workBoard.setCell(x, y, value);

                        int score = (int) find(workBoard, depth - 1, true)[1];

                        if(score < bestScore) {
                            bestScore = score;
                        }
                    }
                }
            }
        }

        return new Object[]{bestDirection, bestScore};
    }

}
