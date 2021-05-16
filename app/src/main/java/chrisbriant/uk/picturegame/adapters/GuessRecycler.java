package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.util.Log;
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
    private ArrayList<Guess> guessList;
    private Context context;

    public GuessRecycler(Context ctx, ArrayList<Guess> guessList) {
        this.guessList = guessList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ON CREATE VIEW HOLDER", "Create view holder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guess_item,parent,false);
        return new GuessRecycler.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guess guessItem = guessList.get(position);

        holder.gitmGuess.setText(guessItem.getGuess());
        holder.gitmName.setText(guessItem.getName());
        Log.d("ROBOCOP", guessItem.getGuess());
    }

    @Override
    public int getItemCount() {
        return this.guessList.size();
    }

    public void setGuessList(ArrayList<Guess> guessList) {
        this.guessList = guessList;
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
