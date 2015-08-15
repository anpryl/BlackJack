package com.prylutskyi.blackjack.webservice;

import com.prylutskyi.blackjack.config.MainTestConfig;
import com.prylutskyi.blackjack.config.PersistenceConfig;
import com.prylutskyi.blackjack.config.ServicesConfig;
import com.prylutskyi.blackjack.config.WebServiceConfig;
import com.prylutskyi.blackjack.services.GameService;
import com.prylutskyi.blackjack.vo.Account;
import com.prylutskyi.blackjack.vo.Card;
import com.prylutskyi.blackjack.vo.Game;
import com.prylutskyi.blackjack.vo.GameStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.prylutskyi.blackjack.constants.TestConstants.JSON_FORMAT;
import static com.prylutskyi.blackjack.enumeration.Rank.*;
import static com.prylutskyi.blackjack.enumeration.Side.DEALER;
import static com.prylutskyi.blackjack.enumeration.Side.PLAYER;
import static com.prylutskyi.blackjack.enumeration.Suit.CLUBS;
import static com.prylutskyi.blackjack.enumeration.Suit.HEARTS;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class, ServicesConfig.class, WebServiceConfig.class, MainTestConfig.class})
@WebAppConfiguration
public class GameControllerTest {

    private GameService gameService = mock(GameService.class);

    private MockMvc mockGameController;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        reset(gameService);
        GameController gameController = new GameController();
        gameController.setGameService(gameService);
        mockGameController = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void testStartGame() throws Exception {
        GameStatus gameStatus = new GameStatus();
        List<Card> playerCards = gameStatus.getPlayerCards();
        playerCards.add(new Card(ACE, HEARTS));
        playerCards.add(new Card(SIX, HEARTS));
        List<Card> dealerCards = gameStatus.getDealerCards();
        dealerCards.add(new Card(KING, CLUBS));
        when(gameService.startGame(anyLong(), anyDouble())).thenReturn(gameStatus);
        String request = "/game/3/20";
        MockHttpServletRequestBuilder startGame = post(request);
        mockGameController.perform(startGame)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.playerCards.[0].rank").value(ACE.toString()))
                .andExpect(jsonPath("$.dealerCards.[0].rank").value(KING.toString()));
    }

    @Test
    public void testMakeBet() throws Exception {
        GameStatus gameStatus = new GameStatus();
        List<Card> playerCards = gameStatus.getPlayerCards();
        playerCards.add(new Card(ACE, HEARTS));
        playerCards.add(new Card(SIX, HEARTS));
        List<Card> dealerCards = gameStatus.getDealerCards();
        dealerCards.add(new Card(KING, CLUBS));
        when(gameService.makeBet(anyLong(), anyDouble())).thenReturn(gameStatus);
        String request = "/game/3/20";
        MockHttpServletRequestBuilder makeBet = put(request);
        mockGameController.perform(makeBet)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.playerCards.[0].rank").value(ACE.toString()))
                .andExpect(jsonPath("$.dealerCards.[0].rank").value(KING.toString()));
    }

    @Test
    public void testStand() throws Exception {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setWinner(DEALER);
        when(gameService.stand(anyLong())).thenReturn(gameStatus);
        String request = "/game/3/stand";
        MockHttpServletRequestBuilder stand = post(request);
        mockGameController.perform(stand)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.winner").value(DEALER.toString()));
    }

    @Test
    public void testHit() throws Exception {
        GameStatus gameStatus = new GameStatus();
        gameStatus.setWinner(PLAYER);
        when(gameService.hit(anyLong())).thenReturn(gameStatus);
        String request = "/game/3/hit";
        MockHttpServletRequestBuilder hit = post(request);
        mockGameController.perform(hit)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$.winner").value(PLAYER.toString()));
    }

    @Test
    public void testGetGamesForAccount() throws Exception {
        Account account = new Account();
        int accountId = 20;
        account.setAccountId((long) accountId);
        Game game1 = new Game();
        game1.setAccount(account);
        Game game2 = new Game();
        game2.setAccount(account);
        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        when(gameService.getGamesForAccount(accountId)).thenReturn(games);
        String request = "/game/" + accountId;
        MockHttpServletRequestBuilder getGamesForAccount = get(request);
        mockGameController.perform(getGamesForAccount)
                .andExpect(status().isOk())
                .andExpect(JSON_FORMAT)
                .andExpect(jsonPath("$[0].account.accountId").value(accountId))
                .andExpect(jsonPath("$[1].account.accountId").value(accountId));
    }
}