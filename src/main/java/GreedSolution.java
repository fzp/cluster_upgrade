import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import Entity.*;

public class GreedSolution {
    List<Group> solve(List<Node> nodes, List<Application> applications, List<DisruptionBudget> budgets) {
        List<Group> groups = new LinkedList<>();
        Set<Node> matchedNodes = new HashSet<>();
        GreedGroup g = new GreedGroup();
        for (int i = 0; i < nodes.size(); i++) {

        }
        return groups;
    }

    public static void main(String[] args){
        System.out.println("123");
    }
}
