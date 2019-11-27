package com.daviapps.tictactop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.daviapps.tictactop.database.ScoreboardDAO;
import com.daviapps.tictactop.domain.ScoreBoard;

public class MainActivity extends AppCompatActivity {
    ScoreboardDAO scoreDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.scoreDB = new ScoreboardDAO(this);

        ScoreBoard score = new ScoreBoard();
        score.incrementScore1();
        score.incrementScore1();

        score.incrementScore2();

        score.incrementTie();
        score.incrementTie();
        score.incrementTie();

        score = scoreDB.insert(score);

        Toast.makeText(this, score.toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "Length: " + scoreDB.count("1 = 1"), Toast.LENGTH_LONG).show();

        score.incrementScore1();
        score.incrementScore1();

        score.incrementScore2();

        score.incrementTie();
        score.incrementTie();
        score.incrementTie();

        scoreDB.update(score);

        Toast.makeText(this, score.toString(), Toast.LENGTH_LONG).show();

    }
}
