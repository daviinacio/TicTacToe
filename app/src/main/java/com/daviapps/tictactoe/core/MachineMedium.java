package com.daviapps.tictactoe.core;

import com.daviapps.tictactoe.domain.Position;
import com.daviapps.tictactoe.domain.ValidationResult;

// Created by daviinacio on 27/12/2020.
public class MachineMedium extends MachineEasy {
    @Override
    public Position choose(Game game) {
        if(game == null) return null;

        // Find the last final attach (to win or to defense)
        for(int player = 2; player > 0; player--){
            for(Position position : game.findEmptyPositions()){
                Game virtual = game.getVirtualGame();

                if(virtual.select(position, player)){
                    ValidationResult result = virtual.validateWinner();

                    if(result != null && result.getWinner() == player)
                        return position;
                }
            }
        }

        return super.choose(game);
    }
}
