
import entity.heuristic.HApplication;
import entity.heuristic.HGroup;
import entity.heuristic.HNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Heuristic1SolutionTest {

    Heuristic1Solution solution = new Heuristic1Solution();

    @Test
    void good() {
        HApplication a1 = new HApplication("a1", 1, 2);
        HApplication a2 = new HApplication("a2", 1, 2);
        HApplication a3 = new HApplication("a3", 1, 2);
        HNode n1 = new HNode("n1", new HashSet<>(Arrays.asList(a1, a2)));
        HNode n2 = new HNode("n2", new HashSet<>(Arrays.asList(a1, a2, a3)));
        HNode n3 = new HNode("n3", new HashSet<>(Arrays.asList(a3)));
        List<HGroup> groups = solution.solve(Arrays.asList(n1, n2, n3));
        assertEquals(groups.size(), 2);
    }

    @Test
    void bad() {
        HApplication a1 = new HApplication("a1", 1, 2);
        HApplication a2 = new HApplication("a2", 2, 4);
        HNode n1 = new HNode("n1", new HashSet<>(Arrays.asList(a2)));
        HNode n2 = new HNode("n2", new HashSet<>(Arrays.asList(a2)));
        HNode n3 = new HNode("n3", new HashSet<>(Arrays.asList(a1, a2)));
        HNode n4 = new HNode("n4", new HashSet<>(Arrays.asList(a1, a2)));
        List<HGroup> groups = solution.solve(Arrays.asList(n1, n2, n3, n4));
        assertEquals(groups.size(), 2);
    }
}