package com.prylutskyi.blackjack.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Patap on 26.11.2014.
 */
public class Game implements Serializable {

    private static final long serialVersionUID = 2073745231260050963L;

    private Long gameId;

    private Date startTime = new Date();

    private Account account;

    private List<Action> actions = new ArrayList<>();

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (account != null ? !account.equals(game.account) : game.account != null) return false;
        return !(gameId != null ? !gameId.equals(game.gameId) : game.gameId != null);

    }

    @Override
    public int hashCode() {
        int result = gameId != null ? gameId.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", startTime=" + startTime +
                ", account=" + account +
                '}';
    }
}
