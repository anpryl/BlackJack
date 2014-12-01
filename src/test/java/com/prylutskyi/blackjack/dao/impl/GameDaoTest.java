package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Game;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.utils.VoTestUtils.getAccount;
import static com.prylutskyi.blackjack.utils.VoTestUtils.getGame;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class GameDaoTest {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private AccountDao accountDao;

    @Test
    public void testGameSave() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Game game = getGame(account);
        gameDao.saveOrUpdate(game);
        Long gameId = game.getGameId();
        assertNotNull(gameId);
        Game byId = gameDao.findById(gameId);
        assertEquals(byId.getStartTime(), game.getStartTime());
        accountDao.delete(account);
    }

    @Test
    public void testGameUpdate() throws Exception {
        Account account1 = getAccount();
        Account account2 = getAccount();
        accountDao.saveOrUpdate(account1);
        accountDao.saveOrUpdate(account2);
        Game game = getGame(account1);
        gameDao.saveOrUpdate(game);
        Long gameId = game.getGameId();
        game.setAccount(account2);
        gameDao.saveOrUpdate(game);
        Game gameByID = gameDao.findById(gameId);
        Account updatedAccount = gameByID.getAccount();
        assertNotEquals(account1, updatedAccount);
        assertEquals(account2, updatedAccount);
        accountDao.delete(account1);
        accountDao.delete(account2);
    }

    @Test
    public void testGameDelete() throws Exception {
        Account account = getAccount();
        Game game = getGame(account);
        account.getGames().add(game);
        accountDao.saveOrUpdate(account);
        Long gameId = game.getGameId();
        gameDao.delete(game);
        Game gameById = gameDao.findById(gameId);
        assertNull(gameById);
        accountDao.delete(account);
    }

    @Test
    public void testGameList() throws Exception {
        Account account1 = getAccount();
        Account account2 = getAccount();
        Game game1 = getGame(account1);
        Game game2 = getGame(account2);
        Game game3 = getGame(account2);
        List<Game> account1Games = account1.getGames();
        account1Games.add(game1);
        List<Game> account2Games = account2.getGames();
        account2Games.add(game2);
        account2Games.add(game3);
        accountDao.saveOrUpdate(account1);
        accountDao.saveOrUpdate(account2);
        List<Game> gamesForAccount1 = gameDao.getGamesForAccount(account1);
        List<Game> gamesForAccount2 = gameDao.getGamesForAccount(account2);
        List<Game> allGamesList = gameDao.list();
        assertEquals(account1Games.size(), gamesForAccount1.size());
        assertEquals(account2Games.size(), gamesForAccount2.size());
        int allGames = account1Games.size() + account2Games.size();
        assertEquals(allGames, allGamesList.size());
        accountDao.delete(account1);
        accountDao.delete(account2);
    }
}