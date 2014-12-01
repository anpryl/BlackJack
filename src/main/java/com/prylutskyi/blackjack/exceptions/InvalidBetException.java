package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 30.11.2014.
 */
public class InvalidBetException extends BlackJackAbstractException {
    private static final long serialVersionUID = 8139157923696229096L;

    public InvalidBetException(String message) {
        super(message);
    }
}
