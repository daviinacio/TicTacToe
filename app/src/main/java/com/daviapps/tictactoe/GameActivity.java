package com.daviapps.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.daviapps.tictactoe.domain.ScoreBoard;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        int difficulty = getIntent().getIntExtra("difficulty", 0x00);

        switch (difficulty) {
            case ScoreBoard.DIFF_PVP:
                Toast.makeText(this, R.string.main_player_vs_player, Toast.LENGTH_SHORT).show();
                break;

            case ScoreBoard.DIFF_EASY:
                Toast.makeText(this, R.string.easy, Toast.LENGTH_SHORT).show();
                break;

            case ScoreBoard.DIFF_MEDIUM:
                Toast.makeText(this, R.string.medium, Toast.LENGTH_SHORT).show();
                break;

            case ScoreBoard.DIFF_HARD:
                Toast.makeText(this, R.string.hard, Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, "[Error]: Unknown Difficulty", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return false;
        }
    }
}
