BlackJack
=========

BlackJack RESTful webservice

Technologies used: Spring core, Spring boot, Spring MVC, Hibernate
Database: HSQLDB

In-memory HSQLDB for testing and embedded for work. Database startup automatically with application and hold data in folder BlackJackDB.

If you have installed gradle than you can build runnable jar with "gradle jar", war with "gradle war" or both with "gradle build".
Also you can run application without build with "gradle bootRun".
If you need gradle you can run gradlew.bat, it will download and install gradle.

Address of application: localhost:8080

List of commands:

/account + 

      /create - (POST_METHOD) - return created Account with 0 balance
    
      /create/{startBalance} - (POST_METHOD) - return created Account with {startBalance}
    
      /addmoney/{accountId}/{amount} - (PUT_METHOD)  - add {amount} to Account's({accountID}) balance
    
      /get{accountId} - (GET_METHOD)  - return Account info by {accountId}

/game/{accountId} + 
   
      /startgame/{bet} - (POST_METHOD) -  prepares new table for new player {accountId} with {bet},
      you need to click this only once per session, but you can always start new game. 
      Returns GameStatus with your cards, dealer's cards and information about winner, if someone wins.
    
      /bet/{bet} - (POST_METHOD) - playses new {bet}, you need to do this only whe some one win or it's draw. 
      You don't need to start game every time someone won. Returns GameStatus
    
      /hit/ - (POST_METHOD) - after first deal you can take next card from deck("hit") or you have enough
      cards and you decide to stop("stand"). Returns GameStatus
   
      /stand/ - (POST_METHOD) - when you decide to stop take cards from deck you say "Stand" and dealer 
      starts his move. Returns GameStatus                                       
    
      /games/ - (GET_METHOD) - returns all games for {accountId}
    

/transactions/{accoundId} - (GET_METHOD) - returns all transactions for {accountId}

/actions/{gameId} - (GET_METHOD) - returns all actions for specific game by {gameId}

Examples:
/localhost:8080/create/1000  - you will get JSON file with accountId there, for example - 1
/localhost:8080/game/1/hit    
/localhost:8080/game/1/stand  
/localhost:8080/game/1/bet/50        
/localhost:8080/transactions/1      
