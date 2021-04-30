package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.services.GameServerConn;
import chrisbriant.uk.picturegame.views.DrawingView;

public class GameActivity extends AppCompatActivity {

    private GameServerConn conn;
    private String startPlayerId;
    private String startPlayerName;
    private String myId;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        conn = GameServerConn.getInstance(this);
        SharedPreferences sharedPrefs = conn.getSharedPrefs();

        myId = sharedPrefs.getString("yourid","");
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

        if (startPlayerId.equals(myId)) {
            //Setup for starting player
            gmStartPlayerText.setText("You are the start player, you go first!");
            gmWordTxt.setVisibility(View.VISIBLE);
            gmWordTxt.setText("The word is " + word);
            gmGuessTxt.setVisibility(View.GONE);
            gmGuessBtn.setVisibility(View.GONE);
            gmGiveUpBtn.setVisibility(View.GONE);
        } else {
            //Setup for receiving player
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
                    try {
                        String pictureStr = sharedPrefs.getString("picture", "");
                        Log.d("PICTURE GAMEACTIVITY",pictureStr);
                        gmCanvas.drawPoints(pictureStr);

                    } catch (Exception e) {
                        Log.d("Error", e.getMessage());
                    }

                }
            };
            sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);


        }


    }


}