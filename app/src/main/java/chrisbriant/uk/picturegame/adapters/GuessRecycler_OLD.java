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

public class GuessRecycler_OLD extends RecyclerView.Adapter<GuessRecycler_OLD.ViewHolder> {
    private ArrayList<Guess> guessList;
    private Context context;

    public GuessRecycler_OLD(Context ctx, ArrayList<Guess> guessList) {
        this.guessList = guessList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guess_item,parent,false);
        return new GuessRecycler_OLD.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Guess guessItem = guessList.get(position);

        holder.gitmGuess.setText(guessItem.getGuess());
        holder.gitmName.setText(guessItem.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
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
