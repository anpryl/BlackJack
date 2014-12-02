package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.config.WebServiceConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.dao.GameDao;
import com.prylutskyi.blackjack.services.ActionService;
import com.prylutskyi.blackjack.services.TransactionService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Action;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.prylutskyi.blackjack.constants.TestConstants.JSON_FORMAT;
import static com.prylutskyi.blackjack.constants.TestConstants.TEST_BALANCE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, WebServiceConfig.class, MainTestConfig.class})
@WebAppConfiguration
@Transactional
public class TransactionAndActionControllerTest {

    @Autowired
    private ActionService actionService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private GameDao gameDao;

    private MockMvc mockTransactionActionController;


    @Test
    public void testGetTransactionsForAccount() throws Exception {
        Account account = new Account();
        account.setBalance(TEST_BALANCE);
        Transaction transaction = new Transaction();
        account.getTransactions().add(transaction);
        transaction.setAccount(account);
        transaction.setOperation(TEST_BALANCE);
        accountDao.saveOrUpdate(account);
        String request = "/transactions/" + account.getAccountId();
        MockHttpServletRequestBuilder getTransactions = get(request);
        mockTransactionActionController.perform(getTransactions)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$[0].account.accountId").value((int) account.getAccountId().longValue()));
        accountDao.delete(account);
    }

    @Test
    public void testGetActionsForGame() throws Exception {
        Account account = new Account();
        account.setBalance(TEST_BALANCE);
        accountDao.saveOrUpdate(account);
        Game game = new Game();
        game.setAccount(account);
        Action action = new Action("Test log");
        action.setGame(game);
        game.getActions().add(action);
        gameDao.saveOrUpdate(game);
        String request = "/actions/" + game.getGameId();
        MockHttpServletRequestBuilder getActions = get(request);
        mockTransactionActionController.perform(getActions)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$[0].game.gameId").value((int) game.getGameId().longValue()));
        accountDao.delete(account);
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TransactionAndActionController transactionAndActionController = new TransactionAndActionController();
        transactionAndActionController.setActionService(actionService);
        transactionAndActionController.setTransactionService(transactionService);
        mockTransactionActionController = MockMvcBuilders.standaloneSetup(transactionAndActionController).build();
    }
}