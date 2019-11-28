package com.daviapps.tictactop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.daviapps.tictactop.database.ScoreboardDAO;
import com.daviapps.tictactop.domain.ScoreBoard;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_launcher_spaced);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        /*	*	*	*	  AdMob    *   Admob   *  Admob 	*	*	*	*/

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        AdView adView = findViewById(R.id.adView);

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                Toast.makeText(MainActivity.this, "Admob: loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int p1){
                Toast.makeText(MainActivity.this, "AdMob: Fail to load (" + p1 + ")", Toast.LENGTH_SHORT).show();
            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A4DDF5EC5E81FE0D117CA05BA164E8B8") // LG K10
                .addTestDevice("B32DF5960E16B6E638F0861FB8E63372") // LG G6
                .build();

        adView.loadAd(adRequest);

    }
}
