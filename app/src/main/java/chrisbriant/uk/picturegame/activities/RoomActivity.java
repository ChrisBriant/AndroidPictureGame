package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

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
import chrisbriant.uk.picturegame.objects.MemberList;
import chrisbriant.uk.picturegame.objects.RoomItem;
import chrisbriant.uk.picturegame.objects.RoomList;
import chrisbriant.uk.picturegame.services.GameServerConn;

public class RoomActivity extends AppCompatActivity {
    private TextView rmPlayerList;
    private Button rmStartGameBtn;
    private GameServerConn conn;
    ArrayList<String> roomMembers = new ArrayList<String>();
    String currentRoom;
    MemberList memberList = new MemberList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        conn = GameServerConn.getInstance(this);

        TextView rmRoomTitle = findViewById(R.id.rmRoomTitle);
        rmPlayerList = findViewById(R.id.rmPlayerList);
        rmStartGameBtn = findViewById(R.id.rmStartGameBtn);
        //SharedPreferences sharedPrefs = this.getSharedPreferences(
          //      getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences sharedPrefs = conn.getSharedPrefs();
        currentRoom = sharedPrefs.getString("current_room","");
        rmRoomTitle.setText(currentRoom);
        String roomListStr = sharedPrefs.getString("room_list", "");
        try {
            memberList.loadMembers(roomListStr, currentRoom);
            if(memberList != null) {
                Log.d("PLAYERS","Player list is not null");
                setPlayers(memberList.getMembers());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //RoomItem currentRoomItem = (RoomItem) roomList.get(currentRoom);
       // Log.d("ROOM ITEM", roomList.keySet().toString());

//        roomMembers = currentRoomItem.getPlayers();
//
        SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener;
        sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("Shared pref listener", "Shared prefs changed from room list activity");
                //JSONObject roomList;
                try {
                    String roomListStr = sharedPrefs.getString("room_list", "");
                    currentRoom = sharedPrefs.getString("current room","");
                    memberList.loadMembers(roomListStr, currentRoom);
                    //rooms = db.getRooms();
                    //RoomItem currentRoomItem = (RoomItem) roomList.get(currentRoom);
                    //Log.d("Shared pref listener", roomList.keySet().toString());
                    if(memberList != null) {
                        Log.d("PLAYERS","Player list is not null");
                        setPlayers(memberList.getMembers());
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }

            }
        };
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefListener);

        rmStartGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject payload = new JSONObject();
                try {
                    payload.put("type", "start_game");
                    payload.put("name", sharedPrefs.getString("current_room",""));
                    conn.send(payload.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AUTH", "Exception Fired");
                }
            }
        });

    }

    private void setPlayers(ArrayList<String> roomMembers) {
        rmPlayerList.setText("");
        Log.d("PLAYER",roomMembers.toString());

        for(int i=0;i<roomMembers.size();i++) {
            Log.d("PLAYER",roomMembers.get(i));
            rmPlayerList.append(roomMembers.get(i));
        }

        if(roomMembers.size()>1) {
            Log.d("PLAYER","Room size is more than one");
            rmStartGameBtn.setEnabled(true);
        } else {
            rmStartGameBtn.setEnabled(false);
        }
    }
}