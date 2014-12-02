package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Patap on 01.12.2014.
 */
@RestController
@RequestMapping(value = "/game/{accountId}", produces = "application/json")
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/startgame/{bet}", method = RequestMethod.POST)
    public GameStatus startGame(@PathVariable long accountId, @PathVariable double bet) {
        return gameService.startGame(accountId, bet);
    }

    @RequestMapping(value = "/bet/{bet}", method = RequestMethod.POST)
    public GameStatus makeBet(@PathVariable long accountId, @PathVariable double bet) {
        return gameService.makeBet(accountId, bet);
    }

    @RequestMapping(value = "/stand", method = RequestMethod.POST)
    public GameStatus stand(@PathVariable long accountId) {
        return gameService.stand(accountId);
    }

    @RequestMapping(value = "/hit", method = RequestMethod.POST)
    public GameStatus hit(@PathVariable long accountId) {
        return gameService.hit(accountId);
    }

    @RequestMapping(value = "/games", method = RequestMethod.GET)
    public List<Game> getGamesForAccount(@PathVariable long accountId) {
        return gameService.getGamesForAccount(accountId);
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
