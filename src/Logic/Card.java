package Logic;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Card {
    public final Suits suit;
    public final Values value;

    public static Map<Integer, Integer> currentShoe;

    public Card(Suits suit, Values value) {
        this.suit = suit;
        this.value = value;
    }

    public static Card[] getShoe(int deckAmount){
        Card[] deck = new Card[52 * deckAmount];
        currentShoe = new HashMap<>();
        int i = 0;
        for(int j = 0; j < 6; j++)
            for(Suits s : Suits.values())
                for(Values v : Values.values()) {
                    deck[i++] = new Card(s, v);
                    if(currentShoe.containsKey(v.value))
                        currentShoe.put(v.value, currentShoe.get(v.value) + 1);
                    else
                        currentShoe.put(v.value, 1);
                }
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

    public static void subtractFromShoe(Card card){
        int value = card.value.value;
        currentShoe.put(value, currentShoe.get(value) - 1);
    }

    public static int countCardsInShoeOfValueEqualOrGreaterThan(int value){
        int cardCount = 0;
        for(int currentValue = value; currentValue < 11; currentValue++) {
            cardCount += Card.currentShoe.get(currentValue);
        }
        return cardCount;
    }

    @Override
    public String toString(){
        return value.label + suit.label;
    }
}