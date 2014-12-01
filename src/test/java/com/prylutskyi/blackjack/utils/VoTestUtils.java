package com.prylutskyi.blackjack.utils;

import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.Transaction;

import java.util.List;

import static com.prylutskyi.blackjack.constants.TestConstants.*;
import static org.junit.Assert.assertTrue;

/**
 * Created by Patap on 28.11.2014.
 */
public class VoTestUtils {

    public static Account getAccount() {
        Account account = new Account();
        account.setBalance(TEST_BALANCE);
        return account;
    }

    public static Game getGame(Account account) {
        Game game = new Game();
        game.setAccount(account);
        return game;
    }

    public static Transaction getTransaction(Account account) {
        Transaction transaction = new Transaction();
        transaction.setOperation(NEGATIVE_BALANCE);
        transaction.setAccount(account);
        transaction.setComment(TEST_COMMENT);
        return transaction;
    }

    public static Action getAction(Game game) {
        Action action = new Action(TEST_LOG);
        action.setGame(game);
        return action;
    }

    public static void checkForTransactionOperation(List<Transaction> transactions, double operationForSearch) {
        boolean isOperationFound = false;
        for (Transaction transaction : transactions) {
            double operation = transaction.getOperation();
            if (operation == operationForSearch) {
                transactions.remove(transaction);
                isOperationFound = true;
                break;
            }
        }
        assertTrue(isOperationFound);
    }

    public static void checkForActionWithLog(List<Action> actionsForGame, String logMessage) {
        boolean isActionFound = false;
        for (Action action : actionsForGame) {
            String log = action.getLog();
            if (logMessage.equals(log)) {
                actionsForGame.remove(action);
                isActionFound = true;
                break;
            }
        }
        assertTrue(isActionFound);
    }
}
