package fzp;

import fzp.entity.greed.GGroup;
import fzp.entity.heuristic.HGroup;
import fzp.solution.GreedSolution;
import fzp.solution.HeuristicProSolution;
import fzp.solution.HeuristicSolution;
import fzp.utils.BestEstimation;

import java.io.IOException;
import java.util.List;

public class Batch {
    public static void main(String args[]) throws IOException {
        for (int i = 0; i <= 40; i++) {
            String p = "testCase/test" + i + ".txt";
            List<GGroup> gg = new GreedSolution().solve(p);
            List<HGroup> hg = new HeuristicSolution().solve(p);
            List<HGroup> hpg = new HeuristicProSolution().solve(p);
            int best = new BestEstimation().estimate(p);
            System.out.println(p + ": " + gg.size() + "," + hg.size() + "," + hpg.size() + "," + best);
        }
    }
}
