import javax.swing.*;
import java.io.IOException;
import java.util.Random;

enum Suits {
    Clubs('\u2667', 0), Diamonds('\u2666', 1), Hearts('\u2661', 2), Spades('\u2660', 3);
    public final char label;
    public final int value;
    Suits(char label, int value) {
        this.label = label;
        this.value = value;
    }
}
enum Values {
    Two("2", 0), Three("3", 1), Four("4", 2), Five("5", 3), Six("6", 4), Seven("7", 5),
    Eight("8", 6), Nine("9", 7), Ten("10", 8), Jack("J", 9), Queen("Q", 10), King("K", 11), Ace("A", 12);
    public final String label;
    public final int value;
    Values(String label, int value) {
        this.label = label;
        this.value = value;
    }
}

class Card {
    public final Suits suit;
    public final Values value;

    public Card(Suits suit, Values value) {
        this.suit = suit;
        this.value = value;
    }

    @Override
    public String toString(){
        return value.label + suit.label;
    }
}

public class Main {
    public static void main(String[] args) {
        // '\u2664' - piki
        // '\u2661' - kiery
        // '\u2662' - karo
        // '\u2667' - trefle

        try {
            ImageHandler.loadImages();
        } catch (IOException e){
            e.printStackTrace();
        }

        AudioPlayer audioPlayer = new AudioPlayer("Walk Through The Park - TrackTribe.wav");
        audioPlayer.play();

        SwingUtilities.invokeLater(new Window(getDeck()));
    }

    public static Card[] getDeck(){
        Card[] deck = new Card[52 * 6];
        int i = 0;
        for(int j = 0; j < 6; j++)
            for(Suits s : Suits.values())
                for(Values v : Values.values())
                    deck[i++] = new Card(s, v);
        Card tmp = null;
        int randomIndex;
        Random rnd = new Random();
        for (i = deck.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            tmp = deck[index];
            deck[index] = deck[i];
            deck[i] = tmp;
        }

        return deck;
    }
}