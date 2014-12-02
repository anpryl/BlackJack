package com.prylutskyi.blackjack.services;

import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.GameStatus;

import java.util.List;

/**
 * Created by Patap on 26.11.2014.
 */
public interface GameService {

    List<Game> getGamesForAccount(long accountId);

    GameStatus startGame(long accountId, double bet);

    GameStatus makeBet(long accountId, double bet);

    GameStatus hit(long accountId);

    GameStatus stand(long accountId);
}
