package com.daviapps.tictactoe.core;

import com.daviapps.tictactoe.domain.Position;
import com.daviapps.tictactoe.interfaces.Machine;

import java.util.List;
import java.util.Random;

// Created by daviinacio on 27/12/2020.
public class MachineEasy implements Machine {
    @Override
    public Position choose(Game game) {
        List<Position> emptyPositions = game.findEmptyPositions();

        int c = new Random().nextInt(emptyPositions.size());

        return emptyPositions.get(c);
    }
}
