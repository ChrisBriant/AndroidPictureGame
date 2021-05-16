package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.adapters.GuessRecycler;
import chrisbriant.uk.picturegame.objects.Guess;
import chrisbriant.uk.picturegame.objects.GuessList;
import chrisbriant.uk.picturegame.services.GameServerConn;
import chrisbriant.uk.picturegame.views.DrawingView;

public class GameActivity extends AppCompatActivity {

    private GameServerConn conn;
    private String startPlayerId;
    private String startPlayerName;
    private String myId;
    private String word;
    private RecyclerView recyclerView;
    private GuessRecycler guessRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        conn = GameServerConn.getInstance(this);
        SharedPreferences sharedPrefs = conn.getSharedPrefs();
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.putString("wins","[]");

        myId = sharedPrefs.getString("id","");
        word = sharedPrefs.getString("word","");

        TextView gmRoomNameTxt = findViewById(R.id.gmRoomNameTxt);
        gmRoomNameTxt.setText(sharedPrefs.getString("current_room", ""));

        DrawingView gmCanvas = findViewById(R.id.gmCanvas);

        TextView gmStartPlayerText = findViewById(R.id.gmStartPlayerTxt);
        String startPlayerJson = sharedPrefs.getString("startPlayer", "");
        try {
            JSONObject playerJson = new JSONObject(startPlayerJson);
            startPlayerName = playerJson.getString("name");
            startPlayerId = playerJson.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Condtion where player is drawing or guessing

        //UI Setup
        TextView gmGuessTxt = findViewById(R.id.gmGuessTxt);
        TextView gmWordTxt = findViewById(R.id.gmWordTxt);
        TextView gmGuessEdt = findViewById(R.id.gmGuessEdt);
        Button gmGuessBtn = findViewById(R.id.gmGuessBtn);
        Button gmGiveUpBtn = findViewById(R.id.gmGiveUpBtn);

        gmGuessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("type", "guess");
                    payload.put("client_id", sharedPrefs.getString("id",""));
                    payload.put("game_id",sharedPrefs.getString("gameId",""));
                    payload.put("guess",gmGuessEdt.getText());
                    conn.send(payload.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        gmGiveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("type", "giveup");
                    payload.put("client_id", sharedPrefs.getString("id",""));
                    payload.put("game_id",sharedPrefs.getString("gameId",""));
                    conn.send(payload.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //SET UP RECYCLER
        recyclerView = findViewById(R.id.gmGuessRecyc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //GuessList guessList = GuessList.getInstance();
        ArrayList<Guess> guessList = new ArrayList<Guess>();
//        guessList.add(new Guess(
//                "hello","Gill"
//        ));
        guessRecycler = new GuessRecycler(this,guessList);
        recyclerView.setAdapter(guessRecycler);
        guessRecycler.notifyDataSetChanged();

        Context ctx = this;
//
//        guessList.setGuessListener(new GuessList.GuessListListener() {
//            @Override
//            public void onItemChanged() {
//                Log.d("GUESS LISTENER", "An item was changed");
//                //ArrayList<Guess> guessArrayList = guessList.getGuesses();
//                GuessList changedGuessList = GuessList.getInstance();
//                Log.d("GUESS ARRAY LIST", String.valueOf(changedGuessList.size()));
//                //guessList.add(new Guess("Hello", "hello"));
//                //guessRecycler = new GuessRecycler(ctx,guessList.getGuesses());
//                guessRecycler.setGuessList(changedGuessList);
//                recyclerView.setAdapter(guessRecycler);
//                guessRecycler.notifyDataSetChanged();
////                guessRecycler.setGuessList(guessList.getGuesses());
////                guessRecycler.notifyDataSetChanged();
//            }
//        });




        if (startPlayerId.equals(myId)) {
            //Setup for starting player
            gmCanvas.setLocked(false);
            gmStartPlayerText.setText("You are the start player, you go first!");
            gmWordTxt.setVisibility(View.VISIBLE);
            gmWordTxt.setText("The word is " + word);
            gmGuessTxt.setVisibility(View.GONE);
            gmGuessBtn.setVisibility(View.GONE);
            gmGiveUpBtn.setVisibility(View.GONE);
        } else {
            //Setup for receiving player
            gmCanvas.setLocked(true);
            gmStartPlayerText.setText(startPlayerName + " is the start player.");
            gmWordTxt.setVisibility(View.GONE);
            gmGuessTxt.setText("Guess what the user is drawing");
            gmGuessBtn.setVisibility(View.VISIBLE);
            gmGiveUpBtn.setVisibility(View.VISIBLE);
            //Set up listener for drawing on the canvas
            SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener;
            sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.d("Shared pref listener", "Shared prefs changed in game activity");
                    //JSONObject roomList;
                    if(!sharedPrefs.getString("picture", "").isEmpty()) {
                        try {
                            String pictureStr = sharedPrefs.getString("picture", "");
                            Log.d("PICTURE GAMEACTIVITY", pictureStr);
                            gmCanvas.drawPoints(pictureStr);

                        } catch (Exception e) {
                            Log.d("Error", e.getMessage());
                        }
                    }

                    Log.d("GUESS",sharedPrefs.getString("guess","") );


                    if(!sharedPrefs.getString("guess","").isEmpty()) {
                        Log.d("GUESS", "A guess is made");
                        Log.d("GUESS",sharedPrefs.getString("guess","") );
                        try{
                            Guess receivedGuess = new Guess(sharedPrefs.getString("guess",""));
                            guessList.add(receivedGuess);
                            Log.d("GUESS SIZE",String.valueOf(guessList.size()));
                            guessRecycler.setGuessList(guessList);
                            Log.d("RECYC",String.valueOf(guessRecycler.getItemCount()));
                            guessRecycler.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.getMessage());
                        }

                        //GuessList changedGuessList = GuessList.getInstance();
                        //Log.d("GUESS", String.valueOf(changedGuessList.size()));
                    }

                }
            };
            sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);


        }


    }


}