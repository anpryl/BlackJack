package com.prylutskyi.blackjack.utils;

import com.prylutskyi.blackjack.enumeration.Rank;
import com.prylutskyi.blackjack.enumeration.Suit;
import com.prylutskyi.blackjack.vo.Card;

import java.util.*;

/**
 * Created by Patap on 30.11.2014.
 */
public class CardUtils {

    private static final List<Card> aces = getAces();

    private CardUtils() {
    }

    public static Deque<Card> getShuffledDeck() {
        LinkedList<Card> deck = new LinkedList<>();
        Suit[] suits = Suit.values();
        Rank[] ranks = Rank.values();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                deck.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    public static List<Card> getAces() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.CLUBS);
        Card card3 = new Card(Rank.ACE, Suit.CLUBS);
        Card card4 = new Card(Rank.ACE, Suit.CLUBS);
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        return cards;
    }

    public static boolean containsAce(List<Card> cards) {
        return !Collections.disjoint(cards, aces);
    }
}
