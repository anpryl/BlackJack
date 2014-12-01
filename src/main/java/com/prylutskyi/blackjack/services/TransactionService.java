package com.prylutskyi.blackjack.services;

import com.prylutskyi.blackjack.vo.Transaction;

import java.util.List;

/**
 * Created by Patap on 26.11.2014.
 */
public interface TransactionService {

    List<Transaction> getTransactionsForAccount(long accountId);

}
