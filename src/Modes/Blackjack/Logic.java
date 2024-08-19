package Modes.Blackjack;

import java.awt.*;
import java.util.ArrayList;
import Logic.Card;

public class Logic {
    private Card[] deck;
    private int deckIndex = 0;
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private CardPanel cardPanel;
    public enum CardDealtTo {
        Player, Dealer
    }

    public enum EndStates {
        PlayerWins("Player Wins!"), DealerWins("Dealer Wins!"), PlayerBust("Player Bust!"), DealerBust("Dealer Bust!"), Tie("Tie");
        public final String label;
        EndStates(String label){
            this.label = label;
        }

    }
    private static final int deckAmount = 6;
    private int bet;

    public Logic(CardPanel cardPanel){
        this.cardPanel = cardPanel;
        this.deck = Card.getShoe(deckAmount);
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    private void dealCard(boolean isHidden, CardDealtTo cardDealtTo) {
        if(deckIndex >= deck.length) {
            deck = Card.getShoe(6);
            deckIndex = 0;
        }
        Card card = deck[deckIndex++];
        if(cardDealtTo == CardDealtTo.Player)
            playerHand.add(card);
        else
            dealerHand.add(card);

        cardPanel.fireCardDeal(isHidden, card, cardDealtTo);
    }

    private int evaluateHand(ArrayList<Card> hand) {
        int score = 0;
        int maxValueAces = 0;
        for (Card card : hand) {
            if (card.value.value < 8)
                score += card.value.value + 2;
            else if (card.value.value < 12) {
                score += 10;
            } else {
                maxValueAces++;
                score += 11;
            }
        }
        if(hand.size() == 2 && score == 21) {
            score = -1;
        } while (score > 21 && maxValueAces > 0) {
            score -= 10;
            maxValueAces--;
        }
        return score;
    }

    public void deal(){
        cardPanel.fireNewDeal();

        playerHand.clear();
        dealerHand.clear();

        dealCard(false, CardDealtTo.Player);
        dealCard(false, CardDealtTo.Dealer);
        dealCard(false, CardDealtTo.Player);
        dealCard(true, CardDealtTo.Dealer);

        int playerScore = evaluateHand(playerHand);

        if(playerScore == -1) {
            cardPanel.fireRevealDealerHiddenCard();
            int dealerScore = evaluateHand(dealerHand);
            if(dealerScore == -1){
                cardPanel.fireDealResult(0, EndStates.Tie);
            } else {
                cardPanel.fireDealResult(bet, EndStates.PlayerWins);
            }
        } else
            cardPanel.fireOfferChoice();
    }

    public void stand(){
        cardPanel.fireRevealDealerHiddenCard();

        int dealerScore = evaluateHand(dealerHand);

        if(dealerScore != -1) {
            while (dealerScore < 17) {
                dealCard(false, CardDealtTo.Dealer);
                dealerScore = evaluateHand(dealerHand);
            }
        }
        dealResult();
    }

    public void hit(){
        dealCard(false, CardDealtTo.Player);
        int playerScore = evaluateHand(playerHand);

        if(playerScore > 21) {
            cardPanel.fireRevealDealerHiddenCard();
            dealResult();//cardPanel.fireDealResult(playerScore, 0);
        } else if(playerScore == 21) {
            stand();
        } else {
            cardPanel.fireOfferChoice();
        }
    }

    public void doubleDown(){
        bet *= 2;

        dealCard(false, CardDealtTo.Player);

        cardPanel.fireRevealDealerHiddenCard();
        int playerScore = evaluateHand(playerHand);
        if(playerScore > 21) {
            dealResult();//cardPanel.fireDealResult(playerScore, 0);
        } else {
            int dealerScore = evaluateHand(dealerHand);
            int i = 2;
            while (dealerScore < 17) {
                dealCard(false, CardDealtTo.Dealer);
                dealerScore = evaluateHand(dealerHand);
            }

            dealResult();//cardPanel.fireDealResult(playerScore, dealerScore);
        }
    }

    public void dealResult(){
        int playerScore = evaluateHand(playerHand);
        int dealerScore = evaluateHand(dealerHand);
        if(playerScore == -1){
            cardPanel.fireDealResult(bet, EndStates.PlayerWins);
        } else if (dealerScore == -1){
            cardPanel.fireDealResult(-bet, EndStates.DealerWins);
        } else if (playerScore > 21){
            cardPanel.fireDealResult(-bet, EndStates.PlayerBust);
        } else if (dealerScore > 21){
            cardPanel.fireDealResult(bet, EndStates.DealerBust);
        } else if (playerScore > dealerScore){
            cardPanel.fireDealResult(bet, EndStates.PlayerWins);
        } else if (dealerScore > playerScore){
            cardPanel.fireDealResult(-bet, EndStates.DealerWins);
        } else {
            cardPanel.fireDealResult(0, EndStates.Tie);
        }
    }
}
