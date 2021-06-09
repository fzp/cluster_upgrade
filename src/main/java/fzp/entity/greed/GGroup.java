package fzp.entity.greed;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GGroup {
    HashMap<String, Integer> counters;
    List<GNode> nodes;

    public GGroup() {
        counters = new HashMap<>();
        nodes = new LinkedList<>();
    }

    public boolean check(GNode node) {
        for (GApplication app : node.applications) {
            if (counters.getOrDefault(app.appName, 0) + 1 > app.disruptionAllowed)
                return false;
        }
        return true;
    }

    public void add(GNode node) {
        for (GApplication app : node.applications) {
            counters.put(app.appName, counters.getOrDefault(app.appName, 0) + 1);
        }
        nodes.add(node);
    }

    @Override
    public String toString() {
        return nodes.stream().map(gNode -> gNode.nodeName).reduce((n1, n2) -> n1 + ", " + n2).orElse("");
    }
}