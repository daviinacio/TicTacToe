package com.daviapps.tictactoe.interfaces;

import com.daviapps.tictactoe.core.Game;
import com.daviapps.tictactoe.domain.Position;

// Created by daviinacio on 27/12/2020.
public interface Machine {
    Position choose(Game game);
}
