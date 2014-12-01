package com.prylutskyi.blackjack.vo;

import com.prylutskyi.blackjack.enumeration.Rank;
import com.prylutskyi.blackjack.enumeration.Suit;

import java.io.Serializable;

/**
 * Created by Patap on 26.11.2014.
 */
public class Card implements Serializable {

    private static final long serialVersionUID = 30611031844432424L;

    private final Rank rank;

    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (rank != card.rank) return false;
        if (suit != card.suit) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rank != null ? rank.hashCode() : 0;
        result = 31 * result + (suit != null ? suit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
    }
}
