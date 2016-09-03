package de.timgerdes.ai;

import de.timgerdes.ai.algorithm.AlphaBetaAlgorithm;
import de.timgerdes.ai.algorithm.Solver;
import de.timgerdes.remote.RemoteChromeBrowserController;

public class Main {

    public static void main(String[] args) {
        Solver solver = new Solver(new AlphaBetaAlgorithm(new RemoteChromeBrowserController()));
        solver.run();
    }
}
