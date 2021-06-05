package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GreedGroup implements Group {
    HashMap<String, Integer> counters;
    List<Node> nodes;
    public GreedGroup() {
        counters = new HashMap<>();
        nodes = new LinkedList<>();
    }

    public boolean check(Node node) {
        for (Application app : node.applications) {
            if (counters.getOrDefault(app.appName, 0) + 1 > app.disruptionAllowed)
                return false;
        }
        return true;
    }

    @Override
    public void add(Node node) {
        for (Application app : node.applications) {
            counters.put(app.appName, counters.getOrDefault(app.appName, 0) + 1);
        }
        nodes.add(node);
    }
}
