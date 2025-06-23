public class Card implements Comparable<Card> {
    private final int value;
    private final String color;

    public Card(String color, int value) {
        this.value = value;
        this.color = Constants.convert(color);
    }

    public Card(String name) {
        this.color = Constants.convert(name.substring(0, name.length() - 2));
        this.value = Integer.parseInt(name.substring(name.length() - 2));
    }

    public String toString() {
        if (value <= 9) {
            return Symbols.colorEmoji(color) + "0" + value;
        } else {
            return Symbols.colorEmoji(color) + value;
        }
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public int compareTo(Card other) {
        if (this.color.equals(other.color)) {
            return this.value - other.value;
        }
        return this.color.compareTo(other.color);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Card) {
            return compareTo((Card)o) == 0;
        } else {
            return false;
        }
    }

    public static boolean isValid(String name) {
        if (name.length() <= 2) {
            return false;
        }
        if (!Constants.colors.contains(Constants.convert(name.substring(0, name.length() - 2)))) {
            return false;
        }
        int value = Integer.parseInt(name.substring(name.length() - 2));
        return value <= 11 && value >= 0;
    }
}