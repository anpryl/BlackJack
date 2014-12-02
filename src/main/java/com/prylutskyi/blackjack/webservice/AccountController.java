package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.services.AccountService;
import com.prylutskyi.blackjack.vo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Patap on 01.12.2014.
 */
@RestController
@RequestMapping(value = "/account", produces = "application/json")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Account createAccount() {
        return accountService.createAccount();
    }

    @RequestMapping(value = "/create/{startBalance}", method = RequestMethod.POST)
    public Account createAccount(@PathVariable double startBalance) {
        return accountService.createAccount(startBalance);
    }

    @RequestMapping(value = "/addmoney/{accountId}/{amount}", method = RequestMethod.PUT)
    public void addFunds(@PathVariable long accountId, @PathVariable double amount) {
        accountService.increaseBalance(accountId, amount);
    }

    @RequestMapping(value = "/get{accountId}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable long accountId) {
        return accountService.getAccountById(accountId);
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
