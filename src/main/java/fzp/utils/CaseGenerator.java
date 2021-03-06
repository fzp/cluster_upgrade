package fzp.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CaseGenerator {
    void generate(int nodeNumber, int appNumber, int naMax, int anMin, int anMax, int disMin, String fileName, boolean special) throws IOException {
        Random random = new Random();
        int allApps = nodeNumber * naMax - appNumber * anMin;
        int anRest = anMax - anMin;
        ArrayList<Integer>[] nas = new ArrayList[nodeNumber];
        for (int i = 0; i < nas.length; i++) {
            nas[i] = new ArrayList<>();
            if (special) {
                nas[i].add(1);
            }
        }
        int[] appSizes = new int[appNumber];
        for (int app = 1; app <= appNumber; app++) {
            if (app == 1 && special) {
                appSizes[0] = nodeNumber;
                allApps += anMin - nodeNumber;
                continue;
            }
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
            if (i == 0 && special) {
                disruptions[0] = 5;
                continue;
            }
            if (appSizes[i] >= disMin) {
                disruptions[i] = random.nextInt(appSizes[i] - disMin + 1) + disMin;
            } else {
                disruptions[i] = random.nextInt(appSizes[i]) + 1;
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(String.valueOf(nodeNumber));
        out.newLine();
        out.write(String.valueOf(appNumber));
        out.newLine();
        for (int i = 0; i < appNumber; i++) {
            out.write(appSizes[i] + "," + disruptions[i]);
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

        for (int i = 2; i <= 10; i++) {
            gen.generate(50, 40, 5, 1, i, 1, "testCase/test" + i + ".txt", false);
        }

        for (int i = 11; i <= 30; i++) {
            gen.generate(5000, 4000, 50, 10, (i - 10) * 10, 1, "testCase/test" + i + ".txt", false);
        }

        for (int i = 31; i <= 40; i++) {
            gen.generate(50, 40, 50, 15, 30, 2, "testCase/test" + i + ".txt", true);
        }
    }
}
