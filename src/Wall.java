import java.util.*;

public class Wall {
    private boolean damaged;
    private int length;
    private int damagedLength;
    private String pattern;
    private String damagedPattern;
    private boolean broken;

    private List<Card> attackerCards;
    private List<Card> defenderCards;

    private boolean attackerFinishedFirst;

    public Wall(int length, int damagedLength, String pattern, String damagedPattern) {
        damaged = false;
        this.length = length;
        this.damagedLength = damagedLength;
        this.pattern = pattern;
        this.damagedPattern = damagedPattern;
        broken = false;

        attackerCards = new ArrayList<>();
        defenderCards = new ArrayList<>();

        attackerFinishedFirst = false;
    }

    public List<Card> damage() {
        if (damaged) {
            broken = true;
        } else {
            damaged = true;
        }
        List<Card> toDiscard = new ArrayList<>();
        toDiscard.addAll(attackerCards);
        toDiscard.addAll(defenderCards);
        length = damagedLength;
        pattern = damagedPattern;
        attackerCards.clear();
        defenderCards.clear();
        return toDiscard;
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
        return attackerCards.removeLast();
    }

    public boolean declareControl(List<Card> remainingCards) {
        if (attackerCards.size() == length) {
            int defenderStrength = getStrongestDefenderFormationStrength(remainingCards);
            if (attackerFinishedFirst) {
                return Main.getStrength(attackerCards, pattern) >= defenderStrength;
            } else {
                return Main.getStrength(attackerCards, pattern) > defenderStrength;
            }
        }
        return false;
    }

    public int getStrongestDefenderFormationStrength(List<Card> remainingCards) {
        return getStrongestFormationStrength(defenderCards, remainingCards, length, pattern, Integer.MIN_VALUE);
    }


    private int getStrongestFormationStrength(List<Card> currentFormation, List<Card> remainingCards, int length, String pattern, int maxStrength) {
        if (currentFormation.size() == length) {
            return Math.max(Main.getStrength(currentFormation, pattern), maxStrength);
        }

        for (int i = 0; i < remainingCards.size(); i++) {
            Card card = remainingCards.remove(i);
            currentFormation.add(card);
            maxStrength = Math.max(getStrongestFormationStrength(currentFormation, remainingCards, length, pattern, maxStrength), maxStrength);
            currentFormation.remove(card);
            remainingCards.add(i, card);
        }

        return maxStrength;
    }

    // return 0 if card played, -1 if card not played, 1-5 if 11, 0 cancel
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
        return 0;
    }

    public String toString() {
        String result = "";
        for (int i = 3; i >= 0; i--) {
            if (i >= attackerCards.size()) {
                result += "       ";
            } else {
                result += attackerCards.get(i).toString() + " ";
            }
        }
        if (damaged) {
            result += " | ";
        } else {
            result += "|| ";
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
        if (damaged) {
            result += "|  ";
        } else {
            result += "|| ";
        }
        for (Card card : defenderCards) {
            result += " " + card.toString();
        }
        return result;
    }

    public boolean containsCard(Card card) {
        return attackerCards.contains(card) || defenderCards.contains(card);
    }
}
