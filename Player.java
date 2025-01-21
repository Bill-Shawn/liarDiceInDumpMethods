import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private final int id;
    private List<Integer> dice;
    public boolean oneCounted;
    private int diceCount = 5;
    private static final int NUM_FACES = 6;

    public Player(int id) {
        this.id = id;
        this.dice = generateRandomDice(diceCount);
        this.oneCounted = true;
    }

    public int getId() {
        return id;
    }

    public void setDice(List<Integer> dice) {
        this.dice.clear();
        this.dice.addAll(dice);
    }

    public boolean getOneCounted() {
        return oneCounted;
    }

    public List<Integer> getDice() {
        return dice;
    }

    public void rollDice() {
        dice.clear();
        Random random = new Random();
        for (int i = 0; i < diceCount; i++) {
            dice.add(random.nextInt(6) + 1);
        }
    }
    public int countDice(int value) {

        if (oneCounted){
            return (int) dice.stream().filter(die -> die == value || die == 1).count();

        } else {
            return (int) dice.stream().filter(die -> die == value).count();

        }
    }

    public void changeOneMode(){
        this.oneCounted = false;
    }

    // Generate random dice rolls
    private List<Integer> generateRandomDice(int diceCount) {
        List<Integer> dice = new ArrayList<>();
        for (int i = 0; i < diceCount; i++) {
            dice.add((int) (Math.random() * NUM_FACES) + 1); // Random number between 1 and NUM_FACES
        }
        return dice;
    }
}
