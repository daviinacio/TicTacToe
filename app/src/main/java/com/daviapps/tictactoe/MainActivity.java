package com.daviapps.tictactoe;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.daviapps.tictactoe.domain.ScoreBoard;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        AdView adView = findViewById(R.id.adView);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                //Toast.makeText(MainActivity.this, "Admob: loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int p1){
                //Toast.makeText(MainActivity.this, "AdMob: Fail to load (" + p1 + ")", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A4DDF5EC5E81FE0D117CA05BA164E8B8") // LG K10
                .addTestDevice("B32DF5960E16B6E638F0861FB8E63372") // LG G6
                .build();

        adView.loadAd(adRequest);

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

        });

        this.btn_settings.setOnClickListener((v) -> {

        });


    }
}
