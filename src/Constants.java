import java.util.*;

public class Constants {
    public static final List<String> colors = List.of("heart", "diamond", "star", "clover", "flower");
    public static final List<String> colorColors = List.of("red", "blue", "yellow", "green", "pink");
    public static final List<Integer> values = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    public static final int handSize = 6;
    public static final int numCauldrons = 3;
    public static final int numWalls = 7;
    public static final int longestWall = 4;

    public static final int attackerWins = 1;
    public static final int defenderWins = 2;
    public static final int noWinner = 0;

    public static final int[] wallLengths = {3, 4, 3, 2, 3, 4, 3};
    public static final int[] damagedWallLengths = {3, 2, 3, 4, 3, 2, 3};
    public static final String[] wallPatterns = {Symbols.PLUS, Symbols.NONE, Symbols.NONE, Symbols.NONE, Symbols.NONE, Symbols.NONE, Symbols.MINUS};
    public static final String[] damagedWallPatterns = {Symbols.RUN, Symbols.EQUALS, Symbols.COLOR, Symbols.MINUS, Symbols.COLOR, Symbols.EQUALS, Symbols.RUN};

    public static Set<Card> allCards() {
        Set<Card> result = new TreeSet<>();
        for (String color : colors) {
            for (int value : values) {
                result.add(new Card(color, value));
            }
        }
        return result;
    }

    public static String convert(String color) {
        color = color.toLowerCase();
        if (colors.contains(color)) {
            return color;
        } else if (colorColors.contains(color)) {
            return colors.get(colorColors.indexOf(color));
        } else if (Symbols.COLORS.contains(color)) {
            return colors.get(Symbols.COLORS.indexOf(color));
        } else {
            return color;
        }
    }
}