package fzp.entity.greed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GReader {
    public List<GNode> read(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        int nodeNumber = Integer.parseInt(in.readLine());
        int appNumber = Integer.parseInt(in.readLine());
        List<GApplication> apps = new ArrayList<>();
        for (int i = 1; i <= appNumber; i++) {
            String[] appInfo = in.readLine().split(",");
            apps.add(new GApplication("a" + i, Integer.parseInt(appInfo[1])));
        }

        List<GNode> nodes = new ArrayList<>();
        for (int i = 1; i <= nodeNumber; i++) {
            String[] appIndexes = in.readLine().split(",");
            Set<GApplication> appSet = new HashSet<>();
            for (String appIndex : appIndexes) {
                if (!appIndex.isEmpty())
                    appSet.add(apps.get(Integer.parseInt(appIndex) - 1));
            }
            nodes.add(new GNode("n" + i, appSet));
        }
        in.close();
        return nodes;
    }
}
