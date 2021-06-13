package fzp;

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
        if (algorithm.equals("0")) {
            GreedSolution solution = new GreedSolution();
            ans = solution.solve(filePath);
        } else if (algorithm.equals("1")) {
            HeuristicSolution solution = new HeuristicSolution();
            ans = solution.solve(filePath);
        } else {
            HeuristicProSolution solution = new HeuristicProSolution();
            ans = solution.solve(filePath);
        }
        System.out.println(ans.size());
        /*
        for (Object an : ans) {
            System.out.println(an.toString());
        }
         */
    }
}
