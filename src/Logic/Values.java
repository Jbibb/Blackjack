package Logic;

public enum Values {
    Two("2", 2), Three("3", 3), Four("4", 4), Five("5", 5), Six("6", 6), Seven("7", 7),
    Eight("8", 8), Nine("9", 9), Ten("10", 10), Jack("J", 10), Queen("Q", 10), King("K", 10), Ace("A", 11);
    public final String label;
    public final int value;
    Values(String label, int value) {
        this.label = label;
        this.value = value;
    }
}
