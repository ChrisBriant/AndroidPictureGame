package chrisbriant.uk.picturegame.objects;

public class Guess {
    private String name;
    private String guess;

    public Guess(String name, String guess) {
        this.name = name;
        this.guess = guess;
    }

    public String getName() {
        return name;
    }

    public String getGuess() {
        return guess;
    }
}
