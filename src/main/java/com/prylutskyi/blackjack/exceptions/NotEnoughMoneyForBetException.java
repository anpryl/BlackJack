package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 29.11.2014.
 */
public class NotEnoughMoneyForBetException extends BlackJackAbstractException {
    private static final long serialVersionUID = 4181927252976887026L;

    public NotEnoughMoneyForBetException(String message) {
        super(message);
    }
}
