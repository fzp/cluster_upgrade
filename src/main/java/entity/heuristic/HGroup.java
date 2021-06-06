package entity.heuristic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HGroup {

    HashMap<String, Integer> counters;
    List<HNode> nodes;

    public HGroup() {
        counters = new HashMap<>();
        nodes = new LinkedList<>();
    }

    public void add(HNode node) {
        for (HApplication app : node.applications) {
            counters.put(app.appName, counters.getOrDefault(app.appName, 0) + 1);
        }
        nodes.add(node);
    }

    public boolean check(HNode node) {
        for (HApplication app : node.applications) {
            if (counters.getOrDefault(app.appName, 0) + 1 > app.disruptionAllowed)
                return false;
        }
        return true;
    }
}
