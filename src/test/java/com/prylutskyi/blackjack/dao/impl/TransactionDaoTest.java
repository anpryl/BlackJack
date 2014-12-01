package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prylutskyi.blackjack.utils.VoTestUtils.getAccount;
import static com.prylutskyi.blackjack.utils.VoTestUtils.getTransaction;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class TransactionDaoTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionDao transactionDao;

    @Test
    public void testTransactionCreate() throws Exception {
        Account account = getAccount();
        accountDao.saveOrUpdate(account);
        Transaction transaction = getTransaction(account);
        transactionDao.saveOrUpdate(transaction);
        Long transactionId = transaction.getTransactionId();
        assertNotNull(transactionId);
        Transaction byId = transactionDao.findById(transactionId);
        assertEquals(transaction.getAccount(), byId.getAccount());
        assertEquals(transaction.getComment(), byId.getComment());
        assertEquals(transaction.getDate(), byId.getDate());
        assertEquals(transaction.getOperation(), byId.getOperation());
        accountDao.delete(account);
    }

    @Test
    public void testTransactionDelete() throws Exception {
        Account account = getAccount();
        Transaction transaction = getTransaction(account);
        accountDao.saveOrUpdate(account);
        transactionDao.saveOrUpdate(transaction);
        transactionDao.delete(transaction);
        Transaction byId = transactionDao.findById(transaction.getTransactionId());
        assertNull(byId);
        accountDao.delete(account);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Account account = getAccount();
        Transaction transaction = getTransaction(account);
        accountDao.saveOrUpdate(account);
        transactionDao.saveOrUpdate(transaction);
    }

    @Test
    public void testTransactionList() throws Exception {
        Account account1 = getAccount();
        Account account2 = getAccount();
        Transaction transaction1 = getTransaction(account1);
        Transaction transaction2 = getTransaction(account2);
        Transaction transaction3 = getTransaction(account2);
        List<Transaction> account1Transactions = account1.getTransactions();
        account1Transactions.add(transaction1);
        List<Transaction> account2Transactions = account2.getTransactions();
        account2Transactions.add(transaction2);
        account2Transactions.add(transaction3);
        accountDao.saveOrUpdate(account1);
        accountDao.saveOrUpdate(account2);
        List<Transaction> transactionsForAccount1 = transactionDao.getTransactionsForAccount(account1);
        List<Transaction> transactionsForAccount2 = transactionDao.getTransactionsForAccount(account2);
        List<Transaction> allTransactionsList = transactionDao.list();
        assertEquals(account1Transactions.size(), transactionsForAccount1.size());
        assertEquals(account2Transactions.size(), transactionsForAccount2.size());
        int allTransactions = account1Transactions.size() + account2Transactions.size();
        assertEquals(allTransactions, allTransactionsList.size());
        accountDao.delete(account1);
        accountDao.delete(account2);
    }
}