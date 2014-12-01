package com.prylutskyi.blackjack.engine;

import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Table;

import java.util.List;

/**
 * Created by Patap on 29.11.2014.
 */
public interface BlackJackEngine {

    List<Action> makeBet(Table table, double bet);

    List<Action> playerHit(Table table);

    List<Action> playerStand(Table table);
}
