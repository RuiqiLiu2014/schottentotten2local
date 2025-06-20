import java.util.*;

public class Main {
    public static final int aWin = 1;
    public static final int dWin = -1;
    public static final String COLOR = "c";
    public static final String RUN = "r";
    public static final String[] colors = {"grape", "mango", "apple", "lemon", "peach"};
    public static int cauldronCount = 3;

    public static void main(String[] args) {
        Wall[] board = {new Wall(3, 3, "+", RUN),
                        new Wall(4, 2, " ", "="),
                        new Wall(3, 3, " ", COLOR),
                        new Wall(2, 4, " ", "-"),
                        new Wall(3, 3, " ", COLOR),
                        new Wall(4, 2, " ", "="),
                        new Wall(3, 3, "-", RUN)};

        Stack<Card> deck = new Stack<>();
        for (String color : colors) {
            for (int n = 0; n < 12; n++) {
                deck.push(new Card(color, n));
            }
        }
        Set<Card> discard = new TreeSet<>();

        Set<Card> attackerHand = new TreeSet<>();
        Set<Card> defenderHand = new TreeSet<>();
        shuffleAndDeal(deck, attackerHand, defenderHand);

        int won;
        while (true) {
            attackerTurn(board, deck, discard, attackerHand);
            won = won(board, deck);
            if (won != 0) {
                displayBoard(board, deck, discard);
                displayWinner(won);
                break;
            }
            defenderTurn(board, deck, discard, defenderHand);
        }
    }

    public static void attackerTurn(Wall[] board, Stack<Card> deck, Set<Card> discard, Set<Card> hand) {
        displayBoard(board, deck, discard);
        displayHand(hand);
        boolean played = playCard(board, deck, discard, hand, true);
        while (!played) {
            System.out.println("ur bad");
            played = playCard(board, deck, discard, hand, true);
        }
        declareControl(board, discard);
        if (!deck.isEmpty()) {
            hand.add(deck.pop());
        }
    }

    public static void defenderTurn(Wall[] board, Stack<Card> deck, Set<Card> discard, Set<Card> hand) {
        displayBoard(board, deck, discard);
        displayHand(hand);
        boolean played = playCard(board, deck, discard, hand, false);
        while (!played) {
            System.out.println("ur bad");
            played = playCard(board, deck, discard, hand, false);
        }
        if (!deck.isEmpty()) {
            hand.add(deck.pop());
        }
    }

    public static void retreat(Wall[] board, Set<Card> discard) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Which walls? ");
        String walls = scan.nextLine();
        Scanner wallScan = new Scanner(walls);
        List<Integer> toRetreatFrom = new ArrayList<>();
        while (wallScan.hasNext()) {
            int wall = Integer.parseInt(wallScan.next());
            toRetreatFrom.add(wall);
        }
        for (int wall : toRetreatFrom) {
            discard.addAll(board[wall - 1].retreat());
        }
    }

    public static void cauldron(Wall[] board, Set<Card> discard) {
        if (cauldronCount > 0) {
            Scanner scan = new Scanner(System.in);
            System.out.print("Which wall (1-7, 0 to cancel)? ");
            int wall = Integer.parseInt(scan.nextLine());
            if (wall != 0) {
                Card card = board[wall - 1].cauldron();
                if (card != null) {
                    discard.add(card);
                    cauldronCount--;
                    System.out.println(cauldronCount + " cauldrons remaining");
                }
            }
        }
    }

    public static boolean playCard(Wall[] board, Stack<Card> deck, Set<Card> discard, Set<Card> hand, boolean attacker) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Which card");
        if (attacker) {
            System.out.print(" (r for retreat)? ");
        } else if (cauldronCount > 0) {
            System.out.print(" (c for cauldron)? ");
        } else {
            System.out.print("? ");
        }
        String c = scan.nextLine();
        boolean used = false;
        if (attacker && c.equalsIgnoreCase("r")) {
            retreat(board, discard);
            used = true;
            displayBoard(board, deck, discard);
            displayHand(hand);
        }
        if (!attacker && c.equalsIgnoreCase("c")) {
            if (cauldronCount > 0) {
                cauldron(board, discard);
                used = true;
                displayBoard(board, deck, discard);
                displayHand(hand);
            } else {
                System.out.println("you have no more cauldrons");
                System.out.println("cry about it");
                return false;
            }
        }

        if (used) {
            System.out.print("Which card? ");
            c = scan.nextLine();
        }
        if (!Card.isValid(c)) {
            System.out.println("invalid move");
            System.out.println("your opponent smacks you");
            return false;
        }
        Card card = new Card(c);

        if (!hand.remove(card)) {
            System.out.println("you don't have that card");
            System.out.println("you clearly need glasses");
            return false;
        }

        System.out.print("Which wall? ");
        int wall = Integer.parseInt(scan.nextLine());
        if (wall < 1 || wall > 7) {
            System.out.println("that's not a wall");
            System.out.println("stop sending your troops to narnia");
            hand.add(card);
            return false;
        }
        int i = board[wall - 1].playCard(card, attacker);
        if (i == -1) {
            hand.add(card);
            return false;
        } else if (i != 0) {
            discard.add(new Card(colors[i - 1], 0));
            discard.add(new Card(colors[i - 1], 11));
        }
        return true;
    }

    public static void declareControl(Wall[] board, Set<Card> discard) {
        List<Card> remainingCards = new ArrayList<>();
        for (String color : colors) {
            for (int n = 0; n < 12; n++) {
                Card card = new Card(color, n);
                if (!discard.contains(card) && !onBoard(board, card)) {
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

    public static int won(Wall[] board, Stack<Card> deck) {
        int numDamaged = 0;
        for (Wall wall : board) {
            if (wall.isBroken()) {
                return aWin;
            } else if (wall.isDamaged()) {
                numDamaged++;
            }
        }
        if (numDamaged >= 4) {
            return aWin;
        }
        if (deck.isEmpty()) {
            return dWin;
        }
        boolean defenderSideFull = true;
        for (Wall wall : board) {
            if (!wall.defenderSideFull()) {
                defenderSideFull = false;
                break;
            }
        }
        if (defenderSideFull) {
            return dWin;
        }
        return 0;
    }

    public static void shuffleAndDeal(Stack<Card> deck, Set<Card> attackerHand, Set<Card> defenderHand) {
        Collections.shuffle(deck);
        for (int i = 0; i < 6; i++) {
            attackerHand.add(deck.pop());
            defenderHand.add(deck.pop());
        }
    }

    public static void displayBoard(Wall[] board, Stack<Card> deck, Set<Card> discard) {
        System.out.print("           ATTACKER                       DECK:");
        if (deck.size() < 10) {
            System.out.print("0");
        }
        System.out.print(deck.size() + "                       DEFENDER ");
        for (int i = 0; i < cauldronCount; i++) {
            System.out.print("\uD83E\uDDC9");
        }
        System.out.println();
        for (int i = 0; i < board.length; i++) {
            System.out.println(board[i].toString(i + 1));
        }

        System.out.println("------------------------------------------DISCARD------------------------------------------");
        for (Card card : discard) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();
        System.out.println("-------------------------------------------------------------------------------------------");
    }

    public static void displayHand(Set<Card> hand) {
        for (Card card : hand) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    public static void displayWinner(int won) {
        if (won == aWin) {
            System.out.println("Attacker wins");
        } else if (won == dWin) {
            System.out.println("Defender wins");
        } else {
            System.out.println("Game not over yet");
        }
    }

    public static boolean onBoard(Wall[] board, Card card) {
        for (Wall wall : board) {
            if (wall.contains(card)) {
                return true;
            }
        }
        return false;
    }
}