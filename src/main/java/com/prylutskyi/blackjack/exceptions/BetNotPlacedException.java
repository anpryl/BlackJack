package com.prylutskyi.blackjack.exceptions;

/**
 * Created by Patap on 01.12.2014.
 */
public class BetNotPlacedException extends BlackJackAbstractException {
    private static final long serialVersionUID = -5670853426923483863L;

    public BetNotPlacedException(String message) {
        super(message);
    }
}
