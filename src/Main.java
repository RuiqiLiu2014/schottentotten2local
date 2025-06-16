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
                displayWinner(won);
                break;
            }

            defenderTurn(board, deck, discard, defenderHand);
            won = won(board, deck);
            if (won != 0) {
                displayWinner(won);
                break;
            }
        }
    }

    public static void attackerTurn(Wall[] board, Stack<Card> deck, Set<Card> discard, Set<Card> hand) {
        displayBoard(board, discard);
        displayHand(hand);
        boolean played = playCard(board, discard, hand, true);
        while (!played) {
            System.out.println("ur bad");
            played = playCard(board, discard, hand, true);
        }
        declareControl(board, discard);
        draw(hand, deck);
    }

    public static void defenderTurn(Wall[] board, Stack<Card> deck, Set<Card> discard, Set<Card> hand) {
        displayBoard(board, discard);
        displayHand(hand);
        boolean played = playCard(board, discard, hand, false);
        while (!played) {
            System.out.println("ur bad");
            played = playCard(board, discard, hand, false);
        }
        draw(hand, deck);
    }

    public static void draw(Set<Card> hand, Stack<Card> deck) {
        hand.add(deck.pop());
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
                discard.add(board[wall - 1].cauldron());
                cauldronCount--;
                System.out.println(cauldronCount + " cauldrons remaining");
            }
        }
    }

    public static void declareControl(Wall[] board, Set<Card> discard) {
        List<Card> remainingCards = new ArrayList<>();
        for (String color : colors) {
            for (int n = 0; n < 12; n++) {
                Card card = new Card(color, n);
                if (!discard.contains(card) || !onBoard(board, card)) {
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

    public static boolean onBoard(Wall[] board, Card card) {
        for (Wall wall : board) {
            if (wall.containsCard(card)) {
                return true;
            }
        }
        return false;
    }

    public static boolean playCard(Wall[] board, Set<Card> discard, Set<Card> hand, boolean attacker) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Which card ");
        if (attacker) {
            System.out.print("(r for retreat)? ");
        } else {
            System.out.print("(c for cauldron)? ");
        }
        String c = scan.nextLine();
        boolean used = false;
        if (attacker && c.equalsIgnoreCase("r")) {
            retreat(board, discard);
            used = true;
        }
        if (!attacker && c.equalsIgnoreCase("c")) {
            cauldron(board, discard);
            used = true;
        }

        if (used) {
            System.out.print("Which card? ");
        }
        Card card = new Card(c);
        if (!hand.remove(card)) {
            System.out.println("You don't have that card");
            return false;
        }

        System.out.print("Which wall (1-7)? ");
        int wall = Integer.parseInt(scan.nextLine());
        int i = board[wall - 1].playCard(card, attacker);
        if (i == -1) {
            return false;
        } else if (i != 0) {
            discard.add(new Card(colors[i - 1], 0));
            discard.add(new Card(colors[i - 1], 11));
        } else {
            hand.remove(card);
            discard.add(card);
        }
        return true;
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
        return 0;
    }


    public static void displayBoard(Wall[] board, Set<Card> discard) {
        System.out.println("       Attacker                                       Defender");
        for (Wall wall : board) {
            System.out.println(wall.toString());
        }

        System.out.println("----------------------------------------------------------------------");
        for (Card card : discard) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();
        System.out.println("----------------------------------------------------------------------");
    }

    public static void shuffleAndDeal(Stack<Card> deck, Set<Card> attackerHand, Set<Card> defenderHand) {
        Collections.shuffle(deck);
        for (int i = 0; i < 6; i++) {
            attackerHand.add(deck.pop());
            defenderHand.add(deck.pop());
        }
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

    public static int getStrength(List<Card> formation, String pattern) {
        int sum = 0;
        for (Card card : formation) {
            sum += card.getValue();
        }
        int type = getPatternType(formation);

        switch (pattern) {
            case "+" -> {
                return sum;
            }
            case "-" -> {
                return -sum;
            }
            case COLOR -> {
                if (type == 4 || type == 2) {
                    return sum;
                } else {
                    return type * 100 + sum;
                }
            }
            case RUN -> {
                if (type == 4 || type == 3) {
                    return sum;
                } else {
                    return type * 100 + sum;
                }
            }
            default -> {
                return type * 100 + sum;
            }
        }
    }

    // 5 - color run
    // 4 - same strength
    // 3 - color
    // 2 - run
    // 1 - sum
    public static int getPatternType(List<Card> formation) {
        Set<String> colors = new TreeSet<>();
        List<Integer> values = new ArrayList<>();
        for (Card card : formation) {
            colors.add(card.getColor());
            values.add(card.getValue());
        }
        Collections.sort(values);

        boolean allSame = true;
        int first = values.getFirst();
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) != first) {
                allSame = false;
                break;
            }
        }
        if (allSame) {
            return 4;
        }

        Set<Integer> diffs = new TreeSet<>();
        for (int i = 0; i < formation.size() - 1; i++) {
            diffs.add(values.get(i + 1) - values.get(i));
        }

        if (colors.size() == 1) {
            if (diffs.size() == 1 && diffs.contains(1)) {
                return 5;
            } else {
                return 3;
            }
        } else {
            if (diffs.size() == 1 && diffs.contains(1)) {
                return 2;
            }
        }

        return 1;
    }

    public String[] colors() {
        return colors;
    }
}