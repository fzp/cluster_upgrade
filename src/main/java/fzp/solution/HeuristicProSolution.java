package fzp.solution;

import fzp.entity.heuristic.HGroup;
import fzp.entity.heuristic.HNode;
import fzp.entity.heuristic.HReader;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class HeuristicProSolution {
    public List<HGroup> solve(String path) throws IOException {
        HReader reader = new HReader();
        return solve(reader.read(path));
    }

    public List<HGroup> solve(List<HNode> nodes) {
        List<HGroup> groups = new LinkedList<>();
        List<HNode> unused = new LinkedList<>(nodes);
        while (unused.size() != 0) {
            List<HNode> rest = new LinkedList<>();
            PriorityQueue<HNode> q = new PriorityQueue<>((HNode h1, HNode h2) -> h1.compareTo(h2) ? -1 : 1);
            HGroup group = new HGroup();
            unused.forEach(node -> {
                node.setValues();
                q.add(node);
            });
            while (!q.isEmpty()) {
                HNode node = q.poll();
                node.setValues();
                if (!q.isEmpty() && !node.compareTo(q.peek())) {
                    q.add(node);
                    continue;
                }
                if (group.check(node)) {
                    group.add(node);
                } else {
                    rest.add(node);
                }
            }
            groups.add(group);
            unused = rest;
        }
        return groups;
    }
}
