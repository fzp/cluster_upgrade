package fzp.entity.heuristic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HReader {
    public List<HNode> read(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        int nodeNumber = Integer.parseInt(in.readLine());
        int appNumber = Integer.parseInt(in.readLine());
        List<HApplication> apps = new ArrayList<>();
        for (int i = 1; i <= appNumber; i++) {
            String[] appInfo = in.readLine().split(",");
            apps.add(new HApplication("a" + i, Integer.parseInt(appInfo[1]), Integer.parseInt(appInfo[0])));
        }

        List<HNode> nodes = new ArrayList<>();
        for (int i = 1; i <= nodeNumber; i++) {
            String[] appIndexes = in.readLine().split(",");
            Set<HApplication> appSet = new HashSet<>();
            for (String appIndex : appIndexes) {
                if (!appIndex.isEmpty())
                    appSet.add(apps.get(Integer.parseInt(appIndex) - 1));
            }
            nodes.add(new HNode("n" + i, appSet));
        }
        in.close();
        return nodes;
    }
}
