package com.daviapps.tictactoe.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daviapps.tictactoe.R;
import com.daviapps.tictactoe.adapter.ScoreboardAdapter;
import com.daviapps.tictactoe.util.Admob;

public class ScoreboardActivity extends AppCompatActivity {
    private ListView lvScoreboard;
    private TextView tvEmptyTitle, tvEmptyMessage;
    private ScoreboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        this.tvEmptyTitle = findViewById(R.id.tv_empty_title);
        this.tvEmptyMessage = findViewById(R.id.tv_empty_message);
        this.lvScoreboard = findViewById(R.id.listview_scoreboard);
        this.adapter = new ScoreboardAdapter(this);

        this.adapter.setOnLoadListener(items -> {
            this.tvEmptyTitle.setVisibility(items.size() == 0 ? View.VISIBLE : View.GONE);
            this.tvEmptyMessage.setVisibility(items.size() == 0 ? View.VISIBLE : View.GONE);
        });

        this.lvScoreboard.setAdapter(adapter);

        this.lvScoreboard.setOnItemLongClickListener((parent, v, i, id) -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.alert_delete_score_title)
                    .setMessage(R.string.alert_delete_score_message)
                    .setPositiveButton(R.string.delete, (dialog, which) -> {
                        this.adapter.removeItemByPosition(i);
                        Toast.makeText(this, R.string.game_db_deleted, Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
            return true;
        });

        this.adapter.load();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
