package com.prylutskyi.blackjack.services;

import com.prylutskyi.blackjack.vo.Account;

/**
 * Created by Patap on 26.11.2014.
 */
public interface AccountService {

    Account createAccount();

    Account createAccount(double startBalance);

    Account getAccountById(long accountId);

    void increaseBalance(long accountId, double amount);
}
