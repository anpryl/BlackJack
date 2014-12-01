package com.prylutskyi.blackjack.dao;

import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Game;

import java.util.List;

/**
 * Created by Patap on 27.11.2014.
 */
public interface GameDao {

    public List<Game> list();

    Game findById(long id);

    void saveOrUpdate(Game game);

    void delete(Game game);

    List<Game> getGamesForAccount(Account account);
}
