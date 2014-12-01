package com.prylutskyi.blackjack.services.impl;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.services.AccountService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import static com.prylutskyi.blackjack.utils.CheckUtils.*;
import static com.prylutskyi.blackjack.utils.DoubleUtils.sum;

/**
 * Created by Patap on 26.11.2014.
 */
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = Logger.getLogger(AccountServiceImpl.class);

    private AccountDao accountDao;

    private TransactionDao transactionDao;

    @Override
    public Account createAccount() {
        return createAccount(0);
    }

    @Override
    public Account createAccount(double startBalance) {
        checkStartBalance(startBalance);
        Account account = new Account();
        account.setBalance(startBalance);
        LOGGER.info("Creating account with " + startBalance + " start balance");
        accountDao.saveOrUpdate(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperation(startBalance);
        transaction.setComment("Start balance");
        transactionDao.saveOrUpdate(transaction);
        return account;
    }

    @Override
    public Account getAccountById(long accountId) {
        return accountDao.findById(accountId);
    }

    @Override
    public void increaseBalance(long accountId, double amount) {
        checkIncreaseAmount(amount);
        Account account = accountDao.findById(accountId);
        checkForNull(account, accountId);
        Double balance = account.getBalance();
        account.setBalance(sum(balance, amount));
        LOGGER.info("Adding " + amount + " to balance, account id " + accountId);
        accountDao.saveOrUpdate(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setOperation(amount);
        transaction.setComment("Increase balance");
        transactionDao.saveOrUpdate(transaction);
    }

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }
}
