import java.util.*;

public class Card implements Comparable<Card> {
    private final int value;
    private final String color;

    public Card(String color, int value) {
        this.value = value;
        this.color = color;
    }

    public Card(String name) {
        this.color = name.substring(0, 5);
        this.value = Integer.parseInt(name.substring(5));
    }

    public String toString() {
        return color + value;
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
}