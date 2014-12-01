package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.exceptions.InvalidIdException;
import com.prylutskyi.blackjack.services.TransactionService;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, MainTestConfig.class})
@Transactional
public class TransactionServiceImplTest {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransactionService transactionService;

    @Test
    public void testGetActionsForGame() throws Exception {
        Account account = getAccount();
        Transaction transaction1 = getTransaction(account);
        Transaction transaction2 = getTransaction(account);
        List<Transaction> transactions = account.getTransactions();
        transactions.add(transaction1);
        transactions.add(transaction2);
        accountDao.saveOrUpdate(account);
        List<Transaction> transactionsForAccount = transactionService.getTransactionsForAccount(account.getAccountId());
        assertEquals(transactions, transactionsForAccount);
        accountDao.delete(account);
    }


    @Test(expected = InvalidIdException.class)
    public void testGetActionsForGameInvalidGameId() throws Exception {
        transactionService.getTransactionsForAccount(Long.MAX_VALUE);
    }

}