package fzp;

import fzp.entity.greed.GReader;
import fzp.entity.heuristic.HReader;
import fzp.solution.GreedSolution;
import fzp.solution.HeuristicProSolution;
import fzp.solution.HeuristicSolution;

import java.io.IOException;
import java.util.List;

public class Application {
    public static void main(String[] args) throws IOException {
        String algorithm = args[0];
        String filePath = args[1];
        List<?> ans;
        if(algorithm.equals("0")){
            GReader reader = new GReader();
            GreedSolution solution = new GreedSolution();
            ans = solution.solve(reader.read(filePath));
        } else if(algorithm.equals("1")){
            HReader reader = new HReader();
            HeuristicSolution solution = new HeuristicSolution();
            ans = solution.solve(reader.read(filePath));
        } else {
            HReader reader = new HReader();
            HeuristicProSolution solution = new HeuristicProSolution();
            ans = solution.solve(reader.read(filePath));
        }
        System.out.println(ans.size());
        for (Object an : ans) {
            System.out.println(an.toString());
        }
    }
}
