package de.timgerdes.ai.algorithm;

import de.timgerdes.ai.control.Board;
import de.timgerdes.ai.control.Direction;
import de.timgerdes.ai.control.GameController;
import de.timgerdes.remote.RemoteBrowserController;

public abstract class GameAlgorithm extends GameController {

    public GameAlgorithm(RemoteBrowserController r) {
        super(r);
    }

    public abstract Direction findBestMove();

    protected int heuristic(Board board) {
        return Math.max((int) (board.getScore() + Math.log(board.getScore()) * board.countEmptyCells() - clusteringScore(board)), Math.min(board.getScore(), 1));
    }

    private int clusteringScore(Board board) {
        int clusteringScore=0;

        int[] nb = {-1,0,1};
        int[][] array = board.toArray();

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {

                if(array[i][j] == 0)
                    continue;

                int neighbors = 0;
                int sum = 0;
                for(int k : nb) {
                    int x = i + k;

                    if(x < 0 || x >= 4)
                        continue;

                    for(int l : nb) {
                        int y = j + l;

                        if(y < 0 || y >= 4)
                            continue;

                        if(array[x][y] > 0) {
                            neighbors++;
                            sum += Math.abs(array[i][j] - array[x][y]);
                        }
                    }
                }

                clusteringScore += sum/neighbors;
            }
        }

        return clusteringScore;
    }

}
