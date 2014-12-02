package com.prylutskyi.blackjack.engine.impl;

import com.prylutskyi.blackjack.enumeration.Side;
import com.prylutskyi.blackjack.exceptions.BetAlreadyPlacedException;
import com.prylutskyi.blackjack.exceptions.BetNotPlacedException;
import com.prylutskyi.blackjack.vo.*;
import org.junit.Test;

import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.prylutskyi.blackjack.constants.BlackJackEngineConfig.BLACKJACK_MULTIPLIER;
import static com.prylutskyi.blackjack.constants.BlackJackEngineConfig.DEFAULT_MULTIPLIER;
import static com.prylutskyi.blackjack.constants.TestConstants.FULL_DECK_SIZE;
import static com.prylutskyi.blackjack.enumeration.Rank.*;
import static com.prylutskyi.blackjack.enumeration.Side.*;
import static com.prylutskyi.blackjack.enumeration.Suit.*;
import static com.prylutskyi.blackjack.services.impl.GameServiceImplTest.TEST_BET;
import static com.prylutskyi.blackjack.utils.CardUtils.getShuffledDeck;
import static org.junit.Assert.*;

public class BlackJackEngineImplTest {

    private BlackJackEngineImpl engine = new BlackJackEngineImpl();

    @Test
    public void testMakeBet() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(TWO, CLUBS);
        Card card2 = new Card(THREE, CLUBS);
        Card card3 = new Card(TEN, CLUBS);
        Card card4 = new Card(QUEEN, CLUBS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);
        engine.makeBet(table, TEST_BET);
        Card hiddenCard = table.getHiddenCard();
        GameStatus gameStatus = table.getGameStatus();
        List<Card> dealerCards = gameStatus.getDealerCards();
        List<Card> playerCards = gameStatus.getPlayerCards();
        Side winner = gameStatus.getWinner();

        assertTrue(winner == null);
        assertTrue(dealerCards.contains(card3));
        assertEquals(1, dealerCards.size());
        assertTrue(playerCards.contains(card1));
        assertTrue(playerCards.contains(card2));
        assertEquals(2, playerCards.size());
        assertTrue(deck.isEmpty());
        assertNotNull(hiddenCard);
        assertEquals(card4, hiddenCard);
    }

    @Test(expected = BetAlreadyPlacedException.class)
    public void testMakeBetTwoTimesPerDealFail() throws Exception {
        Table table = prepareTableWithDeck();
        engine.makeBet(table, TEST_BET);
        engine.makeBet(table, TEST_BET);
        engine.makeBet(table, TEST_BET);
    }

    @Test
    public void testMakeBetBlackJackPlayerWins() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(ACE, CLUBS);
        Card card2 = new Card(KING, CLUBS);
        Card card3 = new Card(TEN, CLUBS);
        Card card4 = new Card(QUEEN, CLUBS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);

        engine.makeBet(table, TEST_BET);
        Card hiddenCard = table.getHiddenCard();
        GameStatus gameStatus = table.getGameStatus();
        List<Card> dealerCards = gameStatus.getDealerCards();
        Side winner = gameStatus.getWinner();
        double multiplier = table.getMultiplier();
        List<Card> usedCards = table.getUsedCards();

        assertTrue(PLAYER.equals(winner));
        assertEquals(2, dealerCards.size());
        assertEquals(BLACKJACK_MULTIPLIER, multiplier, 0);
        assertNull(hiddenCard);
        assertEquals(4, usedCards.size());
    }

    @Test
    public void testMakeBetBlackJackDraw() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(ACE, CLUBS);
        Card card2 = new Card(KING, CLUBS);
        Card card3 = new Card(FIVE, CLUBS);
        Card card4 = new Card(TWO, HEARTS);
        Card card5 = new Card(THREE, HEARTS);
        Card card6 = new Card(ACE, HEARTS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);
        deck.addLast(card5);
        deck.addLast(card6);
        engine.makeBet(table, TEST_BET);
        Card hiddenCard = table.getHiddenCard();
        GameStatus gameStatus = table.getGameStatus();
        Side winner = gameStatus.getWinner();

        assertTrue(DRAW.equals(winner));
        assertNull(hiddenCard);
    }

    @Test
    public void testPlayerHitAndBustedDealerPlaysWithStartedCardsOnly() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(JACK, CLUBS);
        Card card2 = new Card(KING, CLUBS);
        Card card3 = new Card(FIVE, CLUBS);
        Card card4 = new Card(EIGHT, HEARTS);
        Card card5 = new Card(THREE, HEARTS);
        Card card6 = new Card(ACE, HEARTS);
        Card card7 = new Card(ACE, DIAMONDS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);
        deck.addLast(card5);
        deck.addLast(card6);
        deck.addLast(card7);

        engine.makeBet(table, TEST_BET);
        engine.playerHit(table);

        GameStatus gameStatus = table.getGameStatus();
        Side winner = gameStatus.getWinner();
        List<Card> dealerCards = gameStatus.getDealerCards();
        double multiplier = table.getMultiplier();

        assertEquals(DEALER, winner);
        assertEquals(DEFAULT_MULTIPLIER, multiplier, 0);
        assertEquals(2, dealerCards.size());
    }

    @Test(expected = BetNotPlacedException.class)
    public void testPlayerHitBetNotPlaced() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        engine.playerHit(table);
    }

    @Test
    public void testPlayerStandAfterFewHits() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(FIVE, CLUBS);
        Card card2 = new Card(THREE, CLUBS);
        Card card3 = new Card(FOUR, HEARTS);
        Card card4 = new Card(ACE, HEARTS);
        Card card5 = new Card(ACE, CLUBS);
        Card card6 = new Card(ACE, SPADES);
        Card card7 = new Card(ACE, DIAMONDS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);
        deck.addLast(card5);
        deck.addLast(card6);
        deck.addLast(card7);

        engine.makeBet(table, TEST_BET);
        engine.playerHit(table);
        engine.playerHit(table);
        engine.playerStand(table);

        GameStatus gameStatus = table.getGameStatus();
        Side winner = gameStatus.getWinner();
        double multiplier = table.getMultiplier();

        assertEquals(PLAYER, winner);
        assertEquals(DEFAULT_MULTIPLIER, multiplier, 0);
    }


    @Test
    public void testPlayerStandAfterFirstDealAndWon() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        Card card1 = new Card(JACK, CLUBS);
        Card card2 = new Card(KING, CLUBS);
        Card card3 = new Card(KING, HEARTS);
        Card card4 = new Card(NINE, DIAMONDS);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);

        engine.makeBet(table, TEST_BET);
        engine.playerStand(table);

        GameStatus gameStatus = table.getGameStatus();
        Side winner = gameStatus.getWinner();
        List<Card> playerCards = gameStatus.getPlayerCards();

        assertEquals(2, playerCards.size());
        assertEquals(PLAYER, winner);
    }


    @Test
    public void testPlayerStandDealerBustTakeCardFromUsed() throws Exception {
        Table table = prepareTableWithEmptyDeck();
        Deque<Card> deck = table.getDeck();
        List<Card> usedCards = table.getUsedCards();
        Card card1 = new Card(QUEEN, CLUBS);
        Card card2 = new Card(KING, CLUBS);
        Card card3 = new Card(FIVE, CLUBS);
        Card card4 = new Card(FOUR, HEARTS);
        Card card5 = new Card(SEVEN, HEARTS);
        Card card6 = new Card(SEVEN, SPADES);
        deck.addLast(card1);
        deck.addLast(card2);
        deck.addLast(card3);
        deck.addLast(card4);
        usedCards.add(card5);
        usedCards.add(card6);
        engine.makeBet(table, TEST_BET);
        engine.playerStand(table);
        Card hiddenCard = table.getHiddenCard();
        GameStatus gameStatus = table.getGameStatus();
        Side winner = gameStatus.getWinner();
        double multiplier = table.getMultiplier();

        assertTrue(PLAYER.equals(winner));
        assertEquals(DEFAULT_MULTIPLIER, multiplier, 0);
        assertNull(hiddenCard);
    }

    @Test
    public void testDeckShuffler() throws Exception {
        Deque<Card> shuffledDeck = getShuffledDeck();
        Set<Card> deckSet = new HashSet<>();
        deckSet.addAll(shuffledDeck);//For uniqueness test
        assertEquals(shuffledDeck.size(), deckSet.size());
        assertEquals(FULL_DECK_SIZE, shuffledDeck.size(), 0);
    }

    private Table prepareTableWithEmptyDeck() {
        return new Table(new Account(), new Game());
    }

    private Table prepareTableWithDeck() {
        Table table = new Table(new Account(), new Game());
        table.setBet(TEST_BET);
        table.setGameStatus(new GameStatus());
        table.setDeck(getShuffledDeck());
        return table;
    }
}