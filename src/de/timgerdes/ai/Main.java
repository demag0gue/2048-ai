package de.timgerdes.ai;

import de.timgerdes.ai.algorithm.AlphaBetaAlgorithm;
import de.timgerdes.ai.algorithm.MiniMaxAlgorithm;
import de.timgerdes.ai.algorithm.Solver;
import de.timgerdes.remote.RemoteChromeBrowserController;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("===== 2048 AI Solver =====");
        System.out.println("Press 1 to use Minimax");
        System.out.println("Press 2 to use AlphaBeta");

        Scanner scanner = new Scanner(System.in);
        int arg = scanner.nextInt();
        Solver solver = null;
        switch(arg) {
            case 1:
                solver = new Solver(new MiniMaxAlgorithm(new RemoteChromeBrowserController()));
                break;
            case 2:
                solver = new Solver(new AlphaBetaAlgorithm(new RemoteChromeBrowserController()));
                break;
            default:
                System.out.println("Unknown argument. Good bye!");
                System.exit(0);
        }

        solver.run();
    }
}
