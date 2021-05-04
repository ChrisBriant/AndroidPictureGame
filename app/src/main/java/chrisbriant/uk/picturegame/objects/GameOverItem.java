package chrisbriant.uk.picturegame.objects;

public class GameOverItem {
    private String name;
    private int score;
    private String reason;

    public GameOverItem(String name, int score, String reason) {
        this.name = name;
        this.score = score;
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getReason() {
        return reason;
    }
}
