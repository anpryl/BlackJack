package com.prylutskyi.blackjack.utils;

import com.prylutskyi.blackjack.exceptions.*;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.Table;
import org.apache.log4j.Logger;

/**
 * Created by Patap on 29.11.2014.
 */
public class CheckUtils {

    public static final Logger LOGGER = Logger.getLogger(CheckUtils.class);

    private CheckUtils() {
    }

    public static void checkForNull(Account account, long accountId) {
        if (account == null) {
            LOGGER.info("Invalid account id " + accountId);
            throw new InvalidIdException("Can't find account with id " + accountId);
        }
    }

    public static void checkForNull(Game game, long gameId) {
        if (game == null) {
            LOGGER.info("Invalid game id " + gameId);
            throw new InvalidIdException("Can't find game with id " + gameId);
        }
    }

    public static void checkForNull(Table table, long accountId) {
        if (table == null) {
            LOGGER.info("No active table for account id " + accountId);
            throw new GameNotStartedException("Game have to be started at first");
        }
    }

    public static void checkIncreaseAmount(double amount) {
        if (amount <= 0) {
            LOGGER.info("Invalid balance increase " + amount);
            throw new InvalidBalanceException("Balance increase should be positive");
        }
    }

    public static void checkStartBalance(double startBalance) {
        if (startBalance < 0) {
            LOGGER.info("Invalid startBalance " + startBalance);
            throw new InvalidBalanceException("Balance should be positive or 0");
        }
    }

    public static void checkBetAndBalance(double bet, double balance) {
        if (bet > balance) {
            LOGGER.info("Bet greater than balance(" + bet + " > " + balance + ")");
            throw new NotEnoughMoneyForBetException("Your balance is " + balance + ", you can't bet more");
        }
        if (bet <= 0) {
            LOGGER.info("Bet should be positive, but now: " + bet);
            throw new InvalidBetException("Bet should be positive");
        }
    }

}
