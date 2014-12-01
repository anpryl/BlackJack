package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.config.GameServiceTestConfig;
import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.exceptions.GameNotStartedException;
import com.prylutskyi.blackjack.exceptions.InvalidBetException;
import com.prylutskyi.blackjack.exceptions.InvalidIdException;
import com.prylutskyi.blackjack.exceptions.NotEnoughMoneyForBetException;
import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.vo.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.constants.Messages.NEW_TABLE_PREPARED;
import static com.prylutskyi.blackjack.constants.TestConstants.*;
import static com.prylutskyi.blackjack.utils.VoTestUtils.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, GameServiceTestConfig.class, MainTestConfig.class})
@Transactional
public class GameServiceImplTest {

    public static final double TEST_BET = TEST_BALANCE;
    @Autowired
    private GameDao gameDao;

    @Autowired
    private ActionDao actionDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private GameService gameService;


    @Test
    public void testGetGamesForAccount() throws Exception {
        Account account = getAccount();
        Game game1 = getGame(account);
        Game game2 = getGame(account);
        List<Game> games = account.getGames();
        games.add(game1);
        games.add(game2);
        accountDao.saveOrUpdate(account);
        List<Game> gamesForAccount = gameService.getGamesForAccount(account.getAccountId());
        assertEquals(games, gamesForAccount);
        accountDao.delete(account);
    }

    @Test(expected = InvalidIdException.class)
    public void testGetActionsForGameInvalidGameId() throws Exception {
        gameService.getGamesForAccount(Long.MAX_VALUE);
    }

    @Test
    public void testStartGameAndPlayerWin() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        GameStatus gameStatus = gameService.startGame(accountId, TEST_BET);
        List<Game> gamesForAccount = gameDao.getGamesForAccount(account);
        int gamesSize = gamesForAccount.size();
        Game game = gamesForAccount.get(0);
        List<Action> actionsForGame = actionDao.getActionsForGame(game);
        Account byId = accountDao.findById(accountId);
        double balance = byId.getBalance();
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(account);

        assertNotNull(gameStatus);
        assertTrue(gamesSize == 1);
        checkForActionWithLog(actionsForGame, NEW_TABLE_PREPARED);
        checkForActionWithLog(actionsForGame, TEST_BET_LOG);
        checkForActionWithLog(actionsForGame, TEST_PLAYER_WIN_LOG);
        checkForTransactionOperation(transactionsForAccount, TEST_BET);
        assertEquals(TEST_BALANCE + TEST_BET, balance, TEST_BET);
        accountDao.delete(account);
    }

    @Test(expected = NotEnoughMoneyForBetException.class)
    public void testStartGameBetMoreThanBalance() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.startGame(accountId, TEST_BET + TEST_BET);
    }

    @Test
    public void testMakeBet() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.startGame(accountId, TEST_BET);
        GameStatus gameStatus = gameService.makeBet(accountId, TEST_BET);
        List<Game> gamesForAccount = gameDao.getGamesForAccount(account);
        int gamesSize = gamesForAccount.size();
        Game game = gamesForAccount.get(0);
        List<Action> actionsForGame = actionDao.getActionsForGame(game);

        assertNotNull(gameStatus);
        assertTrue(gamesSize == 1);
        checkForActionWithLog(actionsForGame, NEW_TABLE_PREPARED);
        checkForActionWithLog(actionsForGame, TEST_BET_LOG);
        checkForActionWithLog(actionsForGame, TEST_BET_LOG);
        accountDao.delete(account);
    }

    @Test(expected = GameNotStartedException.class)
    public void testMakeBetGameNotStarted() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.makeBet(accountId, TEST_BET);
    }

    @Test(expected = InvalidBetException.class)
    public void testMakeInvalidBet() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.startGame(accountId, TEST_BET);
        try {
            gameService.makeBet(accountId, NEGATIVE_BALANCE);
        } finally {
            accountDao.delete(account);
        }
    }

    @Test
    public void testHit() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.startGame(accountId, TEST_BET);

        GameStatus gameStatus = gameService.hit(accountId);
        List<Game> gamesForAccount = gameDao.getGamesForAccount(account);
        int gamesSize = gamesForAccount.size();
        Game game = gamesForAccount.get(0);
        List<Action> actionsForGame = actionDao.getActionsForGame(game);

        assertNotNull(gameStatus);
        assertTrue(gamesSize == 1);
        checkForActionWithLog(actionsForGame, TEST_HIT_LOG);
        accountDao.delete(account);
    }

    @Test(expected = GameNotStartedException.class)
    public void testHitGameNotStarted() throws Exception {
        long accountId = Long.MAX_VALUE;
        gameService.hit(accountId);
    }

    @Test
    public void testStand() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        gameService.startGame(accountId, TEST_BET);
        GameStatus gameStatus = gameService.stand(accountId);
        List<Game> gamesForAccount = gameDao.getGamesForAccount(account);
        int gamesSize = gamesForAccount.size();
        Game game = gamesForAccount.get(0);
        List<Action> actionsForGame = actionDao.getActionsForGame(game);

        assertNotNull(gameStatus);
        assertTrue(gamesSize == 1);
        checkForActionWithLog(actionsForGame, NEW_TABLE_PREPARED);
        checkForActionWithLog(actionsForGame, TEST_STAND_LOG);
        accountDao.delete(account);
    }

    @Test(expected = GameNotStartedException.class)
    public void testStandGameNotStarted() throws Exception {
        long accountId = Long.MAX_VALUE;
        gameService.stand(accountId);
    }

}