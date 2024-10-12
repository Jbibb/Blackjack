package Logic;

public enum Suits {
    Clubs('\u2667', 0), Diamonds('\u2666', 1), Hearts('\u2661', 2), Spades('\u2660', 3);
    public final char label;
    public final int value;
    Suits(char label, int value) {
        this.label = label;
        this.value = value;
    }
}
