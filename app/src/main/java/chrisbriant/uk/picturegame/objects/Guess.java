package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Guess {
    private String name;
    private String guess;
    private boolean correct;

    public Guess(String name, String guess, boolean correct) {
        this.name = name;
        this.guess = guess;
        this.correct = correct;
    }

    //Construct from a JSON
    public Guess(String JSONData) throws JSONException {
        JSONObject guessJSON = new JSONObject(JSONData);
        this.name = guessJSON.getString("name");
        this.guess = guessJSON.getString("guess");
        this.correct = guessJSON.getBoolean("correct");
    }

    public String getName() {
        return name;
    }

    public String getGuess() {
        return guess;
    }

    public JSONObject toJSON() {
        JSONObject jsonGuess = new JSONObject();
        try {
            jsonGuess.put("name", this.name);
            jsonGuess.put("guess", this.guess);
            jsonGuess.put("correct", this.correct);
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return jsonGuess;
    }
}
