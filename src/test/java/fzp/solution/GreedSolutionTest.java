package fzp.solution;

import fzp.entity.greed.GApplication;
import fzp.entity.greed.GGroup;
import fzp.entity.greed.GNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GreedSolutionTest {

    GreedSolution solution = new GreedSolution();

    @Test
    void good() {
        GApplication a1 = new GApplication("a1", 1);
        GApplication a2 = new GApplication("a2", 1);
        GApplication a3 = new GApplication("a3", 1);
        GNode n1 = new GNode("n1", new HashSet<>(Arrays.asList(a1, a2)));
        GNode n2 = new GNode("n2", new HashSet<>(Arrays.asList(a1, a2, a3)));
        GNode n3 = new GNode("n3", new HashSet<>(Arrays.asList(a3)));
        List<GGroup> groups = solution.solve(Arrays.asList(n1, n2, n3));
        assertEquals(groups.size(), 2);
    }

    @Test
    void bad() {
        GApplication a1 = new GApplication("a1", 1);
        GApplication a2 = new GApplication("a2", 2);
        GNode n1 = new GNode("n1", new HashSet<>(Arrays.asList(a2)));
        GNode n2 = new GNode("n2", new HashSet<>(Arrays.asList(a2)));
        GNode n3 = new GNode("n3", new HashSet<>(Arrays.asList(a1, a2)));
        GNode n4 = new GNode("n4", new HashSet<>(Arrays.asList(a1, a2)));
        List<GGroup> groups = solution.solve(Arrays.asList(n1, n2, n3, n4));
        assertEquals(groups.size(), 3);
    }
}