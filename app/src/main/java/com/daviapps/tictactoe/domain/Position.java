package com.daviapps.tictactoe.domain;

// Created by daviinacio on 27/12/2020.
public class Position {
    private int x = -1, y = -1;

    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position setX(int x) {
        this.x = x;
        return this;
    }
    public int getX() {
        return x;
    }

    public Position setY(int y) {
        this.y = y;
        return this;
    }
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        Position position = (Position) object;
        return position.getX() == this.getX() && position.getY() == this.getY();
    }

    @Override
    public String toString() {
        return String.format("{x: %s, y: %s}", x, y);
    }
}