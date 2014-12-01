package com.prylutskyi.blackjack.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Patap on 26.11.2014.
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = -5349270715913903123L;

    private Long transactionId;

    private Account account;

    private String comment;

    private Double operation;

    private Date date = new Date();

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getOperation() {
        return operation;
    }

    public void setOperation(Double operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        return !(transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null);

    }

    @Override
    public int hashCode() {
        int result = transactionId != null ? transactionId.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
