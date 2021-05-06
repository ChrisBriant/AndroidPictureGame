package chrisbriant.uk.picturegame.objects;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class GuessList_OLD implements List {
    private ArrayList<Guess> guesses;
    private GuessListListener listener;
    // static variable single_instance of type Singleton
    private static GuessList_OLD single_instance = null;

    public interface GuessListListener {
        public void onItemChanged(ArrayList<Guess> guesses);
    }

    public GuessList_OLD() {
        this.guesses = null;
        this.listener = null;
    }

    public void setGuessListener(GuessListListener listener) {
        this.listener = listener;
    }

    public static GuessList_OLD getInstance() {
        if (single_instance == null)
            single_instance = new GuessList_OLD();

        return single_instance;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public boolean add(Object o) {
        boolean success = guesses.add((Guess) o);
        if(success) {
            Log.d("GUESS", "I am about to trigger an event");
            listener.onItemChanged(this.guesses);
        }
        return success;
    }

    @Override
    public boolean remove(@Nullable Object o) {
        boolean success =  guesses.remove((Guess) o);
        if(success) {
            listener.onItemChanged(this.guesses);
        }
        return success;
    }

    @Override
    public boolean addAll(@NonNull Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public Object set(int index, Object element) {
        return null;
    }

    @Override
    public void add(int index, Object element) {

    }

    @Override
    public Object remove(int index) {
        return null;
    }

    @Override
    public int indexOf(@Nullable Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(@Nullable Object o) {
        return 0;
    }

    @NonNull
    @Override
    public ListIterator listIterator() {
        return null;
    }

    @NonNull
    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @NonNull
    @Override
    public List subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public boolean retainAll(@NonNull Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(@NonNull Collection c) {
        return false;
    }

    @Override
    public boolean containsAll(@NonNull Collection c) {
        return false;
    }

    @NonNull
    @Override
    public Object[] toArray(@NonNull Object[] a) {
        return new Object[0];
    }
}
