package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.constants.TestConstants.TEST_BALANCE;
import static com.prylutskyi.blackjack.constants.TestConstants.UPDATED_BALANCE;
import static com.prylutskyi.blackjack.utils.VoTestUtils.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class AccountDaoTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TransactionDao transactionDao;

    @Test
    public void testAccountCreate() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        assertNotNull(accountId);
        Account byId = accountDao.findById(accountId);
        assertEquals(byId.getBalance(), TEST_BALANCE, 0);
        accountDao.delete(account);
    }

    @Test
    public void testAccountDelete() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        assertNotNull(accountId);
        accountDao.delete(account);
        Account deletedAccount = accountDao.findById(accountId);
        assertNull(deletedAccount);
    }

    @Test
    public void testAccountUpdate() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        account.setBalance(UPDATED_BALANCE);
        accountDao.saveOrUpdate(account);
        Account updatedAccount = accountDao.findById(accountId);
        Double updatedBalance = updatedAccount.getBalance();
        assertEquals(UPDATED_BALANCE, updatedBalance.intValue(), 0);
        accountDao.delete(account);
    }

    @Test
    public void testAccountGameMapping() throws Exception {
        Account account = getAccount();
        Game game1 = getGame(account);
        Game game2 = getGame(account);
        List<Game> games = account.getGames();
        games.add(game1);
        games.add(game2);
        accountDao.saveOrUpdate(account);
        List<Game> gamesForAccount = gameDao.getGamesForAccount(account);
        assertEquals(games.size(), gamesForAccount.size());
        accountDao.delete(account);
    }

    @Test
    public void testAccountTransactionMapping() throws Exception {
        Account account = getAccount();
        Transaction transaction1 = getTransaction(account);
        Transaction transaction2 = getTransaction(account);
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction1);
        transactions.add(transaction2);
        accountDao.saveOrUpdate(account);
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(account);
        assertEquals(transactions.size(), transactionsForAccount.size());
        accountDao.delete(account);
    }

    @Test
    public void testAccountList() throws Exception {
        Account account1 = getAccount();
        Account account2 = getAccount();
        accountDao.saveOrUpdate(account1);
        accountDao.saveOrUpdate(account2);
        List<Account> list = accountDao.list();
        for (Account account : list) {
            System.out.println(account);
        }
        assertEquals(2, list.size());
        accountDao.delete(account1);
        accountDao.delete(account2);
    }
}