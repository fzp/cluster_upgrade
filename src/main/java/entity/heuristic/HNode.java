package entity.heuristic;

import java.util.Set;

public class HNode {

    int[] values;
    public String nodeName;
    public Set<HApplication> applications;

    public HNode(String nodeName, Set<HApplication> applications) {
        this.nodeName = nodeName;
        this.applications = applications;

        values = new int[201];
        setValues();
    }

    public void setValues() {
        for (HApplication app : applications) {
            values[app.getSteps()]++;
        }
    }

    public boolean compareTo(HNode other) {
        for (int i = 200; i >= 0; i--) {
            if (values[i] == other.values[i]) continue;
            return values[i] > other.values[i];
        }
        return true;
    }
}
