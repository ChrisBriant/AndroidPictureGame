package chrisbriant.uk.picturegame.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chrisbriant.uk.picturegame.R;
import chrisbriant.uk.picturegame.adapters.GameOverRecycler;
import chrisbriant.uk.picturegame.adapters.RoomRecycler;
import chrisbriant.uk.picturegame.services.GameServerConn;

public class GameOverActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private GameOverRecycler gameOverRecycler;
    private GameServerConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        conn = GameServerConn.getInstance(this);
        SharedPreferences sharedPrefs = conn.getSharedPrefs();

        recyclerView = findViewById(R.id.gameOverRecyc);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameOverRecycler = new GameOverRecycler(this);
        recyclerView.setAdapter(gameOverRecycler);
        gameOverRecycler.setGameOverList(sharedPrefs);
        gameOverRecycler.notifyDataSetChanged();

        Button gameOverBtn = findViewById(R.id.gameOverBtn);

        gameOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = v.getContext();
                Intent intent = new Intent(ctx, RoomListActivity.class);
                ctx.startActivity(intent);
            }
        });

    }
}