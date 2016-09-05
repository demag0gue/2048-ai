package de.timgerdes.ai.algorithm;

import de.timgerdes.ai.control.Direction;

public class Solver implements Runnable {

    private GameAlgorithm algorithm;

    public Solver(GameAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void run() {
        algorithm.getRemote().startBrowser();
        algorithm.getRemote().setupWebSocket();
        algorithm.setup();
        algorithm.restart();

        long start = System.currentTimeMillis();
        int highestCell = 2;
        while(algorithm.getStatus().equals("running")) {
            int currentHighestCell = algorithm.getBoard().getHighestCell();
            if(currentHighestCell > highestCell) {
                System.out.println(currentHighestCell + " : " + (System.currentTimeMillis() - start) * 0.001);
                highestCell = currentHighestCell;
            }
            if(highestCell == 2048)
                break;

            Direction direction = algorithm.findBestMove();
            algorithm.move(direction);
        }
        algorithm.getRemote().stopBrowser();
    }

}
