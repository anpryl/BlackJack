package com.prylutskyi.blackjack.services;

import com.prylutskyi.blackjack.vo.Action;

import java.util.List;

/**
 * Created by Patap on 26.11.2014.
 */
public interface ActionService {

    List<Action> getActionsForGame(long gameId);

}
