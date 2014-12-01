package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.services.ActionService;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;

import java.util.List;

import static com.prylutskyi.blackjack.utils.CheckUtils.checkForNull;

/**
 * Created by Patap on 29.11.2014.
 */
public class ActionServiceImpl implements ActionService {

    private ActionDao actionDao;

    private GameDao gameDao;

    @Override
    public List<Action> getActionsForGame(long gameId) {
        Game game = gameDao.findById(gameId);
        checkForNull(game, gameId);
        return actionDao.getActionsForGame(game);
    }

    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }
}
