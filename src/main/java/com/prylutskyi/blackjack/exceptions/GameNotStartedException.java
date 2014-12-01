package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 30.11.2014.
 */
public class GameNotStartedException extends BlackJackAbstractException {
    private static final long serialVersionUID = 7307421145713414272L;

    public GameNotStartedException(String message) {
        super(message);
    }
}
