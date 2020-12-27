package com.daviapps.tictactoe.domain;

import java.util.List;

// Created by daviinacio on 27/12/2020.
public class ValidationResult {
    private int winner;
    private List<Position> positions;

    public ValidationResult() {}

    public ValidationResult(int winner, List<Position> positions) {
        this.winner = winner;
        this.positions = positions;
    }

    public ValidationResult setWinner(int machine){
        this.winner = machine;
        return this;
    }
    public int getWinner(){
        return this.winner;
    }

    public ValidationResult setPositions(List<Position> positions){
        if(positions != null && !positions.isEmpty())
            this.positions = positions;
        return this;
    }
    public ValidationResult addPosition(Position position){
        if(position != null)
            this.positions.add(position);
        return this;
    }
    public List<Position> getPositions(){
        return this.positions;
    }
}
