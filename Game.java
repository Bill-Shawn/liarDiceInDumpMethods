import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

public class Game {

    private int NUM_PLAYERS;
    private List<Player> playerList;
    private int challengerIndex = 0;
    private List<String> report; 
    private final Random random;

    // RANDOM FACTORS
    private int bidNumerator;
    private int bidDenominator;
    private int questionNumerator;
    private int questionDenominator;

    private int isOneCounted = 0;

    private boolean reloadGame = false;

    // data for each round
    private int enderId = 1;
    private int lastBidCount = 2;
    private int lastBidValue = 0;

    public Game(int i, int i1, int i2, int i3, int i4) {
        NUM_PLAYERS = i;
        this.random = new Random();
        playerList = new ArrayList<>();
        report = new ArrayList<>();
        setupPlayers(NUM_PLAYERS);
        bidNumerator = i1;
        bidDenominator = i2;
        questionNumerator = i3;
        questionDenominator = i4;

    }

    public void setupPlayers(int NUM_PLAYERS) {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            playerList.add(new Player(i + 1));
        }
    }

    public int[] makeBid(Player player) {

        enderId++;
        if (enderId >= NUM_PLAYERS)
        {
            enderId = enderId % NUM_PLAYERS + 1;
        }
        if (bidNumerator > random.nextInt(bidDenominator)){
            if(lastBidValue == 1)
            {
                isOneCounted = 1;
            }
            lastBidCount++;
            return new int[]{lastBidCount, lastBidValue}; 
        } else {
            int newbidValue = random.nextInt(6) + 1;
            while (newbidValue == lastBidValue)
            {
                newbidValue = random.nextInt(6) + 1;
            }
            lastBidValue = newbidValue;
            if(lastBidValue == 1)
            {
                isOneCounted = 1;
            }
            return new int[]{lastBidCount, newbidValue};
        }

    }

    public int[] makeQuestion() 
    {
        if (isOneCounted == 1)
        {

            reloadGame = true;
            if (enderId >= NUM_PLAYERS)
            {
                enderId = enderId % NUM_PLAYERS + 1;
            }
    
            int totalOccurrences = 0;

            if (lastBidValue == 1)
            {
                for (Player player : playerList) {
                    totalOccurrences += player.countDice(lastBidValue);
                }
            } else 
            {
                for (Player player : playerList) {
                    totalOccurrences += player.countDice(lastBidValue);
                    totalOccurrences += player.countDice(1);
                }
            }
    
            if (totalOccurrences >= lastBidCount) {
                // he wins
                return new int[]{enderId + 1, 1, lastBidCount};
            } else {
                // he drinks
                return new int[]{enderId + 1, 0, lastBidCount};        
            }


        } else {

            reloadGame = true;
            if (enderId >= NUM_PLAYERS)
            {
                enderId = enderId % NUM_PLAYERS + 1;
            }
    
            int totalOccurrences = 0;
            for (Player player : playerList) {
                totalOccurrences += player.countDice(lastBidValue);
            }

            if (totalOccurrences >= lastBidCount) {
                // he wins
                return new int[]{enderId + 1, 1, lastBidCount};
            } else {
                // he drinks
                return new int[]{enderId + 1, 0, lastBidCount};        
            }
        }

    }
    

    public int[] startGame()
    {
        reloadGame = false;
        
        // Re-roll the dice for all players
        for (Player player : playerList) {
            player.rollDice();
        }

        int[] resultForRounds = null;

        while (reloadGame != true)
        {

            if (questionNumerator <= random.nextInt(questionDenominator))
            {   
                makeBid(playerList.get(enderId));
            } else {
                resultForRounds = makeQuestion();
            }
        }
        return resultForRounds;
    }
     
}
