package com.prylutskyi.blackjack.config;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.engine.BlackJackEngine;
import com.prylutskyi.blackjack.engine.impl.StubBlackJackEngine;
import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.services.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Patap on 29.11.2014.
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class GameServiceTestConfig {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ActionDao actionDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TransactionDao transactionDao;

    @Bean
    public GameService gameService() {
        GameServiceImpl gameService = new GameServiceImpl();
        gameService.setAccountDao(accountDao);
        gameService.setActionDao(actionDao);
        gameService.setGameDao(gameDao);
        gameService.setTransactionDao(transactionDao);
        gameService.setBlackJackEngine(blackJackEngineForTest());
        return gameService;
    }

    @Bean
    public BlackJackEngine blackJackEngineForTest() {
        return new StubBlackJackEngine();
    }

}
