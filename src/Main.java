import java.util.*;

public class Main {
    public static Board board = Board.getInstance();
    public static Discard discard = Discard.getInstance();
    public static Deck deck = Deck.getInstance();
    public static int cauldronCount = Constants.numCauldrons;

    public static void main(String[] args) {
        Player attacker = new Attacker();
        Player defender = new Defender();
        shuffleAndDeal(attacker, defender);

        int won;
        while (true) {
            displayBoard();
            attacker.takeTurn();
            declareControl();
            won = won();
            if (won != Constants.noWinner) {
                displayBoard();
                displayWinner(won);
                break;
            }
            displayBoard();
            defender.takeTurn();
        }
    }

    public static void retreat(int wall) {
        discard.addAll(board[wall - 1].retreat());
    }

    public static void cauldron(int wall, Player defender) {
        Card card = board[wall - 1].cauldron();
        if (card != null) {
            discard.add(card);
            cauldronCount--;
            System.out.println(cauldronCount + " cauldrons remaining");
            ((Defender)defender).usedCauldron();
        }
    }

    public static boolean playCard(Card card, int wall, boolean attacker) {
        int i = board[wall - 1].playCard(card, attacker);
        if (i == -1) {
            return false;
        } else if (i != 0) {
            discard.add(new Card(Constants.colors.get(i - 1), 0));
            discard.add(new Card(Constants.colors.get(i - 1), 11));
        }
        return true;
    }

    public static void declareControl() {
        List<Card> remainingCards = new ArrayList<>();
        for (String color : Constants.colors) {
            for (int n : Constants.values) {
                Card card = new Card(color, n);
                if (!discard.contains(card) && !onBoard(card)) {
                    remainingCards.add(card);
                }
            }
        }

        for (Wall wall : board) {
            if (wall.declareControl(remainingCards)) {
                discard.addAll(wall.damage());
            }
        }
    }

    public static int won() {
        int numDamaged = 0;
        for (Wall wall : board) {
            if (wall.isBroken()) {
                return Constants.attackerWins;
            } else if (wall.isDamaged()) {
                numDamaged++;
            }
        }
        if (numDamaged >= 4) {
            return Constants.attackerWins;
        }
        if (deck.isEmpty()) {
            return Constants.defenderWins;
        }
        boolean defenderSideFull = true;
        for (Wall wall : board) {
            if (!wall.defenderSideFull()) {
                defenderSideFull = false;
                break;
            }
        }
        if (defenderSideFull) {
            return Constants.defenderWins;
        }
        return Constants.noWinner;
    }

    public static void shuffleAndDeal(Player attacker, Player defender) {
        deck.shuffle();
        for (int i = 0; i < Constants.handSize; i++) {
            attacker.draw();
            defender.draw();
        }
    }

    public static void displayBoard() {
        System.out.print("           ATTACKER                       DECK:");
        if (deck.size() < 10) {
            System.out.print("0");
        }
        System.out.print(deck.size() + "                       DEFENDER ");
        for (int i = 0; i < cauldronCount; i++) {
            System.out.print(Constants.CAULDRON);
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.println(board[i].toString(i + 1));
        }

        System.out.println("------------------------------------------DISCARD------------------------------------------");
        discard.display();
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public static void displayWinner(int won) {
        if (won == Constants.attackerWins) {
            System.out.println("Attacker wins");
        } else if (won == Constants.defenderWins) {
            System.out.println("Defender wins");
        } else {
            System.out.println("Game not over yet");
        }
    }

    public static boolean onBoard(Card card) {
        for (Wall wall : board) {
            if (wall.contains(card)) {
                return true;
            }
        }
        return false;
    }
}