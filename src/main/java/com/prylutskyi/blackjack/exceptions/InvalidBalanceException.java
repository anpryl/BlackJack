package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 29.11.2014.
 */
public class InvalidBalanceException extends BlackJackAbstractException {
    private static final long serialVersionUID = 993761154240367322L;

    public InvalidBalanceException(String message) {
        super(message);
    }
}
