package com.daviapps.tictactoe.core;

import com.daviapps.tictactoe.domain.Position;
import com.daviapps.tictactoe.domain.ValidationResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

// Created by daviinacio on 27/12/2020.
public class Game {
    private static final int FIELD_EMPTY = 0;
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;

    private int [][] field;

    private OnSelectedListener onSelectedListener;

    // Constructors
    private Game(int [][] field){
        this.field = field;
    }
    public Game(int length){
        this(new int[length > 3 ? length : 3][length > 3 ? length : 3]);
    }
    public Game(){
        this(3);
    }

    // Game methods
    public void clearFields(){
        for(int[] array : this.field)
            Arrays.fill(array, Game.FIELD_EMPTY);
    }

    public boolean isFieldDirty(){
        return this.findEmptyPositions().size() != this.getAllPositions().size();
    }

    public boolean select(Position position, int player){
        try {
            if(player != Game.FIELD_EMPTY && this.field[position.getX()][position.getY()] == Game.FIELD_EMPTY){
                this.field[position.getX()][position.getY()] = player;
                this.handleOnSelected(position, player);
                return true;
            }
            else return false;
        }
        catch(ArrayIndexOutOfBoundsException | NullPointerException ignored){
            return false;
        }
    }

    public ValidationResult validateWinner(){
        for(int m = 0; m < 4; m++){
            for(int x = 0; x < (m < 2 ? this.field.length : 1); x++){
                Set<Integer> s = new HashSet<>();
                List<Position> positions = new ArrayList<>();

                // Check vertical and horizontal
                if(m == 0 || m == 1){
                    for(int y = 0; y < (m == 0 ? this.field[0].length : this.field.length); y++){
                        Position pos = new Position(m == 0 ? x : y, m == 0 ? y : x);
                        positions.add(pos);
                        s.add(this.field[pos.getX()][pos.getY()]);
                    }
                }
                else
                // Check diagonal
                if(m == 2 || m == 3){
                    for(int i = 0; i < this.field.length; i++){
                        Position pos = new Position(i, m == 2 ? i : (this.field[i].length -1) - i);
                        positions.add(pos);
                        s.add(this.field[pos.getX()][pos.getY()]);
                    }
                }

                // Check for winner
                if(s.size() == 1){
                    int winner = (int) Objects.requireNonNull(s.toArray())[0];
                    if(winner != Game.FIELD_EMPTY)
                        return new ValidationResult()
                                .setWinner(winner)
                                .setPositions(positions);
                }
            }
        }

        return null;
    }

    public List<Position> findEmptyPositions(){
        List<Position> empty = new ArrayList<>();

        for(int x = 0; x < this.field.length; x++){
            for(int y = 0; y < this.field[x].length; y++){
                if(this.field[x][y] == Game.FIELD_EMPTY)
                    empty.add(new Position(x, y));
            }
        }

        return empty;
    }

    public List<Position> getAllPositions(){
        List<Position> positions = new ArrayList<>();

        for(int x = 0; x < this.field.length; x++){
            for(int y = 0; y < this.field[x].length; y++){
                positions.add(new Position(x, y));
            }
        }

        return positions;
    }

    public Game getVirtualGame(){
        return new Game(this.getVirtualField());
    }

    public int[][] getVirtualField(){
        int[][] virtual = new int[this.field.length][];

        for(int i = 0; i < this.field.length; i++)
            virtual[i] = Arrays.copyOf(this.field[i], this.field[i].length);

        return virtual;
    }

    // Listener Handlers
    private void handleOnSelected(Position position, int player){
        if(this.onSelectedListener != null)
            this.onSelectedListener.onSelected(position, player);
    }

    // Listener Setters
    public void setOnSelectedListener(OnSelectedListener listener){
        if(listener != null)
            this.onSelectedListener = listener;
    }

    // Listeners
    public interface OnSelectedListener {
        void onSelected(Position position, int player);
    }
}
