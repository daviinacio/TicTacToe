package com.daviapps.tictactoe;

import android.annotation.SuppressLint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daviapps.tictactoe.database.DataSet;
import com.daviapps.tictactoe.database.ScoreboardDAO;
import com.daviapps.tictactoe.domain.ScoreBoard;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements ScoreBoard.Observer {
    private Button [][] btn_array;

    private TextView tvScore1, tvScore2, tvScoreTie, tvScore1Label, tvScore2Label, tvGameStatus;

    private boolean roundSide = false; // true: player1, false: player2

    private static final String player1Mark = "X", player2Mark = "O";

    private ScoreBoard score;

    private DataSet<ScoreBoard> db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.db = new ScoreboardDAO(this);

        this.btn_array = new Button [][]{
            {
                findViewById(R.id.btn_tile_1_1),
                findViewById(R.id.btn_tile_1_2),
                findViewById(R.id.btn_tile_1_3),
            },
            {
                findViewById(R.id.btn_tile_2_1),
                findViewById(R.id.btn_tile_2_2),
                findViewById(R.id.btn_tile_2_3),
            },
            {
                findViewById(R.id.btn_tile_3_1),
                findViewById(R.id.btn_tile_3_2),
                findViewById(R.id.btn_tile_3_3),
            },
        };

        this.tvScore1 = findViewById(R.id.tv_score_player);
        this.tvScore2 = findViewById(R.id.tv_score_player2);
        this.tvScoreTie = findViewById(R.id.tv_score_tie);
        this.tvScore1Label = findViewById(R.id.label_score_player);
        this.tvScore2Label = findViewById(R.id.label_score_player2);
        this.tvGameStatus = findViewById(R.id.tv_game_status);

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


        this.score = new ScoreBoard();
        this.score.addObserver(this);

        this.score.setDifficulty(getIntent().getIntExtra("difficulty", 0x00));

        this.tvScore2Label.setText(this.score.getDifficulty() == ScoreBoard.DIFF_PVP ?
                R.string.opponent : R.string.machine);

        switch (this.score.getDifficulty()) {
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

        for(int x = 0; x < btn_array.length; x++){
            for(int y = 0; y < btn_array[x].length; y++){
                final int _x = x, _y = y;

                btn_array[x][y].setOnClickListener((v) -> {
                    markTile(_x, _y);
                });
            }
        }

        reset();

    }

    private boolean markTile(int x, int y){
        try {
            Button btn = btn_array[x][y];

            if(btn.getText().equals("")){
                // Player1
                if(roundSide){
                    btn.setText(player1Mark);
                }
                // Player2
                else {
                    btn.setText(player2Mark);
                }

                int result = compareTiles();

                //Toast.makeText(this, "Result: " + result, Toast.LENGTH_SHORT).show();

                if(result != 0)
                    finishRound(result);
                else
                    next();

                if(result == 1)
                    score.incrementScore1();
                else
                if(result == 2)
                    score.incrementScore2();
                else
                if(result == 3)
                    score.incrementTie();

                return true;

            }
            else {
                if(this.score.getDifficulty() == ScoreBoard.DIFF_PVP && !roundSide)
                    Toast.makeText(this, R.string.game_tile_already_marked, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}

        return false;
    }

    private int compareTiles(){
        // Horizontal
        for(Button [] btn_x  : btn_array){
            if(
                    !btn_x[0].getText().toString().isEmpty() &&
                    !btn_x[1].getText().toString().isEmpty() &&
                    !btn_x[2].getText().toString().isEmpty() &&

                    btn_x[0].getText().equals(btn_x[1].getText()) &&
                    btn_x[1].getText().equals(btn_x[2].getText())
            ){
                for(Button btn_y : btn_x){
                    btn_y.setTextColor(getResources().getColor(roundSide ?
                            R.color.tile_pl_win_color :
                            R.color.tile_p2_win_color));
                }

                return roundSide ? 1 : 2;
            }
        }

        // Vertical
        for (int y = 0; y < btn_array.length; y++) {
            if(
                    !btn_array[0][y].getText().toString().isEmpty() &&
                    !btn_array[1][y].getText().toString().isEmpty() &&
                    !btn_array[2][y].getText().toString().isEmpty() &&

                    btn_array[0][y].getText().equals(btn_array[1][y].getText()) &&
                    btn_array[1][y].getText().equals(btn_array[2][y].getText())
            ){

                for (int x = 0; x < btn_array.length; x++) {
                    btn_array[x][y].setTextColor(getResources().getColor(roundSide ?
                            R.color.tile_pl_win_color :
                            R.color.tile_p2_win_color));
                }

                return roundSide ? 1 : 2;
            }
        }

        // Diagonal [\]
        if(
                !btn_array[0][0].getText().toString().isEmpty() &&
                !btn_array[1][1].getText().toString().isEmpty() &&
                !btn_array[2][2].getText().toString().isEmpty() &&

                btn_array[0][0].getText().equals(btn_array[1][1].getText()) &&
                btn_array[1][1].getText().equals(btn_array[2][2].getText())
        ){
            btn_array[0][0].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            btn_array[1][1].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            btn_array[2][2].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            return roundSide ? 1 : 2;
        }

        // Diagonal [/]
        if(
                !btn_array[0][2].getText().toString().isEmpty() &&
                !btn_array[1][1].getText().toString().isEmpty() &&
                !btn_array[2][0].getText().toString().isEmpty() &&

                btn_array[0][2].getText().equals(btn_array[1][1].getText()) &&
                btn_array[1][1].getText().equals(btn_array[2][0].getText())
        ){
            btn_array[0][2].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            btn_array[1][1].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            btn_array[2][0].setTextColor(getResources().getColor(roundSide ?
                    R.color.tile_pl_win_color :
                    R.color.tile_p2_win_color));

            return roundSide ? 1 : 2;
        }

        if(
                !btn_array[0][0].getText().toString().isEmpty() &&
                !btn_array[0][1].getText().toString().isEmpty() &&
                !btn_array[0][2].getText().toString().isEmpty() &&

                !btn_array[1][0].getText().toString().isEmpty() &&
                !btn_array[1][1].getText().toString().isEmpty() &&
                !btn_array[1][2].getText().toString().isEmpty() &&

                !btn_array[2][0].getText().toString().isEmpty() &&
                !btn_array[2][1].getText().toString().isEmpty() &&
                !btn_array[2][2].getText().toString().isEmpty()
        ){
            return 3;
        }

        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onScoreChange(int score1, int score2, int tie) {
        runOnUiThread(() -> {
            this.tvScore1.setText(Integer.toString(score1));
            this.tvScore2.setText(Integer.toString(score2));
            this.tvScoreTie.setText(Integer.toString(tie));
        });
    }

    private void finishGame(){
        if(this.score != null){
            this.db.insert(this.score);

            Toast.makeText(this, R.string.game_db_inserted, Toast.LENGTH_SHORT).show();
        }
    }

    public void finishRound(int result){
        int status = 0;

        switch (result){
            case 1:
                status = (this.score.getDifficulty() == ScoreBoard.DIFF_PVP ?
                        R.string.game_player1_won_turn :
                        R.string.game_you_won_turn);
                break;

            case 2:
                status = (this.score.getDifficulty() == ScoreBoard.DIFF_PVP ?
                        R.string.game_player2_won_turn :
                        R.string.game_machine_won_turn);
                break;

            case 3: status = R.string.game_tie_turn; break;
        }

        this.tvGameStatus.setText(status);

        this.btnEnabled(false);
        new Thread(() -> {
            try {
                Thread.sleep(1500);
                runOnUiThread(() -> {
                    this.btnEnabled(true);
                    this.roundSide = !roundSide;
                    reset();
                });
            } catch (InterruptedException ignored){};
        }).start();

    }

    private void next(){
        Random r = new Random();

        this.roundSide = !roundSide;

        this.tvGameStatus.setText(this.score.getDifficulty() == ScoreBoard.DIFF_PVP ?
                (roundSide ? R.string.game_player1_turn : R.string.game_player2_turn) :
                (roundSide ? R.string.game_your_turn : R.string.game_machine_turn)
        );


        // Machine round
        if(this.score.getDifficulty() != ScoreBoard.DIFF_PVP && !roundSide){
            btnEnabled(false);
            new Thread(() -> {
               try {
                   Thread.sleep(500);
                   runOnUiThread(() -> {
                       btnEnabled(true);
                       while(true){
                           int x = r.nextInt(btn_array.length);
                           int y = r.nextInt(btn_array.length);

                           if(markTile(x, y))
                               break;
                       }
                   });
               } catch (InterruptedException ignored){};
            }).start();
        }
    }

    private void reset(){
        for(Button[] btns : btn_array){
            for(Button btn : btns){
                btn.setText("");
                btn.setTextColor(getResources()
                        .getColor(R.color.tile_default_color));
            }
        }

        next();
    }

    private void btnEnabled(boolean enabled){
        for(Button[] btns : btn_array){
            for(Button btn : btns){
                btn.setEnabled(enabled);
            }
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onStop() {
        super.onStop();

        finishGame();
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
