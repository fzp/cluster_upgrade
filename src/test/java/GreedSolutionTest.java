import Entity.Application;
import Entity.Group;
import Entity.Node;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreedSolutionTest {

    GreedSolution solution = new GreedSolution();

    @Test
    void solve() {
        Application a1 = new Application("a1", 1);
        Application a2 = new Application("a2", 1);
        Application a3 = new Application("a3", 1);
        Node n1 = new Node("n1", Arrays.asList(a1, a2));
        Node n2 = new Node("n2", Arrays.asList(a1, a2, a3));
        Node n3 = new Node("n3", Arrays.asList(a3));
        List<Group> groups = solution.solve(Arrays.asList(n1, n2, n3), null);
        assertEquals(groups.size(), 2);
    }
}