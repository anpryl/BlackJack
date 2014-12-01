package com.prylutskyi.blackjack.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patap on 26.11.2014.
 */
public class Action implements Serializable {

    private static final long serialVersionUID = 393270411804797559L;

    private Long actionId;

    private Date date = new Date();

    private Game game;

    private String log;

    public Action(String log) {
        this.log = log;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action actionLog = (Action) o;

        if (date != null ? !date.equals(actionLog.date) : actionLog.date != null) return false;
        if (game != null ? !game.equals(actionLog.game) : actionLog.game != null) return false;
        if (actionId != null ? !actionId.equals(actionLog.actionId) : actionLog.actionId != null) return false;
        return !(log != null ? !log.equals(actionLog.log) : actionLog.log != null);

    }

    @Override
    public int hashCode() {
        int result = actionId != null ? actionId.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (game != null ? game.hashCode() : 0);
        result = 31 * result + (log != null ? log.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Action{" +
                "actionId=" + actionId +
                ", date=" + date +
                ", log='" + log + '\'' +
                '}';
    }
}
