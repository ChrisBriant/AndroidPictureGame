package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GuessList extends ArrayList<Guess>{
    private GuessListListener listener;
    // static variable single_instance of type Singleton
    private static GuessList single_instance = null;


    public interface GuessListListener {
        public void onItemChanged();
    }

    public GuessList() {
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

    @Override
    public boolean add(Guess guess) {
        Log.d("GUESS", "I am about to trigger an event");
        //listener.onItemChanged();
        return super.add(guess);
    }
}
