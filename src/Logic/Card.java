package Logic;

import java.util.Random;

public class Card {
    public final Suits suit;
    public final Values value;

    public Card(Suits suit, Values value) {
        this.suit = suit;
        this.value = value;
    }

    public static Card[] getShoe(int deckAmount){
        Card[] deck = new Card[52 * deckAmount];
        int i = 0;
        for(int j = 0; j < 6; j++)
            for(Suits s : Suits.values())
                for(Values v : Values.values())
                    deck[i++] = new Card(s, v);
        Card tmp;
        Random rnd = new Random();
        for (i = deck.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            tmp = deck[index];
            deck[index] = deck[i];
            deck[i] = tmp;
        }

        return deck;
    }

    @Override
    public String toString(){
        return value.label + suit.label;
    }
}