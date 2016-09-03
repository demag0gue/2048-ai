package de.timgerdes.ai.algorithm;

import de.timgerdes.ai.algorithm.GameAlgorithm;
import de.timgerdes.ai.control.Direction;

public class Solver extends Thread {

    private GameAlgorithm algorithm;

    public Solver(GameAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void run() {
        algorithm.getRemote().startBrowser();
        algorithm.getRemote().setupWebSocket();
        algorithm.setup();
        algorithm.restart();

        while(true) {
            System.out.println("Searching for move...");
            Direction direction = algorithm.findBestMove();
            System.out.println("Moving " + direction + "...");
            algorithm.move(direction);

            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
