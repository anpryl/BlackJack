package com.prylutskyi.blackjack.vo;

import com.prylutskyi.blackjack.enumeration.Side;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patap on 29.11.2014.
 */
public class GameStatus implements Serializable {

    private static final long serialVersionUID = -8432723008713154711L;

    private Side winner;

    private List<Card> playerCards = new ArrayList<>();

    private List<Card> dealerCards = new ArrayList<>();

    public Side getWinner() {
        return winner;
    }

    public void setWinner(Side winner) {
        this.winner = winner;
    }

    public List<Card> getPlayerCards() {
        return playerCards;
    }

    public List<Card> getDealerCards() {
        return dealerCards;
    }
}
