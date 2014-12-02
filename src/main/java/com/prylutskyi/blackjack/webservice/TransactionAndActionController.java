package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.services.ActionService;
import com.prylutskyi.blackjack.services.TransactionService;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Patap on 02.12.2014.
 */

@RestController
@RequestMapping(produces = "application/json")
@Transactional
public class TransactionAndActionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ActionService actionService;

    @RequestMapping(value = "/transactions/{accountId}", method = RequestMethod.GET)
    public List<Transaction> getTransactionsForAccount(@PathVariable long accountId) {
        return transactionService.getTransactionsForAccount(accountId);
    }

    @RequestMapping(value = "/actions/{gameId}", method = RequestMethod.GET)
    public List<Action> getActionsForGame(@PathVariable long gameId) {
        return actionService.getActionsForGame(gameId);
    }

    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void setActionService(ActionService actionService) {
        this.actionService = actionService;
    }
}
