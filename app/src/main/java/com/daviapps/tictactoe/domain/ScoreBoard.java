package com.daviapps.tictactoe.domain;

import com.daviapps.tictactoe.database.DataSet;

import java.util.Date;

// Created by daviinacio on 26/11/2019.
public class ScoreBoard {
    private int id;
    private int score1, score2, tie;
    private Date date;
    private long duringTime;

    // Constructors
    public ScoreBoard() {
        this.date = new Date();
    }

    public ScoreBoard(int id) {
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }

    public int getScore1() {
        return score1;
    }
    public void setScore1(int score1) {
        this.score1 = score1;
    }
    public int incrementScore1(){
        return this.score1++;
    }

    public int getScore2() {
        return score2;
    }
    public void setScore2(int score2) {
        this.score2 = score2;
    }
    public int incrementScore2(){
        return this.score2++;
    }

    public int getTie() {
        return tie;
    }
    public void setTie(int tie) {
        this.tie = tie;
    }
    public int incrementTie(){
        return this.tie++;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public long getDuringTime() {
        return duringTime;
    }
    public void setDuringTime(long duringTime) {
        this.duringTime = duringTime;
    }

    // Other methods
    @Override
    public String toString() {
        return String.format("ScoreBoard(%s):\nid:\t%s\nscore1:\t%s\nscore2:\t%s\ntie:\t%s\ndate:\t%s\nduring_time:\t%smin",
                super.toString(), id, score1, score2, tie, DataSet.timeFormat.format(getDate()), (float) duringTime / 60);
    }
}
