import java.util.*;

public class Constants {
    public static final List<String> colors = List.of("grape", "mango", "apple", "lemon", "peach");
    public static final List<Integer> values = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
    public static final int handSize = 6;
    public static final int numCauldrons = 3;
    public static final int numWalls = 7;
    public static final String CAULDRON = "\uD83E\uDDC9";

    public static final int attackerWins = 1;
    public static final int defenderWins = 2;
    public static final int noWinner = 0;

    public static final String COLOR = "c";
    public static final String RUN = "r";
    public static final String EQUALS = "=";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String NONE = " ";

    public static final int[] wallLengths = {3, 4, 3, 2, 3, 4, 3};
    public static final int[] damagedWallLengths = {3, 2, 3, 4, 3, 2, 3};
    public static final String[] wallPatterns = {PLUS, NONE, NONE, NONE, NONE, NONE, MINUS};
    public static final String[] damagedWallPatterns = {RUN, EQUALS, COLOR, MINUS, COLOR, EQUALS, RUN};
}