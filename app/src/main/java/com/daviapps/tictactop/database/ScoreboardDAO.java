package com.daviapps.tictactop.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.daviapps.tictactop.dialogs.ErrorDialog;
import com.daviapps.tictactop.domain.ScoreBoard;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Created by daviinacio on 26/11/2019.
public class ScoreboardDAO extends DataSet<ScoreBoard> {
    private Core core;

    public ScoreboardDAO(Context context){
        this.core = new Core(context);
    }

    @Override
    public ScoreBoard insert(ScoreBoard item) {
        SQLiteDatabase db = core.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(core.columns[1], item.getScore1());
            values.put(core.columns[2], item.getScore2());
            values.put(core.columns[3], item.getTie());
            values.put(core.columns[4], timeFormat.format(item.getDate()));
            values.put(core.columns[5], item.getDuringTime());

            db.insert(Core.DB_NAME, null, values);

            // Return the inserted item with id
            List<ScoreBoard> result = this.select("1 = 1", null);
            item = result.get(result.size() - 1);
        }
        catch (SQLiteException | NullPointerException ex) {
            ErrorDialog.show(core.context, ScoreboardDAO.class, ex, null);
            return null;
        }
        finally {
            db.close();
        }

        return item;
    }

    @Override
    public ScoreBoard update(ScoreBoard item) {
        SQLiteDatabase db = core.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(core.columns[1], item.getScore1());
            values.put(core.columns[2], item.getScore2());
            values.put(core.columns[3], item.getTie());
            values.put(core.columns[4], timeFormat.format(item.getDate()));
            values.put(core.columns[5], item.getDuringTime());

            db.update(Core.DB_NAME, values, "id = ?", new String[]{ Integer.toString(item.getId()) });

        }
        catch (SQLiteException | NullPointerException ex) {
            ErrorDialog.show(core.context, ScoreboardDAO.class, ex, null);
        }
        finally {
            db.close();
        }

        return item;
    }

    @Override
    public boolean delete(ScoreBoard item) {
        SQLiteDatabase db = core.getWritableDatabase();

        try {
            db.delete(Core.DB_NAME, "id = ?", new String[]{ Integer.toString(item.getId()) });
            return true;
        }
        catch (SQLiteException ex){
            ErrorDialog.show(core.context, ScoreboardDAO.class, ex, null);
        }
        finally {
            db.close();
        }

        return false;
    }

    @Override
    public List<ScoreBoard> select(String where, String order) {
        SQLiteDatabase db = core.getReadableDatabase();
        List<ScoreBoard> result = new ArrayList<>();

        try {
            Cursor c = db.query(Core.DB_NAME, core.columns, where, null, null, null, order);

            while(c.moveToNext()){
                ScoreBoard item = new ScoreBoard(c.getInt(0));
                item.setScore1(c.getInt(1));
                item.setScore2(c.getInt(2));
                item.setTie(c.getInt(3));
                item.setDate(timeFormat.parse(c.getString(4)));
                item.setDuringTime(c.getLong(5));

                result.add(item);
            }

            c.close();
        }
        catch (SQLiteException | ParseException ex){
            ErrorDialog.show(core.context, ScoreboardDAO.class, ex, null);
        }
        finally {
            db.close();
        }

        return result;
    }

    protected class Core extends SQLiteOpenHelper {
        static final String DB_NAME = "scoreboard";
        private static final int DB_VERSION = 1;

        String [] columns = { "id", "score1", "score2", "tie", "date", "duringTime" };

        protected Context context;

        public Core(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_NAME + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "score1 INTEGER NOT NULL," +
                        "score2 INTEGER NOT NULL," +
                        "tie INTEGER NOT NULL," +
                        "date DATETIME DEFAULT current_timestamp," +
                        "duringTime LONG DEFAULT 0" +
                        ");");
            } catch (SQLException ex) {
                ErrorDialog.show(context, Core.class, ex, null);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
                onCreate(db);
            } catch (SQLException ex) {
                ErrorDialog.show(context, Core.class, ex, null);
            }
        }
    }
}
