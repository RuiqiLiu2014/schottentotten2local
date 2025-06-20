import java.util.*;

public class Wall {
    private boolean damaged;
    private boolean broken;
    private int length;
    private int damagedLength;
    private String pattern;
    private String damagedPattern;

    private List<Card> attackerCards;
    private List<Card> defenderCards;

    private boolean attackerFinishedFirst;

    public Wall(int length, int damagedLength, String pattern, String damagedPattern) {
        damaged = false;
        broken = false;
        this.length = length;
        this.damagedLength = damagedLength;
        this.pattern = pattern;
        this.damagedPattern = damagedPattern;

        attackerCards = new ArrayList<>();
        defenderCards = new ArrayList<>();

        attackerFinishedFirst = false;
    }

    public Set<Card> damage() {
        if (damaged) {
            broken = true;
            return new TreeSet<>();
        } else {
            damaged = true;
            Set<Card> toDiscard = new TreeSet<>();
            toDiscard.addAll(attackerCards);
            toDiscard.addAll(defenderCards);
            length = damagedLength;
            pattern = damagedPattern;
            attackerCards.clear();
            defenderCards.clear();
            return toDiscard;
        }
    }

    public boolean isDamaged() {
        return damaged;
    }

    public boolean isBroken() {
        return broken;
    }

    public List<Card> retreat() {
        List<Card> toDiscard = new ArrayList<>();
        for (Card card : attackerCards) {
            if (card != null) {
                toDiscard.add(card);
            }
        }
        attackerCards.clear();
        return toDiscard;
    }

    public Card cauldron() {
        if (attackerCards.size() == length) {
            attackerFinishedFirst = false;
        } else if (attackerCards.isEmpty()) {
            System.out.println("nothing to cauldron");
            System.out.println("thanks for watering the plants with hot oil i guess");
            System.out.println("jk have your cauldron back");
            return null;
        }
        return attackerCards.removeLast();
    }

    public boolean declareControl(List<Card> remainingCards) {
        if (attackerCards.size() == length) {
            int defenderStrength = getStrongestDefenderFormationStrength(remainingCards);
            int attackerStrength = getStrength(attackerCards);
            if (attackerFinishedFirst) {
                return attackerStrength >= defenderStrength;
            } else {
                return attackerStrength > defenderStrength;
            }
        }
        return false;
    }

    public int getStrongestDefenderFormationStrength(List<Card> remainingCards) {
        return getStrongestPossibleFormationStrength(defenderCards, remainingCards, length, Integer.MIN_VALUE);
    }

    private int getStrongestPossibleFormationStrength(List<Card> currentFormation, List<Card> remainingCards, int length, int maxStrength) {
        if (currentFormation.size() == length) {
            return Math.max(getStrength(currentFormation), maxStrength);
        }

        for (int i = 0; i < remainingCards.size(); i++) {
            Card card = remainingCards.remove(i);
            currentFormation.add(card);
            maxStrength = Math.max(getStrongestPossibleFormationStrength(currentFormation, remainingCards, length, maxStrength), maxStrength);
            currentFormation.remove(card);
            remainingCards.add(i, card);
        }

        return maxStrength;
    }

    // return 0 if card played, -1 if card not played, 1-5 if 11/0 cancel
    public int playCard(Card card, boolean attacker) {
        List<Card> playingSide;
        List<Card> otherSide;
        if (attacker) {
            playingSide = attackerCards;
            otherSide = defenderCards;
        } else {
            playingSide = defenderCards;
            otherSide = attackerCards;
        }

        if (playingSide.size() == length) {
            System.out.println("no more space");
            return -1;
        }

        int value = card.getValue();
        if (value == 0 || value == 11) {
            Card temp = new Card(card.getColor(), 11 - value);
            if (otherSide.contains(temp)) {
                playingSide.remove(card);
                otherSide.remove(temp);
                return Arrays.asList(Main.colors).indexOf(card.getColor()) + 1;
            }
        }
        playingSide.add(card);
        if (attackerCards.size() == length && !defenderSideFull()) {
            attackerFinishedFirst = true;
        }
        return 0;
    }

    public String toString(int wallNum) {
        String result = "";
        for (int i = 3; i >= 0; i--) {
            if (i >= attackerCards.size()) {
                result += "        ";
            } else {
                result += attackerCards.get(i).toString() + " ";
            }
        }
        if (broken) {
            result += "  " + wallNum + "   ";
        } else if (damaged) {
            result += "| " + wallNum + " | ";
        } else {
            result += "||" + wallNum + "|| ";
        }
        for (int i = 0; i < 4 - length; i++) {
            result += "  ";
        }
        for (int i = 0; i < length; i++) {
            result += "[" + pattern + "] ";
        }
        for (int i = 0; i < 4 - length; i++) {
            result += "  ";
        }
        if (broken) {
            result += "  " + wallNum + "  ";
        } else if (damaged) {
            result += "| " + wallNum + " |";
        } else {
            result += "||" + wallNum + "||";
        }
        for (Card card : defenderCards) {
            result += " " + card.toString();
        }
        return result;
    }

    public boolean contains(Card card) {
        return attackerCards.contains(card) || defenderCards.contains(card);
    }

    public boolean defenderSideFull() {
        return defenderCards.size() == length;
    }

    private int getStrength(List<Card> formation) {
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
            case Main.COLOR -> {
                if (type == 4 || type == 2) {
                    return sum;
                } else {
                    return type * 100 + sum;
                }
            }
            case Main.RUN -> {
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

    // 5 - color run, 4 - same strength, 3 - color, 2 - run, 1 - sum
    private static int getPatternType(List<Card> formation) {
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
}
