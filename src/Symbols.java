import java.util.*;

public class Symbols {
    public static final String cardSpace = "    ";
    public static final String CAULDRON = "\uD83E\uDDC9";
    public static final String[] leftWalls = {"||", "| ", "  "};
    public static final String[] rightWalls = {"||", " |", "  "};
    public static final String COLOR = "c";
    public static final String RUN = "r";
    public static final String EQUALS = "=";
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String NONE = " ";

    private static final String HEART = "♥️";
    private static final String DIAMOND = "\uD83D\uDC8E";
    private static final String STAR = "⭐️";
    private static final String CLOVER = "\uD83C\uDF40";
    private static final String FLOWER = "\uD83C\uDF38";
    public static final List<String> COLORS = List.of(HEART, DIAMOND, STAR, CLOVER, FLOWER);

    public static String colorEmoji(String color) {
        return COLORS.get(Constants.colors.indexOf(Constants.convert(color)));
    }
}
