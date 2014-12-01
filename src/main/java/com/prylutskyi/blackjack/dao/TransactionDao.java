package com.prylutskyi.blackjack.dao;

import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;

import java.util.List;

/**
 * Created by anatolii.prylutskyi on 11/28/2014.
 */
public interface TransactionDao {

    public List<Transaction> list();

    Transaction findById(long id);

    void saveOrUpdate(Transaction transaction);

    void delete(Transaction transaction);

    List<Transaction> getTransactionsForAccount(Account account);
}
