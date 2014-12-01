package com.prylutskyi.blackjack.engine.impl;

import com.prylutskyi.blackjack.constants.Messages;
import com.prylutskyi.blackjack.engine.BlackJackEngine;
import com.prylutskyi.blackjack.enumeration.Side;
import com.prylutskyi.blackjack.exceptions.BetAlreadyPlacedException;
import com.prylutskyi.blackjack.exceptions.BetNotPlacedException;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Card;
import com.prylutskyi.blackjack.vo.GameStatus;
import com.prylutskyi.blackjack.vo.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import static com.prylutskyi.blackjack.constants.BlackJackEngineConfig.*;
import static com.prylutskyi.blackjack.enumeration.Side.*;

/**
 * Created by Patap on 29.11.2014.
 */
public class BlackJackEngineImpl implements BlackJackEngine {

    @Override
    public List<Action> makeBet(Table table, double bet) {
        if (isBetPlaced(table)) {
            throw new BetAlreadyPlacedException(Messages.BET_ALREADY_PLACED);
        }
        resetTable(table, bet);
        List<Action> actions = new ArrayList<>();
        actions.add(new Action("Player make bet " + table.getBet()));
        actions.addAll(firstDeal(table));
        actions.addAll(checkForBlackJack(table));
        return actions;
    }

    @Override
    public List<Action> playerHit(Table table) {
        if (!isBetPlaced(table)) {
            throw new BetNotPlacedException("You have to place bet before first hit");
        }
        List<Action> actions = takeCardFromDeckFor(PLAYER, table);
        int playerPoints = table.getPlayerPoints();
        if (playerPoints >= WINNING_POINTS) {
            actions.addAll(playerStand(table));
        }
        return actions;
    }

    @Override
    public List<Action> playerStand(Table table) {
        if (!isBetPlaced(table)) {
            throw new BetNotPlacedException(Messages.BET_NOT_PLACED);
        }
        List<Action> actions = dealerMove(table);
        actions.addAll(decideWhoWon(table));
        return actions;
    }

    private void resetTable(Table table, double bet) {
        table.setMultiplier(DEFAULT_MULTIPLIER);
        table.setBet(bet);
        table.setGameStatus(new GameStatus());
        table.setHiddenCard(null);
    }

    private boolean isBetPlaced(Table table) {
        return table.getHiddenCard() != null;
    }

    private List<Action> firstDeal(Table table) {
        List<Action> actions = takeCardFromDeckFor(PLAYER, table);
        actions.addAll(takeCardFromDeckFor(PLAYER, table));
        actions.addAll(takeCardFromDeckFor(DEALER, table));
        actions.addAll(takeHiddenCardForDealer(table));
        return actions;
    }

    private List<Action> takeHiddenCardForDealer(Table table) {
        List<Action> actions = new ArrayList<>();
        Card hiddenCard = getNextCard(table, actions);
        table.setHiddenCard(hiddenCard);
        actions.add(new Action("Dealer get hidden card " + hiddenCard));
        return actions;
    }

    private List<Action> takeCardFromDeckFor(Side side, Table table) {
        List<Action> actions = new ArrayList<>();
        Card card = getNextCard(table, actions);
        GameStatus gameStatus = table.getGameStatus();
        if (PLAYER.equals(side)) {
            actions.add(new Action("Player get " + card));
            gameStatus.getPlayerCards().add(card);
        } else if (DEALER.equals(side)) {
            actions.add(new Action("Dealer get " + card));
            gameStatus.getDealerCards().add(card);
        }
        return actions;
    }

    private Card getNextCard(Table table, List<Action> actions) {
        Deque<Card> deck = table.getDeck();
        Card card = deck.pollFirst();
        if (card == null) {
            actions.add(new Action(Messages.DECK_ENDED));
            shuffleUsedCardsAndAddToTheDeck(table);
            card = deck.pollFirst();
        }
        return card;
    }

    private void shuffleUsedCardsAndAddToTheDeck(Table table) {
        List<Card> usedCards = table.getUsedCards();
        Collections.shuffle(usedCards);
        table.getDeck().addAll(usedCards);
        usedCards.clear();
    }

    private List<Action> checkForBlackJack(Table table) {
        List<Action> actions = new ArrayList<>();
        int playerPoints = table.getPlayerPoints();
        if (playerPoints == WINNING_POINTS) {
            table.setMultiplier(BLACKJACK_MULTIPLIER);
            Long accountId = table.getAccount().getAccountId();
            actions.add(new Action("BlackJack for accountId=" + accountId));
            actions.addAll(playerStand(table));
        }
        return actions;
    }

    private List<Action> dealerMove(Table table) {
        List<Action> actions = new ArrayList<>();
        openDealerHiddenCard(table);
        int dealerPoints = table.getDealerPoints();
        if (isPlayerBusted(table)) {
            return actions;
        }
        while (dealerPoints <= DEALER_LIMIT) {
            actions.addAll(takeCardFromDeckFor(DEALER, table));
            dealerPoints = table.getDealerPoints();
        }
        return actions;
    }

    private List<Action> decideWhoWon(Table table) {
        if (isPlayerBusted(table)) {
            return playerBusted(table);
        } else if (isDealerBusted(table)) {
            return playerWon(table);
        } else {
            return compareResults(table);
        }
    }

    private List<Action> compareResults(Table table) {
        int playerPoints = table.getPlayerPoints();
        int dealerPoints = table.getDealerPoints();
        if (playerPoints > dealerPoints) {
            return playerWon(table);
        }
        if (playerPoints < dealerPoints) {
            return dealerWon(table);
        }
        return draw(table);
    }

    private List<Action> playerBusted(Table table) {
        if (isDealerBusted(table)) {
            return draw(table);
        } else {
            return dealerWon(table);
        }
    }

    private boolean isDealerBusted(Table table) {
        int dealerPoints = table.getDealerPoints();
        return dealerPoints > WINNING_POINTS;
    }

    private boolean isPlayerBusted(Table table) {
        int playerPoints = table.getPlayerPoints();
        return playerPoints > WINNING_POINTS;
    }

    private List<Action> playerWon(Table table) {
        List<Action> actions = new ArrayList<>();
        GameStatus gameStatus = table.getGameStatus();
        List<Card> playerCards = gameStatus.getPlayerCards();
        List<Card> dealerCards = gameStatus.getDealerCards();
        int playerPoints = table.getPlayerPoints();
        int dealerPoints = table.getDealerPoints();
        actions.add(new Action("Player won with " + playerPoints + ". " + playerCards));
        actions.add(new Action("Dealer lost with " + dealerPoints + ". " + dealerCards));
        gameStatus.setWinner(PLAYER);
        moveAllCardsToUsed(table);
        return actions;
    }

    private List<Action> dealerWon(Table table) {
        List<Action> actions = new ArrayList<>();
        GameStatus gameStatus = table.getGameStatus();
        List<Card> playerCards = gameStatus.getPlayerCards();
        List<Card> dealerCards = gameStatus.getDealerCards();
        int playerPoints = table.getPlayerPoints();
        int dealerPoints = table.getDealerPoints();
        actions.add(new Action("Player lost with " + playerPoints + ". " + playerCards));
        actions.add(new Action("Dealer won with " + dealerPoints + ". " + dealerCards));
        gameStatus.setWinner(DEALER);
        moveAllCardsToUsed(table);
        return actions;
    }

    private List<Action> draw(Table table) {
        List<Action> actions = new ArrayList<>();
        GameStatus gameStatus = table.getGameStatus();
        List<Card> playerCards = gameStatus.getPlayerCards();
        List<Card> dealerCards = gameStatus.getDealerCards();
        int playerPoints = table.getPlayerPoints();
        int dealerPoints = table.getDealerPoints();
        actions.add(new Action("Push between player and dealer (" + playerPoints + ":" + dealerPoints + ")"));
        actions.add(new Action("Player cards:" + playerCards));
        actions.add(new Action("Dealer cards:" + dealerCards));
        gameStatus.setWinner(DRAW);
        moveAllCardsToUsed(table);
        return actions;
    }

    private void moveAllCardsToUsed(Table table) {
        List<Card> usedCards = table.getUsedCards();
        usedCards.addAll(table.getGameStatus().getDealerCards());
        usedCards.addAll(table.getGameStatus().getPlayerCards());
    }

    private void openDealerHiddenCard(Table table) {
        Card hiddenCard = table.getHiddenCard();
        List<Card> dealerCards = table.getGameStatus().getDealerCards();
        dealerCards.add(hiddenCard);
        table.setHiddenCard(null);
    }
}
