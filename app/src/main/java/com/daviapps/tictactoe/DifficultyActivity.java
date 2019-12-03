package com.daviapps.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daviapps.tictactoe.domain.ScoreBoard;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class DifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

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
