package chrisbriant.uk.picturegame.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.objects.GameOverItem;
import chrisbriant.uk.picturegame.objects.RoomItem;
import chrisbriant.uk.picturegame.services.GameServerConn;

public class GameOverRecycler extends RecyclerView.Adapter<GameOverRecycler.ViewHolder> {
    private Context context;
    private List<GameOverItem> gameOverList;
    private GameServerConn conn;

    public GameOverRecycler(Context context) {
        this.context = context;
        //this.gameOverList = gameOverList;
        //this.conn = conn;
        gameOverList = new ArrayList<GameOverItem>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_over_item,parent,false);
        return new GameOverRecycler.ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameOverItem go = gameOverList.get(position);

        holder.goitmName.setText(go.getName());
        holder.goitmScore.setText(String.valueOf(go.getScore()));
        holder.goitmReason.setText(go.getReason());
    }

    @Override
    public int getItemCount() {
        return gameOverList.size();
    }

    //Load the list from JSON string stored in shared prefs
    public void setGameOverList(SharedPreferences sharedPrefs) {
        HashMap<String,Integer> scores = new HashMap<String,Integer>();
        try {
            JSONArray wins = new JSONArray(sharedPrefs.getString("wins","[]"));
            Log.d("HASHMAP",wins.toString());
            //JSONObject win = new JSONObject();
            //Hash Map to calculate scores


            for(int i=0;i<wins.length();i++) {
                JSONObject win = new JSONObject(wins.getString(i));
                String name = win.getString("winnerName");
                Log.d("HASHMAP",name);
                //Might leave out reason
                //String reason = win.getString("reason");
                if(scores.containsKey(name)) {
                    //increment score
                    Integer score = scores.get(name);
                    scores.put(name,score++);
                } else {
                    //Add a new score entry
                    scores.put(name,1);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Todo: Convert hashmap into scores
        for(String key : scores.keySet()) {
            GameOverItem item = new GameOverItem(
                    key,
                    (int) scores.get(key),
                    ""
            );
            Log.d("HASHMAP",key);
            this.gameOverList.add(item);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView goitmName;
        private TextView goitmScore;
        private TextView goitmReason;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            goitmName = itemView.findViewById(R.id.goitmName);
            goitmScore = itemView.findViewById(R.id.goItmScore);
            goitmReason = itemView.findViewById(R.id.goItmReason);
        }
    }
}
