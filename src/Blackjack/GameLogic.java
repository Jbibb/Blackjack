package Blackjack;

import java.util.ArrayList;
import Logic.Card;
import Logic.Values;

public class GameLogic {
    private Card[] deck;
    private int deckIndex = 0;
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private CardPanel cardPanel;
    private boolean useStrategyTable = false;
    private StrategyTableModel strategyTableModel;
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
    private static int deckAmount;
    private int bet;

    public GameLogic(CardPanel cardPanel,StrategyTableModel strategyTableModel, int deckAmount){
        this.cardPanel = cardPanel;
        this.deckAmount = deckAmount;
        this.deck = Card.getShoe(deckAmount);
        this.strategyTableModel = strategyTableModel;
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

        if(!isHidden)
            Card.subtractFromShoe(card);

        if(cardDealtTo == CardDealtTo.Player)
            playerHand.add(card);
        else
            dealerHand.add(card);

        cardPanel.fireCardDeal(isHidden, card, cardDealtTo);
    }

    private void revealDealerCard(){
        Card.subtractFromShoe(dealerHand.get(1));
        cardPanel.fireRevealDealerHiddenCard();
    }

    private int evaluateHand(ArrayList<Card> hand) {
        int score = 0;
        int maxValueAces = 0;
        for (Card card : hand) {
            if (card.value.value <= 10)
                score += card.value.value;
            else {
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
            //cardPanel.fireRevealDealerHiddenCard();
            revealDealerCard();
            int dealerScore = evaluateHand(dealerHand);
            if(dealerScore == -1){
                cardPanel.fireDealResult(0, EndStates.Tie);
            } else {
                cardPanel.fireDealResult(bet, EndStates.PlayerWins);
            }
        } else
            if(!useStrategyTable)
                cardPanel.fireOfferChoice();
            else {
                StrategyTableModel.Action action;
                if(playerHand.stream().anyMatch((c) -> c.value == Values.Ace))
                    action = strategyTableModel.getAction(-(playerScore - 11), dealerHand.get(0).value.value);
                else
                    action = strategyTableModel.getAction(playerScore, dealerHand.get(0).value.value);
                switch (action){
                    case Hit:
                        hit();
                        break;
                    case Stand:
                        stand();
                        break;
                    default:
                        doubleDown();
                        break;
                }
            }
    }

    public void stand(){
        //cardPanel.fireRevealDealerHiddenCard();
        revealDealerCard();

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
            //cardPanel.fireRevealDealerHiddenCard();
            revealDealerCard();
            dealResult();//cardPanel.fireDealResult(playerScore, 0);
        } else if(playerScore == 21) {
            stand();
        } else {
            if(!useStrategyTable)
                cardPanel.fireOfferChoice();
            else {
                StrategyTableModel.Action action;
                if(playerHand.stream().anyMatch((c) -> c.value == Values.Ace))
                    action = strategyTableModel.getAction(-(playerScore - 11), dealerHand.get(0).value.value);
                else
                    action = strategyTableModel.getAction(playerScore, dealerHand.get(0).value.value);
                switch (action){
                    case Hit:
                        hit();
                        break;
                    case Stand:
                        stand();
                        break;
                    default:
                        doubleDown();
                        break;
                }
            }
        }
    }

    public void doubleDown(){
        bet *= 2;

        dealCard(false, CardDealtTo.Player);

        //cardPanel.fireRevealDealerHiddenCard();
        revealDealerCard();
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

        if(playerScore == dealerScore || ((playerScore == -1 || playerScore == 21) && (dealerScore == -1 || dealerScore == 21)))
            cardPanel.fireDealResult(0, EndStates.Tie);
        else if((playerScore <= 21 && playerScore > dealerScore) || dealerScore > 21)
            cardPanel.fireDealResult(bet, EndStates.PlayerWins);
        else
            cardPanel.fireDealResult(-bet, EndStates.DealerWins);
    }

    public void setUseStrategyTable(boolean useStrategyTable) {
        this.useStrategyTable = useStrategyTable;
    }

    public boolean isUsingStrategyTable() {
        return useStrategyTable;
    }

    public String getTip(){
        if(playerHand.size() >= 2 && dealerHand.size() >= 2 && Card.currentShoe != null) {
            int playerScore = evaluateHand(playerHand);
            StringBuilder sb = new StringBuilder();

            int lowestPlayerScore = 0;
            for (Card c : playerHand)
                if (c.value != Values.Ace)
                    lowestPlayerScore += c.value.value;
                else
                    lowestPlayerScore += 1;

            if (lowestPlayerScore >= 12) {

                sb.append("Hand has a chance of busting if hit.");
                sb.append("\nCards of value ").append(21 - lowestPlayerScore + 1).append(" or greater will cause a bust");
                int amountOfBustCards = Card.countCardsInShoeOfValueEqualOrGreaterThan(21 - lowestPlayerScore + 1);
                sb.append(".\n\nBust chance is ").append(((int) (amountOfBustCards / (deckAmount * 52d - deckIndex) * 10000)) / 100d).append("%.");
            } else {
                sb.append("Hand has no chance of busting.");
            }
            sb.append("\n\n");

            int knownDealerScore = dealerHand.get(0).value.value;
            int scoreRequiredByDealerToBeatPlayer = playerScore - knownDealerScore;
            if (scoreRequiredByDealerToBeatPlayer < 2)
                sb.append("Dealer has a hand that ties or beats the current player hand.");
            else {
                int amountOfWinningDealerCards = Card.countCardsInShoeOfValueEqualOrGreaterThan(scoreRequiredByDealerToBeatPlayer);
                sb.append("Dealer has a ").append(((int) (amountOfWinningDealerCards / (deckAmount * 52d - deckIndex) * 10000)) / 100d);
                sb.append("% chance having a card that will tie or beat the current player hand.");
            }
            sb.append("\n");
            return sb.toString();
        }
        return "";
    }
}
