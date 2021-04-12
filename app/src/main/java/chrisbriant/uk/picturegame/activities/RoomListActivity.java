package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.adapters.RoomRecycler;
import chrisbriant.uk.picturegame.objects.RoomItem;

public class RoomListActivity extends AppCompatActivity {
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

            }
        };
        settings.registerOnSharedPreferenceChangeListener(sharedPrefListener);

    }
}