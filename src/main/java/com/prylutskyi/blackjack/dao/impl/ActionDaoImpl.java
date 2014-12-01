package com.prylutskyi.blackjack.dao.impl;

import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Patap on 29.11.2014.
 */
public class ActionDaoImpl implements ActionDao {

    private static final Logger LOGGER = Logger.getLogger(ActionDaoImpl.class);

    private SessionFactory sessionFactory;

    public ActionDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Action> list() {
        List<Action> actions = (List<Action>) getCurrentSession()
                .createCriteria(Action.class).list();
        LOGGER.info("Find " + actions.size() + " actions in database");
        return actions;
    }

    @Override
    public Action findById(long id) {
        Action action = (Action) getCurrentSession().get(Action.class, id);
        LOGGER.info("Find " + action);
        return action;
    }

    @Override
    public void saveOrUpdate(Action action) {
        LOGGER.info("Save/update " + action);
        getCurrentSession().saveOrUpdate(action);
    }

    @Override
    public void delete(Action action) {
        LOGGER.info("Delete " + action);
        getCurrentSession().delete(action);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Action> getActionsForGame(Game game) {
        List<Action> actions = (List<Action>) getCurrentSession()
                .createCriteria(Action.class).add(Restrictions.eq("game", game)).list();
        LOGGER.info("Find " + actions.size() + " actions for " + game);
        return actions;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
