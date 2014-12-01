package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 29.11.2014.
 */
public class InvalidIdException extends BlackJackAbstractException {
    private static final long serialVersionUID = -6849074183917097360L;

    public InvalidIdException(String message) {
        super(message);
    }
}
