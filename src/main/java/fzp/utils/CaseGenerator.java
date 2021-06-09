package fzp.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CaseGenerator {
    void generate(int nodeNumber, int appNumber, int naMax, int anMin, int anMax, String fileName) throws IOException {
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
            allApps -= appSizes[app - 1];
            appSizes[app - 1] += anMin;
            List<Integer> pool = new ArrayList<>();
            for (int i = 0; i < nodeNumber; i++) {
                if (nas[i].size() < naMax) {
                    pool.add(i);
                }
            }
            if (appSizes[app - 1] > pool.size()) {
                appNumber = app - 1;
                break;
            }
            for (int i = 0; i < appSizes[app - 1]; i++) {
                int index = random.nextInt(pool.size());
                nas[pool.get(index)].add(app);
                pool.remove(index);
            }
        }

        int[] disruptions = new int[appNumber];
        for (int i = 0; i < appNumber; i++) {
            disruptions[i] = random.nextInt(appSizes[i]) + 1;
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(String.valueOf(nodeNumber));
        out.newLine();
        out.write(String.valueOf(appNumber));
        out.newLine();
        for (int i = 0; i < appNumber; i++) {
            out.write(appSizes[i]+","+disruptions[i]);
            out.newLine();
        }
        for (List<Integer> na : nas) {
            List<String> l = na.stream().map(String::valueOf).collect(Collectors.toList());
            out.write(String.join(",", l));
            out.newLine();
        }
        out.close();
    }

    public static void main(String[] args) throws IOException {
        CaseGenerator gen = new CaseGenerator();
        gen.generate(4, 3, 3, 2, 4, "testCase/test1.txt");
        for (int i = 2; i <= 10; i++) {
            gen.generate(500, 400, 5, 1, 20, "testCase/test" + i + ".txt");
        }
        for (int i = 11; i <= 20; i++) {
            gen.generate(5000, 4000, 50, 10, 200, "testCase/test" + i + ".txt");
        }
    }
}
