package fzp.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BestEstimation {
    public int estimate(String path) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(path));
        in.readLine();
        int appNumber = Integer.parseInt(in.readLine());
        int best = 0;
        for (int i = 1; i <= appNumber; i++) {
            String[] appInfo = in.readLine().split(",");
            int all = Integer.parseInt(appInfo[0]);
            int disruption = Integer.parseInt(appInfo[1]);
            best = Math.max(best, (all + disruption - 1) / disruption);
        }
        return best;
    }
}
