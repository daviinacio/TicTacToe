package com.daviapps.tictactoe.activities;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daviapps.tictactoe.R;
import com.daviapps.tictactoe.core.Game;
import com.daviapps.tictactoe.domain.ValidationResult;
import com.daviapps.tictactoe.interfaces.Machine;
import com.daviapps.tictactoe.core.MachineEasy;
import com.daviapps.tictactoe.core.MachineHard;
import com.daviapps.tictactoe.core.MachineMedium;
import com.daviapps.tictactoe.domain.Position;
import com.daviapps.tictactoe.database.DataSet;
import com.daviapps.tictactoe.database.ScoreboardDAO;
import com.daviapps.tictactoe.domain.ScoreBoard;
import com.daviapps.tictactoe.util.Admob;
import com.google.android.gms.ads.AdListener;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements ScoreBoard.Observer, Game.OnSelectedListener {
    private Machine machine;
    private Game game;

    private int currentPlayer = Game.PLAYER1;

    private ScoreBoard score;
    private DataSet<ScoreBoard> db;

    private TextView tvScorePlayer1, tvScorePlayer2, tvScoreTie;
    private TextView tvGameStatus;

    private LinearLayout llTilesContainer;
    private Button[][] btnArray;

    private final static int DELAY_WIN = 2000;
    private final static int DELAY_SELECTED = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Views
        TextView tvLabelScore2 = findViewById(R.id.label_score_player2);
        this.tvScorePlayer1 = findViewById(R.id.tv_score_player);
        this.tvScorePlayer2 = findViewById(R.id.tv_score_player2);
        this.tvScoreTie = findViewById(R.id.tv_score_tie);

        this.tvGameStatus = findViewById(R.id.tv_game_status);

        this.llTilesContainer = findViewById(R.id.ll_tiles_container);

        // Database
        this.db = new ScoreboardDAO(this);

        // Advertising
        Admob.request(this, findViewById(R.id.adView), new AdListener(){});

        // Game values
        this.game = new Game(3);
        this.game.setOnSelectedListener(this);

        this.score = new ScoreBoard();
        this.score.addObserver(this);

        // Difficult
        this.score.setDifficulty(getIntent().getIntExtra("difficulty", ScoreBoard.DIFF_PVP));

        switch (this.score.getDifficulty()) {
            case ScoreBoard.DIFF_PVP:
                Toast.makeText(this, R.string.main_player_vs_player, Toast.LENGTH_SHORT).show();
                this.machine = null;
                break;

            case ScoreBoard.DIFF_EASY:
                Toast.makeText(this, R.string.easy, Toast.LENGTH_SHORT).show();
                this.machine = new MachineEasy();
                break;

            case ScoreBoard.DIFF_MEDIUM:
                Toast.makeText(this, R.string.medium, Toast.LENGTH_SHORT).show();
                this.machine = new MachineMedium();
                break;

            case ScoreBoard.DIFF_HARD:
                Toast.makeText(this, R.string.hard, Toast.LENGTH_SHORT).show();
                this.machine = new MachineHard();
                break;

            default:
                Toast.makeText(this, "[Error]: Unknown Difficulty", Toast.LENGTH_SHORT).show();
                break;
        }

        // Initialize values
        if(machine != null)
            this.currentPlayer = new Random().nextInt(2) + 1;
        else
            this.currentPlayer = Game.PLAYER2;

        // Show opponent label
        tvLabelScore2.setText(this.machine == null ?
                R.string.opponent :
                R.string.machine
        );

        // Clear fields
        this.game.clearFields();
        this.renderButtons();
        this.enableButtons(false);

        this.nextRound();
    }

    private boolean validate(){
        ValidationResult winner = game.validateWinner();

        if(winner != null){
            runOnUiThread(() ->
                    this.highlightPositions(winner.getPositions(), winner.getWinner()));

            if(winner.getWinner() == Game.PLAYER1) {
                this.score.incrementScore1();
                this.currentPlayer++;

                runOnUiThread(() -> this.tvGameStatus.setText(this.machine == null ?
                        R.string.game_player1_won_turn :
                        R.string.game_you_won_turn
                ));
            }
            else
            if(winner.getWinner() == Game.PLAYER2) {
                this.score.incrementScore2();
                this.currentPlayer++;

                runOnUiThread(() -> this.tvGameStatus.setText(this.machine == null ?
                        R.string.game_player2_won_turn :
                        R.string.game_machine_won_turn
                ));
            }

            return true;
        }
        else
        if(game.findEmptyPositions().isEmpty()){
            this.highlightPositions(this.game.getAllPositions(), 0);
            this.score.incrementTie();

            runOnUiThread(() ->
                this.tvGameStatus.setText(R.string.game_tie_turn));

            return true;
        }

        return false;
    }

    private void nextPlayer(){
        // Change current player
        this.currentPlayer %= 2;
        this.currentPlayer++;

        // Update game status
        runOnUiThread(() ->
            this.tvGameStatus.setText(this.machine == null ?
                    // Player vs. Player
                    (this.currentPlayer == Game.PLAYER1 ?
                            R.string.game_player1_turn :
                            R.string.game_player2_turn):
                    // Player vs. Machine
                    (this.currentPlayer == Game.PLAYER1 ?
                            R.string.game_your_turn :
                            R.string.game_machine_turn)
            )
        );

        new Thread(() -> {
            try {
                // Get machine choice
                if(this.machine != null && currentPlayer == Game.PLAYER2){
                    Thread.sleep(GameActivity.DELAY_SELECTED);
                    this.onMachineSelected(this.machine.choose(game));
                }
                // Enable buttons to the human(s) choose
                else this.enableButtons(true);
            }
            catch (InterruptedException ignored){}
        }).start();
    }

    private void nextRound(){
        new Thread(() -> {
            try {
                // Delay between rounds, only if it's running
                if(this.game.isFieldDirty())
                    Thread.sleep(GameActivity.DELAY_WIN);

                // Clear values to the next round
                this.game.clearFields();
                this.renderButtons();
                this.nextPlayer();
            }
            catch (InterruptedException ignored){}
        }).start();
    }

    // Visual methods
    private void renderButtons(){
        char [] label = new char[] {' ', 'X', 'O'};

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        llp.setMargins(1, 1, 1, 1);
        llp.weight = 1.0f;

        if(this.game != null){
            int [][] virtualField = game.getVirtualField();

            // Create buttons
            if(this.btnArray == null){
                this.btnArray = new Button[virtualField.length][];

                for (int x = 0; x < virtualField.length; x++) {
                    LinearLayout row = new LinearLayout(
                            new ContextThemeWrapper(this, R.style.AppTheme_LinearLayout_Row),
                            null, 0);

                    row.setLayoutParams(llp);

                    this.btnArray[x] = new Button[virtualField[x].length];

                    for (int y = 0; y < virtualField[x].length; y++) {
                        Button btn = new Button(
                                new ContextThemeWrapper(this, R.style.AppTheme_Button_Tiles),
                                null, 0);

                        btn.setText(Character.toString(label[virtualField[x][y]]));
                        btn.setLayoutParams(llp);

                        final Position position = new Position(x, y);

                        btn.setOnClickListener(v ->
                            this.onUserSelected(position));

                        row.addView(btn);
                        this.btnArray[x][y] = btn;
                    }

                    this.llTilesContainer.addView(row);
                }
            }

            // Update values
            for (int x = 0; x < this.btnArray.length; x++) {
                for (int y = 0; y < this.btnArray[x].length; y++) {
                    Button btn = this.btnArray[x][y];

                    final char value = label[virtualField[x][y]];

                    runOnUiThread(() -> {
                        btn.setText(Character.toString(value));
                        btn.setTextColor(getResources()
                                .getColor(R.color.tile_default_color));
                    });
                }
            }
        }
    }

    private void highlightPositions(List<Position> positions, final int winner){
        int [] highlightColors = new int[]{
                R.color.tile_tie_color,
                R.color.tile_pl_win_color,
                R.color.tile_p2_win_color
        };

        for (Position p : positions){
            Button btn = this.btnArray[p.getX()][p.getY()];
          
            runOnUiThread(() ->
                    btn.setTextColor(getResources().getColor(highlightColors[winner])));
        }
    }

    private void enableButtons(boolean enable){
        for(Button[] arr : this.btnArray)
            for (Button b : arr)
                this.runOnUiThread(() -> b.setEnabled(enable));
    }

    private void onUserSelected(Position position){
        // Player vs. Player
        if(this.machine == null)
            this.game.select(position, currentPlayer);
        else
        // Player vs. Machine
        if(currentPlayer == Game.PLAYER1)
            this.game.select(position, Game.PLAYER1);
    }

    private void onMachineSelected(Position position){
        this.game.select(position, Game.PLAYER2);
    }

    @Override
    public void onSelected(Position position, int player) {
        this.enableButtons(false);
        this.renderButtons();

        // Round is over
        if(this.validate()){
            this.nextRound();
        }
        // No one scored yet
        else {
            this.nextPlayer();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onScoreChange(int score1, int score2, int tie) {
        this.runOnUiThread(() -> {
            this.tvScorePlayer1.setText(Integer.toString(score1));
            this.tvScorePlayer2.setText(Integer.toString(score2));
            this.tvScoreTie.setText(Integer.toString(tie));
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(this.score.isEmpty()){
            Toast.makeText(this, getString(R.string.game_empty_score), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getString(R.string.game_db_inserted), Toast.LENGTH_SHORT).show();
            this.score = this.db.update(this.score);
            this.score.addObserver(this);
        }
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
