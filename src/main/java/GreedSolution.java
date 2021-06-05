import java.util.*;

import Entity.*;

public class GreedSolution {
    List<Group> solve(List<Node> nodes, List<Application> applications) {
        List<Group> groups = new LinkedList<>();
        Set<Node> matchedNodes = new HashSet<>();
        while(matchedNodes.size() != nodes.size()){
            GreedGroup g = new GreedGroup();
            for (Node node: nodes) {
                if(matchedNodes.contains(node)) continue;
                if(g.check(node)){
                    matchedNodes.add(node);
                    g.add(node);
                }
            }
            groups.add(g);
        }
        return groups;
    }

    public static void main(String[] args){
    }
}
