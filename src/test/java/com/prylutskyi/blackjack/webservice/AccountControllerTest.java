package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.config.WebServiceConfig;
import com.prylutskyi.blackjack.dao.AccountDao;
import com.prylutskyi.blackjack.services.AccountService;
import com.prylutskyi.blackjack.vo.Account;
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

import static com.prylutskyi.blackjack.constants.TestConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, WebServiceConfig.class, MainTestConfig.class})
@WebAppConfiguration
@Transactional
public class AccountControllerTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDao accountDao;

    private MockMvc mockAccountController;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        AccountController accountController = new AccountController();
        accountController.setAccountService(accountService);
        mockAccountController = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        String request = "/account/create";
        MockHttpServletRequestBuilder creageAccount = post(request);
        mockAccountController.perform(creageAccount)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.accountId").exists())
                .andExpect(jsonPath("$.balance").value(is(EMPTY_BALANCE)));
    }

    @Test
    public void testCreateAccountWithBalance() throws Exception {
        String request = "/account/create/" + TEST_BALANCE;
        MockHttpServletRequestBuilder creageAccount = post(request);
        mockAccountController.perform(creageAccount)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.accountId").exists())
                .andExpect(jsonPath("$.balance").value(is(TEST_BALANCE)));
    }

    @Test
    public void testAddFunds() throws Exception {
        Account account = new Account();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        String request = "/account/addFunds/" + accountId + "/" + TEST_BALANCE;
        MockHttpServletRequestBuilder addFunds = put(request);
        mockAccountController.perform(addFunds).andExpect(status().isOk());
        Account byId = accountDao.findById(accountId);
        double balance = byId.getBalance();
        assertEquals(TEST_BALANCE, balance, 0);
        accountDao.delete(account);
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = new Account();
        accountDao.saveOrUpdate(account);
        Long accountId = account.getAccountId();
        String request = "/account/get" + accountId;
        MockHttpServletRequestBuilder getAccount = get(request);
        mockAccountController.perform(getAccount)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.balance").value(is(EMPTY_BALANCE)));
        accountDao.delete(account);
    }
}