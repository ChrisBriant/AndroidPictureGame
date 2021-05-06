package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.objects.Guess;
import chrisbriant.uk.picturegame.objects.GuessList;

public class GuessRecycler extends RecyclerView.Adapter<GuessRecycler.ViewHolder> {
    private GuessList guessList;
    private Context context;

    public GuessRecycler(Context ctx, GuessList guessList) {
        this.guessList = guessList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guess_item,parent,false);
        return new GuessRecycler.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guess guessItem = (Guess) guessList.get(position);

        holder.gitmGuess.setText(guessItem.getGuess());
        holder.gitmName.setText(guessItem.getName());
    }

    @Override
    public int getItemCount() {
        return guessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView gitmName;
        private TextView gitmGuess;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            gitmName = itemView.findViewById(R.id.gitmName);
            gitmGuess = itemView.findViewById(R.id.gitmGuess);
        }
    }
}
