package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.engine.BlackJackEngine;
import com.prylutskyi.blackjack.enumeration.Side;
import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.vo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.prylutskyi.blackjack.constants.Messages.NEW_TABLE_PREPARED;
import static com.prylutskyi.blackjack.enumeration.Side.DEALER;
import static com.prylutskyi.blackjack.enumeration.Side.PLAYER;
import static com.prylutskyi.blackjack.utils.CardUtils.getShuffledDeck;
import static com.prylutskyi.blackjack.utils.CheckUtils.*;
import static com.prylutskyi.blackjack.utils.DoubleUtils.multiply;
import static com.prylutskyi.blackjack.utils.DoubleUtils.sum;

/**
 * Created by Patap on 29.11.2014.
 */
public class GameServiceImpl implements GameService {

    private AccountDao accountDao;

    private GameDao gameDao;

    private ActionDao actionDao;

    private BlackJackEngine blackJackEngine;

    private Map<Long, Table> tables = new ConcurrentHashMap<>();

    @Override
    public List<Game> getGamesForAccount(long accountId) {
        Account account = accountDao.findById(accountId);
        checkForNull(account, accountId);
        return gameDao.getGamesForAccount(account);
    }

    @Override
    public GameStatus startGame(long accountId, double bet) {
        Account account = accountDao.findById(accountId);
        checkForNull(account, accountId);
        double balance = account.getBalance();
        checkBetAndBalance(bet, balance);
        LOGGER.info("Starting new game for accountId=" + accountId);
        Game game = new Game();
        game.setAccount(account);
        gameDao.saveOrUpdate(game);

        List<Action> actions = new ArrayList<>();
        Table table = prepareTable(account, game);
        LOGGER.info("Prepare new " + table);
        Action action = new Action(NEW_TABLE_PREPARED);
        actions.add(action);
        tables.put(accountId, table);
        actions.addAll(blackJackEngine.makeBet(table, bet));
        saveAllActions(actions, game);
        checkForWinner(table);
        return table.getGameStatus();
    }

    @Override
    public GameStatus makeBet(long accountId, double bet) {
        Account account = accountDao.findById(accountId);
        checkForNull(account, accountId);
        Table table = tables.get(accountId);
        checkForNull(table, accountId);
        LOGGER.info("Account(Id=" + accountId + ") make bet " + bet);
        double balance = account.getBalance();
        checkBetAndBalance(bet, balance);

        Game game = table.getGame();
        List<Action> actions = blackJackEngine.makeBet(table, bet);
        saveAllActions(actions, game);
        checkForWinner(table);
        return table.getGameStatus();
    }

    @Override
    public GameStatus hit(long accountId) {
        Table table = tables.get(accountId);
        checkForNull(table, accountId);
        LOGGER.info("Account(Id=" + accountId + ") hit");
        Game game = table.getGame();
        List<Action> actions = blackJackEngine.playerHit(table);
        saveAllActions(actions, game);
        checkForWinner(table);
        return table.getGameStatus();
    }

    @Override
    public GameStatus stand(long accountId) {
        Table table = tables.get(accountId);
        checkForNull(table, accountId);
        LOGGER.info("Account(Id=" + accountId + ") stand");
        Game game = table.getGame();
        List<Action> actions = blackJackEngine.playerStand(table);
        saveAllActions(actions, game);
        checkForWinner(table);
        return table.getGameStatus();
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setGameDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public void setActionDao(ActionDao actionDao) {
        this.actionDao = actionDao;
    }

    public void setBlackJackEngine(BlackJackEngine blackJackEngine) {
        this.blackJackEngine = blackJackEngine;
    }

    private void checkForWinner(Table table) {
        Side winner = table.getGameStatus().getWinner();
        if (PLAYER.equals(winner)) {
            processPlayerWin(table);
        } else if (DEALER.equals(winner)) {
            processDealerWin(table);
        }
    }

    private void processDealerWin(Table table) {
        LOGGER.info("Account win with cards");
        double loss = getLoss(table);
        processAccountUpdate(table, loss);
    }

    private void processPlayerWin(Table table) {
        double prize = getPrize(table);
        processAccountUpdate(table, prize);
    }

    private void processAccountUpdate(Table table, double balanceChange) {
        Account account = table.getAccount();
        Double balance = account.getBalance();
        account.setBalance(sum(balance, balanceChange));
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperation(balanceChange);
        account.getTransactions().add(transaction);
        accountDao.saveOrUpdate(account);
        LOGGER.info("Account with id" + account.getAccountId() + " balance changed: " + balanceChange);
    }


    private double getLoss(Table table) {
        double multiplier = table.getMultiplier();
        double bet = table.getBet();
        return -(multiply(bet, multiplier));
    }

    private double getPrize(Table table) {
        double multiplier = table.getMultiplier();
        double bet = table.getBet();
        return multiply(bet, multiplier);
    }

    private void saveAllActions(List<Action> actions, Game game) {
        for (Action action : actions) {
            action.setGame(game);
            actionDao.saveOrUpdate(action);
        }
    }

    private Table prepareTable(Account account, Game game) {
        Table table = new Table(account, game);
        table.setDeck(getShuffledDeck());
        return table;
    }
}
