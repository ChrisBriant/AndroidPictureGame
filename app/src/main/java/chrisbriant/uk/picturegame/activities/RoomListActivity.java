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
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.adapters.RoomRecycler;
import chrisbriant.uk.picturegame.objects.RoomItem;
import chrisbriant.uk.picturegame.services.PictureEvents;

public class RoomListActivity extends MainActivity {
    private RecyclerView recyclerView;
    private RoomRecycler roomRecycler;
    private ArrayList<RoomItem> rooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        //Get the shared preferences
        Context ctx = this.getApplicationContext();
        SharedPreferences sharedPrefs = ctx.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        TextView rmLstNameTxt = findViewById(R.id.rmLstNameTxt);
        rmLstNameTxt.setText("Welcome " + sharedPrefs.getString("name",""));

        EditText rmNewRoomEdt = findViewById(R.id.rmNewRoomEdt);
        Button rmAddRoomBtn = findViewById(R.id.rmAddRoomBtn);
        rmAddRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("type", "create_room");
                    payload.put("client_id", sharedPrefs.getString("id",""));
                    payload.put("name",rmNewRoomEdt.getText());
                    conn.send(payload.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AUTH", "Exception Fired");
                }
            }
        });


        //SET UP RECYCLER
        recyclerView = findViewById(R.id.rmRoomListRecyc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rooms = new ArrayList<>();


        //Get items from database
        Log.d("no items", String.valueOf(rooms.size()));

        roomRecycler = new RoomRecycler(this,rooms);
        recyclerView.setAdapter(roomRecycler);
        roomRecycler.notifyDataSetChanged();

        //Shared preferences listener to update rooms
        //https://stackoverflow.com/questions/22546938/sharedpreferences-android-listener
        SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener;
        sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("Shared pref listener", "Shared prefs changed from room list activity");
                JSONObject roomList;
                try {
                    Log.d("JSON STRING", sharedPrefs.getString("rooms", ""));
                    roomList = new JSONObject(sharedPrefs.getString("rooms", ""));
                    //Log.d("HERE IS THE JSON", roomList.toString());
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }

            }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);

    }
}