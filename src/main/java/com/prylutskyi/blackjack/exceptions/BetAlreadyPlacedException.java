package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 30.11.2014.
 */
public class BetAlreadyPlacedException extends BlackJackAbstractException {
    private static final long serialVersionUID = -7873960546770200065L;

    public BetAlreadyPlacedException(String message) {
        super(message);
    }
}
