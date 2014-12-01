package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.enumeration.Rank;
import com.prylutskyi.blackjack.enumeration.Side;
import com.prylutskyi.blackjack.enumeration.Suit;
import com.prylutskyi.blackjack.vo.Card;
import com.prylutskyi.blackjack.vo.GameStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Patap on 01.12.2014.
 */
@RestController
public class BlackJackController {

    public GameStatus helloWorld() {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setWinner(Side.PLAYER);
        gameStatus.getDealerCards().add(new Card(Rank.SEVEN, Suit.DIAMONDS));
        gameStatus.getPlayerCards().add(new Card(Rank.SIX, Suit.HEARTS));
        return gameStatus;
    }

    @RequestMapping(value = "/vasya", method = RequestMethod.GET)
    public String helloWord2() {
        return "HELLO";
    }

}
