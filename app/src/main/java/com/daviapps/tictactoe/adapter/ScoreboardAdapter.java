package com.daviapps.tictactoe.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daviapps.tictactoe.R;
import com.daviapps.tictactoe.database.DataSet;
import com.daviapps.tictactoe.database.ScoreboardDAO;
import com.daviapps.tictactoe.domain.ScoreBoard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

// Created by daviinacio on 12/12/2019.
public class ScoreboardAdapter extends BaseAdapter {
    private DataSet<ScoreBoard> db;
    private List<ScoreBoard> items;

    private OnLoadListener onLoadListener;

    private Context context;
    private SimpleDateFormat dateFormat;

    public ScoreboardAdapter(Context context){
        this.context = context;
        this.items = new ArrayList<>();
        this.db = new ScoreboardDAO(context);

        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ScoreBoard getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.getItem(i).getId();
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.adapter_scoreboard_item, null);

        ScoreBoard item = this.getItem(i);

        TextView player1Name = layout.findViewById(R.id.tv_player1_name);
        TextView player1Score = layout.findViewById(R.id.tv_player1_score);
        TextView player2Name = layout.findViewById(R.id.tv_player2_name);
        TextView player2Score = layout.findViewById(R.id.tv_player2_score);
        TextView playerTieScore = layout.findViewById(R.id.tv_tie_score);
        TextView date = layout.findViewById(R.id.tv_date);

        player1Score.setText(String.format("%02d", item.getScore1()));
        player2Score.setText(String.format("%02d", item.getScore2()));
        playerTieScore.setText(String.format("%02d", item.getTie()));

        date.setText(dateFormat.format(item.getDate()));

        if(item.getDifficulty() != ScoreBoard.DIFF_PVP){
            player1Name.setText(R.string.player);
            player2Name.setText(R.string.machine);
        }
        else {
            player1Name.setText(R.string.player1);
            player2Name.setText(R.string.player2);
        }

        return layout;
    }

    public void addItem(ScoreBoard item){
        this.db.insert(item);
        this.load();
    }

    public void setItem(ScoreBoard item){
        this.db.update(item);
        this.load();
    }

    public void removeItem(ScoreBoard item){
        this.db.delete(item);
        this.load();
    }

    public void removeItemById(int id){
        this.db.deleteById(id);
        this.load();
    }

    public void removeItemByPosition(int position){
        this.db.delete(this.getItem(position));
        this.load();
    }


    public void load(){
        this.items = db.select("1 = 1", null);
        this.notifyDataSetChanged();

        if(this.onLoadListener != null)
            this.onLoadListener.onLoad(this.items);
    }

    public void setOnLoadListener(OnLoadListener listener){
        if(listener != null)
            this.onLoadListener = listener;
    }


    public interface OnLoadListener {
        void onLoad(List<ScoreBoard> items);
    }
}
