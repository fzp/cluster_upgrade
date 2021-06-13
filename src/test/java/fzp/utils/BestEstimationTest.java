package fzp.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BestEstimationTest {
    @Test
    void file1() throws IOException {
        BestEstimation b = new BestEstimation();
        int ans = b.estimate("testCase/test0.txt");
        assertEquals(ans, 2);
    }
}