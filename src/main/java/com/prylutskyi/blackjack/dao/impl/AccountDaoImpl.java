package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.vo.Account;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Patap on 27.11.2014.
 */
public class AccountDaoImpl implements AccountDao {

    private static final Logger LOGGER = Logger.getLogger(AccountDaoImpl.class);

    private SessionFactory sessionFactory;

    public AccountDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Account> list() {
        List<Account> accounts = (List<Account>) getCurrentSession()
                .createCriteria(Account.class).list();
        LOGGER.info("Find " + accounts.size() + " accounts in database");
        return accounts;
    }

    @Override
    public Account findById(long id) {
        LOGGER.info("Looking for account with id " + id);
        Account account = (Account) getCurrentSession().get(Account.class, id);
        LOGGER.info("Find " + account);
        return account;
    }

    @Override
    public void saveOrUpdate(Account account) {
        LOGGER.info("Save/Update " + account);
        getCurrentSession().saveOrUpdate(account);
    }

    @Override
    public void delete(Account account) {
        LOGGER.info("Delete " + account);
        getCurrentSession().delete(account);
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
