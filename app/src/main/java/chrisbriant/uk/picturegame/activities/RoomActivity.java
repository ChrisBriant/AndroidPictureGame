package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import chrisbriant.uk.picturegame.R;

public class RoomActivity extends RoomListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        TextView rmRoomTitle = findViewById(R.id.rmRoomTitle);
        TextView rmPlayerList = findViewById(R.id.rmPlayerList);
        Button rmStartGameBtn = findViewById(R.id.rmStartGameBtn);

        rmRoomTitle.setText(sharedPrefs.getString("current room",""));
    }
}