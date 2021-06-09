package fzp.entity.heuristic;

public class HApplication {

    public int used;
    public int numbers;
    public String appName;
    public Integer disruptionAllowed;

    public HApplication(String appName, int disruptionAllowed, int numbers) {
        this.appName = appName;
        this.disruptionAllowed = disruptionAllowed;
        this.used = 0;
        this.numbers = numbers;
    }

    public int getSteps() {
        return (numbers - used + +disruptionAllowed - 1) / disruptionAllowed;
    }
}
