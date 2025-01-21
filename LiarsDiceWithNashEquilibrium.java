import java.util.*;

public class LiarsDiceWithNashEquilibrium {
    private static final int TOTAL_DICE = 5; // Dice per player
    private static final int TOTAL_PLAYERS = 3; // Number of players

    public static void main(String[] args) {
        // Initialize game
        Random random = new Random();
        int[][] dice = new int[TOTAL_PLAYERS][TOTAL_DICE];
        
        for (int player = 0; player < TOTAL_PLAYERS; player++) {
            for (int d = 0; d < TOTAL_DICE; d++) {
                dice[player][d] = random.nextInt(6) + 1; // Roll dice (1-6)
            }
        }

        // Simulate the game with Nash equilibrium strategy
        playGameWithNashEquilibrium(dice);
    }

    private static void playGameWithNashEquilibrium(int[][] dice) {
        int currentPlayer = 0;
        int lastBidCount = 0;
        int lastBidValue = 0;

        while (true) {
            System.out.println("Player " + (currentPlayer + 1) + "'s turn.");
            printPlayerDice(dice[currentPlayer]);

            if (shouldChallenge(dice, lastBidCount, lastBidValue)) {
                System.out.println("Player " + (currentPlayer + 1) + " challenges the bid!");
                boolean result = verifyBid(dice, lastBidCount, lastBidValue);
                System.out.println(result ? "The bid was correct!" : "The bid was incorrect!");
                break;
            } else {
                int[] newBid = makeNashEquilibriumBid(dice, currentPlayer, lastBidCount, lastBidValue);
                lastBidCount = newBid[0];
                lastBidValue = newBid[1];
                System.out.println("Player " + (currentPlayer + 1) + " bids " + lastBidCount + " " + lastBidValue + "s.");
            }

            currentPlayer = (currentPlayer + 1) % TOTAL_PLAYERS;
        }
    }

    private static void printPlayerDice(int[] dice) {
        System.out.print("Your dice: ");
        for (int die : dice) {
            System.out.print(die + " ");
        }
        System.out.println();
    }

    private static boolean shouldChallenge(int[][] dice, int lastBidCount, int lastBidValue) {
        int totalDice = dice.length * dice[0].length;
        int estimatedOccurrences = estimateOccurrences(dice, lastBidValue);
        return lastBidCount > estimatedOccurrences + (totalDice / 6); // Challenge if bid seems too high
    }

    private static int[] makeNashEquilibriumBid(int[][] dice, int currentPlayer, int lastBidCount, int lastBidValue) {
        int[] frequency = new int[7]; // Index 0 is unused; 1-6 for dice faces
        for (int die : dice[currentPlayer]) {
            frequency[die]++;
        }

        int totalDice = dice.length * dice[0].length;
        int estimatedOccurrences = estimateOccurrences(dice, lastBidValue);
        int safeBidCount = Math.max(lastBidCount, estimatedOccurrences);
        
        for (int face = lastBidValue; face <= 6; face++) {
            if (frequency[face] > 0 && (safeBidCount <= estimatedOccurrences)) {
                return new int[]{safeBidCount + 1, face};
            }
        }

        return new int[]{lastBidCount + 1, lastBidValue};
    }

    private static int estimateOccurrences(int[][] dice, int value) {
        int total = 0;
        for (int[] playerDice : dice) {
            for (int die : playerDice) {
                if (die == value || die == 1) { // Assume 1 is wild
                    total++;
                }
            }
        }
        return total;
    }

    private static boolean verifyBid(int[][] dice, int count, int value) {
        int actualCount = 0;
        for (int[] playerDice : dice) {
            for (int die : playerDice) {
                if (die == value || die == 1) {
                    actualCount++;
                }
            }
        }
        return actualCount >= count;
    }
}
