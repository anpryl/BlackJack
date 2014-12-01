package com.prylutskyi.blackjack.dao;

import com.prylutskyi.blackjack.vo.Account;

import java.util.List;

/**
 * Created by Patap on 26.11.2014.
 */
public interface AccountDao {

    public List<Account> list();

    Account findById(long id);

    void saveOrUpdate(Account account);

    void delete(Account account);
}
