import java.util.*;

public class Deck {
    private static Deck instance;
    private Stack<Card> deck;

    private Deck() {
        deck = new Stack<>();
        for (String color : Constants.colors) {
            for (int n : Constants.values) {
                deck.push(new Card(color, n));
            }
        }
    }

    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();
        }
        return instance;
    }

    public int size() {
        return deck.size();
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card pop() {
        return deck.pop();
    }
}
