package com.daviapps.tictactoe.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.daviapps.tictactoe.R;
import com.daviapps.tictactoe.domain.ScoreBoard;
import com.daviapps.tictactoe.util.Admob;
import com.google.android.gms.ads.AdListener;

public class MainActivity extends AppCompatActivity {

    Button btn_pvm, btn_pvp, btn_scoreboard, btn_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Icon on Toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_spaced);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        this.btn_pvm = findViewById(R.id.btn_player_vs_machine);
        this.btn_pvp = findViewById(R.id.btn_player_vs_player);
        this.btn_scoreboard = findViewById(R.id.btn_scoreboard);
        this.btn_settings = findViewById(R.id.btn_settings);

        /*	*	*	*	  AdMob    *   Admob   *  Admob 	*	*	*	*/

        Admob.request(this, findViewById(R.id.adView), new AdListener(){
            @Override
            public void onAdLoaded(){
                //Toast.makeText(MainActivity.this, "Admob: loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int p1){
                //Toast.makeText(MainActivity.this, "AdMob: Fail to load (" + p1 + ")", Toast.LENGTH_SHORT).show();
            }
        });

        /*	*	*	*	  OnClick   *   OnClick *  OnClick  *	*	*	*/

        this.btn_pvm.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, DifficultyActivity.class));
            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        this.btn_pvp.setOnClickListener((v) -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);

            // Open as a player vs. player
            intent.putExtra("difficulty", ScoreBoard.DIFF_PVP);

            startActivity(intent);
        });

        this.btn_scoreboard.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, ScoreboardActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        this.btn_settings.setOnClickListener((v) -> {
            Toast.makeText(this, R.string.func_in_dev, Toast.LENGTH_SHORT).show();
        });
    }
}
