package com.prylutskyi.blackjack.config;

import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.ActionDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.dao.TransactionDao;
import com.prylutskyi.blackjack.engine.BlackJackEngine;
import com.prylutskyi.blackjack.engine.impl.BlackJackEngineImpl;
import com.prylutskyi.blackjack.services.AccountService;
import com.prylutskyi.blackjack.services.ActionService;
import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.services.TransactionService;
import com.prylutskyi.blackjack.services.impl.AccountServiceImpl;
import com.prylutskyi.blackjack.services.impl.ActionServiceImpl;
import com.prylutskyi.blackjack.services.impl.GameServiceImpl;
import com.prylutskyi.blackjack.services.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Patap on 27.11.2014.
 */

@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class ServicesConfig {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ActionDao actionDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private TransactionDao transactionDao;


    @Bean
    public AccountService accountService() {
        AccountServiceImpl accountService = new AccountServiceImpl();
        accountService.setAccountDao(accountDao);
        accountService.setTransactionDao(transactionDao);
        return accountService;
    }

    @Bean
    public ActionService actionService() {
        ActionServiceImpl actionService = new ActionServiceImpl();
        actionService.setActionDao(actionDao);
        actionService.setGameDao(gameDao);
        return actionService;
    }

    @Bean
    public TransactionService transactionService() {
        TransactionServiceImpl transactionService = new TransactionServiceImpl();
        transactionService.setTransactionDao(transactionDao);
        transactionService.setAccountDao(accountDao);
        return transactionService;
    }

    @Bean
    public GameService gameService() {
        GameServiceImpl gameService = new GameServiceImpl();
        gameService.setAccountDao(accountDao);
        gameService.setActionDao(actionDao);
        gameService.setGameDao(gameDao);
        gameService.setBlackJackEngine(blackJackEngine());
        return gameService;
    }

    @Bean
    public BlackJackEngine blackJackEngine() {
        return new BlackJackEngineImpl();
    }
}