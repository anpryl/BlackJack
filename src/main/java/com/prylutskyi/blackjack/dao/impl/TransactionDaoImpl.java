package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Transaction;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Patap on 28.11.2014.
 */
public class TransactionDaoImpl implements TransactionDao {

    private static final Logger LOGGER = Logger.getLogger(TransactionDaoImpl.class);

    private SessionFactory sessionFactory;

    public TransactionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> list() {
        List<Transaction> transactions = (List<Transaction>) getCurrentSession()
                .createCriteria(Transaction.class).list();
        LOGGER.info("Find " + transactions.size() + " transactions in database");
        return transactions;
    }

    @Override
    public Transaction findById(long id) {
        Transaction transaction = (Transaction) getCurrentSession().get(Transaction.class, id);
        LOGGER.info("Find " + transaction);
        return transaction;
    }

    @Override
    public void saveOrUpdate(Transaction transaction) {
        LOGGER.info("Save/update " + transaction);
        getCurrentSession().saveOrUpdate(transaction);
    }

    @Override
    public void delete(Transaction transaction) {
        LOGGER.info("Delete " + transaction);
        getCurrentSession().delete(transaction);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> getTransactionsForAccount(Account account) {
        List<Transaction> transactions = (List<Transaction>) getCurrentSession()
                .createCriteria(Transaction.class).add(Restrictions.eq("account", account)).list();
        LOGGER.info("Find " + transactions.size() + " transactions in database for " + account);
        return transactions;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
