package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Game;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Patap on 27.11.2014.
 */
public class GameDaoImpl implements GameDao {

    private static final Logger LOGGER = Logger.getLogger(GameDaoImpl.class);

    private SessionFactory sessionFactory;

    public GameDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Game> list() {
        List<Game> games = (List<Game>) getCurrentSession()
                .createCriteria(Game.class).list();
        LOGGER.info("Find " + games.size() + " games in database");
        return games;
    }

    @Override
    public Game findById(long id) {
        Game game = (Game) getCurrentSession().get(Game.class, id);
        LOGGER.info("Find " + game);
        return game;
    }

    @Override
    public void saveOrUpdate(Game game) {
        LOGGER.info("Save/update " + game);
        getCurrentSession().saveOrUpdate(game);
    }

    @Override
    public void delete(Game game) {
        LOGGER.info("Delete " + game);
        getCurrentSession().delete(game);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Game> getGamesForAccount(Account account) {
        List<Game> games = (List<Game>) getCurrentSession()
                .createCriteria(Game.class).add(Restrictions.eq("account", account)).list();
        LOGGER.info("Find " + games.size() + " games in database for " + account);
        return games;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
