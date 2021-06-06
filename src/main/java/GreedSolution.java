import entity.greed.GNode;
import entity.greed.GGroup;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GreedSolution {
    List<GGroup> solve(List<GNode> nodes) {
        List<GGroup> groups = new LinkedList<>();
        Set<GNode> matchedNodes = new HashSet<>();
        while(matchedNodes.size() != nodes.size()){
            GGroup g = new GGroup();
            for (GNode node: nodes) {
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
