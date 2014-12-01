package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.ActionDao;
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
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class ActionDaoTest {

    public static final String UPDATED_LOG = "updatedLog";

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ActionDao actionDao;

    @Test
    public void testActionCreate() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        Action action = getAction(game);
        accountDao.saveOrUpdate(account);
        actionDao.saveOrUpdate(action);
        assertNotNull(action);
        Action byId = actionDao.findById(action.getActionId());
        assertEquals(action, byId);
        accountDao.delete(account);
    }

    @Test
    public void testActionUpdate() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        Action action = getAction(game);
        game.getActions().add(action);
        accountDao.saveOrUpdate(account);
        action.setLog(UPDATED_LOG);
        actionDao.saveOrUpdate(action);
        Action byId = actionDao.findById(action.getActionId());
        assertEquals(action, byId);
        accountDao.delete(account);
    }

    @Test
    public void testActionDelete() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        Action action = getAction(game);
        game.getActions().add(action);
        accountDao.saveOrUpdate(account);
        Long actionId = action.getActionId();
        actionDao.delete(action);
        Action byId = actionDao.findById(actionId);
        assertNull(byId);
        accountDao.delete(account);
    }

    @Test
    public void testActionList() throws Exception {
        Account account = getAccount();
        Game game1 = getGame(account);
        Game game2 = getGame(account);
        List<Game> games = account.getGames();
        games.add(game1);
        games.add(game2);
        Action action1 = getAction(game1);
        Action action2 = getAction(game1);
        Action action3 = getAction(game2);
        List<Action> game1Actions = game1.getActions();
        game1Actions.add(action1);
        game1Actions.add(action2);
        List<Action> game2Actions = game2.getActions();
        game2Actions.add(action3);
        accountDao.saveOrUpdate(account);
        List<Action> actionsForGame1 = actionDao.getActionsForGame(game1);
        List<Action> actionsForGame2 = actionDao.getActionsForGame(game2);
        List<Action> allActionList = actionDao.list();
        assertEquals(game1Actions.size(), actionsForGame1.size());
        assertEquals(game2Actions.size(), actionsForGame2.size());
        int allGames = game1Actions.size() + game2Actions.size();
        assertEquals(allGames, allActionList.size());
        accountDao.delete(account);
    }
}