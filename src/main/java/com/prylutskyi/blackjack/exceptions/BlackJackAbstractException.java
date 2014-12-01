package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 29.11.2014.
 */
public abstract class BlackJackAbstractException extends RuntimeException {
    private static final long serialVersionUID = -3084864122698639778L;

    public BlackJackAbstractException(String message) {
        super(message);
    }
}
