package com.prylutskyi.blackjack.enumeration;

/**
 * Created by Patap on 26.11.2014.
 */
public enum Rank {

    TWO("Two", 2), THREE("Three", 3), FOUR("Four", 4), FIVE("Five", 5), SIX("Six", 6), SEVEN("Seven", 7), EIGHT("Eight", 8),
    NINE("Nine", 9), TEN("Ten", 10), JACK("Jack", 10), QUEEN("Queen", 10), KING("King", 10), ACE("Ace", 11);

    private final int price;
    private final String rank;

    Rank(String rank, int price) {
        this.price = price;
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public int getPrice() {
        return price;
    }

    public int getAlternativeAcePrice() {
        return 1;
    }

}
