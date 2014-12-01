package com.prylutskyi.blackjack.engine.impl;

import com.prylutskyi.blackjack.engine.BlackJackEngine;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.GameStatus;
import com.prylutskyi.blackjack.vo.Table;

import java.util.ArrayList;
import java.util.List;

import static com.prylutskyi.blackjack.constants.TestConstants.*;
import static com.prylutskyi.blackjack.enumeration.Side.PLAYER;
import static com.prylutskyi.blackjack.utils.VoTestUtils.getAction;

/**
 * Created by Patap on 29.11.2014.
 */
public class StubBlackJackEngine implements BlackJackEngine {


    @Override
    public List<Action> makeBet(Table table, double bet) {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setWinner(PLAYER);
        table.setGameStatus(gameStatus);
        table.setBet(bet);
        List<Action> actions = new ArrayList<>();
        Action action = getAction(table.getGame());
        action.setLog(TEST_BET_LOG);
        Action playerWinAction = getAction(table.getGame());
        playerWinAction.setLog(TEST_PLAYER_WIN_LOG);
        actions.add(playerWinAction);
        actions.add(action);
        return actions;
    }

    @Override
    public List<Action> playerHit(Table table) {
        List<Action> actions = new ArrayList<>();
        Action action = getAction(table.getGame());
        action.setLog(TEST_HIT_LOG);
        actions.add(action);
        return actions;
    }

    @Override
    public List<Action> playerStand(Table table) {
        List<Action> actions = new ArrayList<>();
        Action action = getAction(table.getGame());
        action.setLog(TEST_STAND_LOG);
        actions.add(action);
        return actions;
    }
}
