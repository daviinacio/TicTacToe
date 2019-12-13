package com.daviapps.tictactoe.domain;

import com.daviapps.tictactoe.database.DataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Created by daviinacio on 26/11/2019.
public class ScoreBoard {
    public static final int DIFF_EASY = 0x01, DIFF_MEDIUM = 0x02, DIFF_HARD = 0x03, DIFF_PVP = 0xff;

    private int id;
    private int score1, score2, tie;
    private int difficulty;
    private Date date;
    private long duringTime;

    // Observers
    private List<Observer> observers;

    // Constructors
    public ScoreBoard() {
        this.date = new Date();
        this.observers = new ArrayList<>();
    }

    public ScoreBoard(int id) {
        this();
        this.id = id;
    }

    // Getters and Setters
    public int getId() { return id; }

    public int getScore1() {
        return score1;
    }
    public void setScore1(int score1) {
        this.score1 = score1;
        this.handleObserver();
    }
    public int incrementScore1(){
        this.setScore1(this.score1 + 1);
        return this.score1;
    }

    public int getScore2() {
        return score2;
    }
    public void setScore2(int score2) {
        this.score2 = score2;
        this.handleObserver();
    }
    public int incrementScore2(){
        this.setScore2(this.score2 + 1);
        return this.score2;
    }

    public int getTie() {
        return tie;
    }
    public void setTie(int tie) {
        this.tie = tie;
        this.handleObserver();
    }
    public int incrementTie(){
        this.setTie(this.tie + 1);
        return this.tie;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public int getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public long getDuringTime() {
        return duringTime;
    }
    public void setDuringTime(long duringTime) {
        this.duringTime = duringTime;
    }

    public  boolean isEmpty(){
        return score1 == score2 && score2 == tie && tie == 0;
    }

    // Observer
    private void handleObserver(){
        for(Observer observer : this.observers){
            observer.onScoreChange(this.score1, this.score2, this.tie);
        }
    }

    public void addObserver(Observer observer){
        if(observer != null){
            this.observers.add(observer);
        }
    }

    // Other methods
    @Override
    public String toString() {
        return String.format("ScoreBoard(%s):\nid:\t%s\nscore1:\t%s\nscore2:\t%s\ntie:\t%s\ndate:\t%s\nduring_time:\t%smin",
                super.toString(), id, score1, score2, tie, DataSet.timeFormat.format(getDate()), (float) duringTime / 60);
    }

    public interface Observer {
        void onScoreChange(int score1, int score2, int tie);
    }
}
