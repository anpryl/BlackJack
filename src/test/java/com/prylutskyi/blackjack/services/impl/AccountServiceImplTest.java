package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.exceptions.InvalidBalanceException;
import com.prylutskyi.blackjack.exceptions.InvalidIdException;
import com.prylutskyi.blackjack.services.AccountService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.constants.TestConstants.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class AccountServiceImplTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionDao transactionDao;

    @Test
    public void testCreateAccount() throws Exception {
        Account account = accountService.createAccount();
        Long accountId = account.getAccountId();
        assertNotNull(accountId);
        Account accountFromDao = accountDao.findById(accountId);
        double balance = accountFromDao.getBalance();
        assertEquals(0, balance, 0);
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(account);
        assertEquals(1, transactionsForAccount.size());
        Transaction transaction = transactionsForAccount.get(0);
        double operation = transaction.getOperation();
        assertEquals(EMPTY_BALANCE, operation, EMPTY_BALANCE);
        accountDao.delete(accountFromDao);
    }

    @Test
    public void testCreateAccountWithBalance() throws Exception {
        Account account = accountService.createAccount(TEST_BALANCE);
        assertNotNull(account.getAccountId());
        Account byId = accountDao.findById(account.getAccountId());
        double balance = byId.getBalance();
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(account);
        assertEquals(1, transactionsForAccount.size());
        Transaction transaction = transactionsForAccount.get(0);
        double operation = transaction.getOperation();

        assertEquals(TEST_BALANCE, balance, 0);
        assertEquals(TEST_BALANCE, operation, 0);
        accountDao.delete(account);
    }

    @Test(expected = InvalidBalanceException.class)
    public void testCreateAccountWithNegativeBalance() throws Exception {
        Account account = accountService.createAccount(NEGATIVE_BALANCE);
        assertNotNull(account.getAccountId());
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = accountService.createAccount();
        Account accountById = accountService.getAccountById(account.getAccountId());
        assertNotNull(accountById);
        accountDao.delete(account);
    }

    @Test
    public void testGetAccountByInvalidId() throws Exception {
        Account accountById = accountService.getAccountById(Long.MAX_VALUE);
        assertNull(accountById);
    }

    @Test
    public void testIncreaseBalance() throws Exception {
        Account account = accountService.createAccount(EMPTY_BALANCE);
        Long accountId = account.getAccountId();
        accountService.increaseBalance(accountId, TEST_BALANCE);
        Account accountById = accountService.getAccountById(accountId);
        double balance = accountById.getBalance();
        double updatedBalance = EMPTY_BALANCE + TEST_BALANCE;
        List<Transaction> transactionsForAccount = transactionDao.getTransactionsForAccount(account);
        assertEquals(2, transactionsForAccount.size());
        Transaction transaction1 = transactionsForAccount.get(0);
        double operation1 = transaction1.getOperation();
        Transaction transaction2 = transactionsForAccount.get(1);
        double operation2 = transaction2.getOperation();

        assertEquals(updatedBalance, balance, 0);
        assertEquals(EMPTY_BALANCE, operation1, 0);
        assertEquals(updatedBalance, operation2, 0);
        accountDao.delete(account);
    }

    @Test(expected = InvalidIdException.class)
    public void testIncreaseBalanceInvalidId() throws Exception {
        accountService.increaseBalance(Long.MAX_VALUE, TEST_BALANCE);
    }

    @Test(expected = InvalidBalanceException.class)
    public void testIncreaseNegativeBalance() throws Exception {
        Account account = accountService.createAccount(EMPTY_BALANCE);
        Long accountId = account.getAccountId();
        try {
            accountService.increaseBalance(accountId, NEGATIVE_BALANCE);
        } finally {
            accountDao.delete(account);
        }
    }
}