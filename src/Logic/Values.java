package Logic;

public enum Values {
    Two("2", 0), Three("3", 1), Four("4", 2), Five("5", 3), Six("6", 4), Seven("7", 5),
    Eight("8", 6), Nine("9", 7), Ten("10", 8), Jack("J", 9), Queen("Q", 10), King("K", 11), Ace("A", 12);
    public final String label;
    public final int value;
    Values(String label, int value) {
        this.label = label;
        this.value = value;
    }
}
