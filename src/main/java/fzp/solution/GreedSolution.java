package fzp.solution;

import fzp.entity.greed.GNode;
import fzp.entity.greed.GGroup;
import fzp.entity.greed.GReader;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GreedSolution {
    public List<GGroup> solve(String path) throws IOException {
        GReader reader = new GReader();
        return solve(reader.read(path));
    }

    public List<GGroup> solve(List<GNode> nodes) {
        List<GGroup> groups = new LinkedList<>();
        Set<GNode> matchedNodes = new HashSet<>();
        while (matchedNodes.size() != nodes.size()) {
            GGroup g = new GGroup();
            for (GNode node : nodes) {
                if (matchedNodes.contains(node)) continue;
                if (g.check(node)) {
                    matchedNodes.add(node);
                    g.add(node);
                }
            }
            groups.add(g);
        }
        return groups;
    }
}
