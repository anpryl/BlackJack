package com.prylutskyi.blackjack.vo;

/**
 * Created by Patap on 02.12.2014.
 */
public class ErrorInfo {
    public final String url;
    public final String ex;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.ex = ex.getMessage();
    }
}
