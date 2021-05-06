package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GuessList  {
    private ArrayList<Guess> guesses;
    private GuessListListener listener;
    // static variable single_instance of type Singleton
    private static GuessList single_instance = null;


    public interface GuessListListener {
        public void onItemChanged(ArrayList<Guess> guesses);
    }

    public GuessList() {
        this.guesses = new ArrayList<Guess>();
        this.listener = null;
    }

    public void setGuessListener(GuessListListener listener) {
        this.listener = listener;
    }

    public static GuessList getInstance() {
        if (single_instance == null)
            single_instance = new GuessList();

        return single_instance;
    }


    public boolean add(Object o) {
        Log.d("GUESS", "Are we there yet?");
        boolean success = guesses.add((Guess) o);
        if(success) {
            Log.d("GUESS", "I am about to trigger an event");
            listener.onItemChanged(this.guesses);
        }
        return success;
    }


    public boolean remove(@Nullable Object o) {
        boolean success =  guesses.remove((Guess) o);
        if(success) {
            listener.onItemChanged(this.guesses);
        }
        return success;
    }

    public int size() {
        if (this.guesses != null) {
            return this.guesses.size();
        } else {
            return 0;
        }
    }

    public Object get(int position) {
        return this.guesses.get(position);
    }


}
