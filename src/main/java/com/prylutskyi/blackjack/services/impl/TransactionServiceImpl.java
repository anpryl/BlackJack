package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.services.TransactionService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;

import java.util.List;

import static com.prylutskyi.blackjack.utils.CheckUtils.checkForNull;

/**
 * Created by Patap on 29.11.2014.
 */
public class TransactionServiceImpl implements TransactionService {

    private AccountDao accountDao;

    private TransactionDao transactionDao;

    @Override
    public List<Transaction> getTransactionsForAccount(long accountId) {
        Account account = accountDao.findById(accountId);
        checkForNull(account, accountId);
        return transactionDao.getTransactionsForAccount(account);
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
}
