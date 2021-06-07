package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CaseGenerator {
    void generate(int nodeNumber, int appNumber, int naMax, int anMin, int anMax) throws IOException {
        Random random = new Random();
        int allApps = nodeNumber * naMax - appNumber * anMin;
        int anRest = anMax - anMin;
        ArrayList<Integer>[] nas = new ArrayList[nodeNumber];
        for (int i = 0; i < nas.length; i++) {
            nas[i] = new ArrayList<>();
        }
        int[] appSizes = new int[appNumber];
        for (int app = 1; app <= appNumber; app++) {
            if (allApps <= anRest) {
                appSizes[app - 1] = allApps;
            } else {
                appSizes[app - 1] = random.nextInt(anRest + 1);
            }
            appSizes[app - 1] += anMin;
            allApps -= appSizes[app - 1];
            HashSet<Integer> nodes = new HashSet<>();
            for (int i = 0; i < appSizes[app - 1]; i++) {
                int node = random.nextInt(nodeNumber);
                while (nodes.contains(node) || nas[node].size() >= naMax) {
                    node = random.nextInt(nodeNumber);
                }
                nodes.add(node);
            }
            for (int node : nodes) {
                nas[node].add(app);
            }
        }

        int[] disruptions = new int[appNumber];
        for (int i = 0; i < appNumber; i++) {
            disruptions[i] = random.nextInt(appSizes[i]) + 1;
        }
        BufferedWriter out = new BufferedWriter(new FileWriter("test1.txt"));
        out.write(String.valueOf(nodeNumber));
        out.newLine();
        out.write(String.valueOf(appNumber));
        out.newLine();
        for (List<Integer> na : nas) {
            List<String> l = na.stream().map(String::valueOf).collect(Collectors.toList());
            out.write(String.join(",", l));
            out.newLine();
        }

        for (int i = 0; i < appNumber; i++) {
            out.write(String.valueOf(disruptions[i]));
            out.newLine();
        }
        out.close();
    }

    public static void main(String[] args) throws IOException {
        CaseGenerator gen = new CaseGenerator();
        gen.generate(4, 3, 3, 2, 4);
    }
}
