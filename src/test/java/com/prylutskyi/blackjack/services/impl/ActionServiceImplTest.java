package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.exceptions.InvalidIdException;
import com.prylutskyi.blackjack.services.ActionService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.utils.VoTestUtils.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class ActionServiceImplTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ActionService actionService;

    @Test
    public void testGetActionsForGame() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        Action action1 = getAction(game);
        Action action2 = getAction(game);
        List<Action> actions = game.getActions();
        actions.add(action1);
        actions.add(action2);
        Game game2 = getGame(account);
        account.getGames().add(game2);
        Action action3 = getAction(game2);
        game2.getActions().add(action3);
        accountDao.saveOrUpdate(account);
        List<Action> actionsForGame = actionService.getActionsForGame(game.getGameId());
        assertEquals(actions, actionsForGame);
        accountDao.delete(account);
    }

    @Test
    public void testGetActionsForGameEmptyList() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        List<Action> actions = game.getActions();
        accountDao.saveOrUpdate(account);
        List<Action> actionsForGame = actionService.getActionsForGame(game.getGameId());
        assertEquals(actions, actionsForGame);
        accountDao.delete(account);
    }

    @Test(expected = InvalidIdException.class)
    public void testGetActionsForGameInvalidGameId() throws Exception {
        actionService.getActionsForGame(Long.MAX_VALUE);
    }
}