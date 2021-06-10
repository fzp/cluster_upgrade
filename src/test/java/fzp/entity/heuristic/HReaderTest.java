package fzp.entity.heuristic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

class HReaderTest {
	@Test
	void read() throws IOException {
		HReader r = new HReader();
		List<HNode> nodes = r.read("testCase/test0.txt");
		int[] appNums = { 2, 3, 1 };
		assertEquals(nodes.size(), 3);
		for (int i = 0; i < nodes.size(); i++) {
			assertEquals(nodes.get(i).nodeName, "n" + (i + 1));
			assertEquals(nodes.get(i).applications.size(), appNums[i]);
		}
	}
}