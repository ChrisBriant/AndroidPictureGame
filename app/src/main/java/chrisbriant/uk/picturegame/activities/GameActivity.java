package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import chrisbriant.uk.picturegame.R;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}