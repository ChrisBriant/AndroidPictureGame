package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import chrisbriant.uk.picturegame.R;

public class RoomListActivity extends AppCompatActivity {

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
    }
}