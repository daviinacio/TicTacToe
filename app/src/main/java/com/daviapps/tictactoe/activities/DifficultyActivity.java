package com.daviapps.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.daviapps.tictactoe.R;
import com.daviapps.tictactoe.domain.ScoreBoard;
import com.daviapps.tictactoe.util.Admob;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void chooseDifficult(View v){
        Intent intent = new Intent(DifficultyActivity.this, GameActivity.class);

        switch (v.getId()){
            case R.id.btn_diff_easy:
                intent.putExtra("difficulty", ScoreBoard.DIFF_EASY);
                break;

            case R.id.btn_diff_medium:
                intent.putExtra("difficulty", ScoreBoard.DIFF_MEDIUM);
                break;

            case R.id.btn_diff_hard:
                intent.putExtra("difficulty", ScoreBoard.DIFF_HARD);
                break;
        }


        if(intent.hasExtra("difficulty")){
            startActivity(intent);
            super.finish();
        }
    }

    public void goBack(View v){
        this.finish();
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
