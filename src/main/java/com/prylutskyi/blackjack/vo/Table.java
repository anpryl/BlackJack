package com.prylutskyi.blackjack.vo;

import com.prylutskyi.blackjack.enumeration.Rank;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static com.prylutskyi.blackjack.constants.BlackJackEngineConfig.WINNING_POINTS;
import static com.prylutskyi.blackjack.enumeration.Rank.ACE;
import static com.prylutskyi.blackjack.utils.CardUtils.containsAce;

/**
 * Created by Patap on 26.11.2014.
 */
public class Table implements Serializable {

    private static final long serialVersionUID = 3241283785031415979L;

    private final Account account;

    private final Game game;

    private GameStatus gameStatus;

    private Card hiddenCard;

    private Deque<Card> deck = new LinkedList<>();

    private List<Card> usedCards = new ArrayList<>();

    private double bet;

    private double multiplier = 1;

    public Table(Account account, Game game) {
        this.account = account;
        this.game = game;
    }

    public Account getAccount() {
        return account;
    }

    public Deque<Card> getDeck() {
        return deck;
    }

    public void setDeck(Deque<Card> deck) {
        this.deck = deck;
    }

    public List<Card> getUsedCards() {
        return usedCards;
    }

    public double getBet() {
        return bet;
    }

    public void setBet(double bet) {
        this.bet = bet;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Game getGame() {
        return game;
    }

    public Card getHiddenCard() {
        return hiddenCard;
    }

    public void setHiddenCard(Card hiddenCard) {
        this.hiddenCard = hiddenCard;
    }

    public int getDealerPoints() {
        List<Card> dealerCards = gameStatus.getDealerCards();
        return countCards(dealerCards);
    }

    public int getPlayerPoints() {
        List<Card> playerCards = gameStatus.getPlayerCards();
        return countCards(playerCards);
    }

    @Override
    public String toString() {
        return "Table{" +
                "accountId=" + account.getAccountId() +
                ", gameId=" + game.getGameId() +
                '}';
    }

    private int countCards(List<Card> cards) {
        int points = 0;
        for (Card card : cards) {
            Rank rank = card.getRank();
            points += rank.getPrice();
        }
        if (points > WINNING_POINTS && containsAce(cards)) {
            int difference = ACE.getPrice() - ACE.getAlternativeAcePrice();
            points -= difference;
        }
        return points;
    }
}
