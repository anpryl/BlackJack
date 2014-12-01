package com.prylutskyi.blackjack.dao;

import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;

import java.util.List;

/**
 * Created by anatolii.prylutskyi on 11/28/2014.
 */
public interface ActionDao {

    public List<Action> list();

    Action findById(long id);

    void saveOrUpdate(Action action);

    void delete(Action action);

    List<Action> getActionsForGame(Game game);
}
