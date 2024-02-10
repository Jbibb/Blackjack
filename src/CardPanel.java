import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CardPanel extends JPanel {
    private ArrayList<CardVisual> cardVisuals = new ArrayList<>();
    private Card[] deck;
    private ArrayList<Card> dealerHand = new ArrayList<>();
    private ArrayList<Card> playerHand = new ArrayList<>();
    private Window parent;
    private int deckIndex = 0;

    private AudioPlayer audioPlayer = new AudioPlayer("flipcard.wav");
    public CardPanel(Card[] deck, Window parent){
        this.deck = deck;
        this.parent = parent;
    }

    private CardVisual dealCard(int x, int y){
        audioPlayer.playOnce();
        if(deckIndex >= deck.length) {
            deck = Main.getDeck();
            deckIndex = 0;
        }
        CardVisual cardVisual = new CardVisual(deck[deckIndex++], deckX, deckY, this);
        cardVisual.move(x, y);
        cardVisuals.add(cardVisual);
        return cardVisual;
    }


    private int deckX, deckY, playerCardsBaseX, playerCardsBaseY;
    public synchronized void deal(){
        cardVisuals.clear();
        deckX = this.getWidth() - (int) (cardWidth * 1.5) - cardWidth / 2;
        deckY = this.getHeight() / 6 - cardHeight / 2;

        playerHand.clear();
        dealerHand.clear();

        Thread dealThread = new Thread( () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            playerCardsBaseX = this.getWidth() / 2 - cardWidth;
            playerCardsBaseY = (int) (this.getHeight() / 1.4);

            CardVisual cardVisual = dealCard(playerCardsBaseX, playerCardsBaseY);
            playerCardsBaseX += cardWidth/2;
            playerHand.add(cardVisual.card);
            cardVisual.reverse();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            cardVisual = dealCard(this.getWidth() / 2 - cardWidth / 2 * 3, cardWidth/8);
            dealerHand.add(cardVisual.card);
            cardVisual.reverse();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e){
                e.printStackTrace();
            }


            cardVisual = dealCard(playerCardsBaseX, playerCardsBaseY);
            cardVisual.reverse();
            playerCardsBaseX += cardWidth/2;
            playerHand.add(cardVisual.card);

            try {
                Thread.sleep(750);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            cardVisual = dealCard(this.getWidth() / 2 - cardWidth / 2 * 2, cardWidth/8);
            dealerHand.add(cardVisual.card);

            try {
                Thread.sleep(750);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            evaluatePlayerHand();
            if(playerScore == 21) {
                cardVisuals.get(3).reverse();

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                parent.setDealResult(21, 0, false, false);
            } else
                parent.offerChoice();
        });
        dealThread.start();
    }
    public void stand() {
        Thread thread = new Thread( () -> {
            evaluatePlayerHand();

            cardVisuals.get(3).reverse();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            evaluateDealerHand();

            int i = 2;
            while (dealerScore < 17) {
                CardVisual cardVisual = dealCard(this.getWidth() / 2 - cardWidth / 2 - (cardWidth / 2) * (2 - i++), cardWidth / 8);
                dealerHand.add(cardVisual.card);
                cardVisual.reverse();

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                evaluateDealerHand();
            }

            parent.setDealResult(playerScore, dealerScore, playerHasBlackJack, dealerHasBlackJack);
        });
        thread.start();
    }

    public void hit() {
        Thread thread = new Thread( () -> {
            CardVisual cardVisual = dealCard(playerCardsBaseX, playerCardsBaseY);
            cardVisual.reverse();
            playerCardsBaseX += cardWidth/2;
            playerHand.add(cardVisual.card);

            evaluatePlayerHand();

            try {
                Thread.sleep(750);
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }

            if(playerScore > 21) {
                cardVisuals.get(3).reverse();

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e1){
                    e1.printStackTrace();
                }

                parent.setDealResult(playerScore, 0, false, false);
            } else if(playerHasBlackJack) {
                stand();
            } else {
                parent.offerChoice();
            }
        });
        thread.start();
    }

    public void doubleDown() {
        Thread thread = new Thread( () -> {
            CardVisual cardVisual = dealCard(playerCardsBaseX, playerCardsBaseY);
            cardVisual.reverse();
            playerCardsBaseX += cardWidth/2;
            playerHand.add(cardVisual.card);

            try {
                Thread.sleep(750);
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }

            evaluatePlayerHand();

            if(playerScore > 21) {
                cardVisuals.get(3).reverse();

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e1){
                    e1.printStackTrace();
                }

                parent.setDealResult(playerScore, 0, false, false);
            } else {

                cardVisuals.get(3).reverse();

                try {
                    Thread.sleep(750);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                evaluateDealerHand();

                int i = 2;
                while (dealerScore < 17) {
                    cardVisual = dealCard(this.getWidth() / 2 - cardWidth / 2 - (cardWidth / 2) * (2 - i++), cardWidth / 8);
                    cardVisual.reverse();
                    dealerHand.add(cardVisual.card);

                    try {
                        Thread.sleep(750);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    evaluateDealerHand();
                }

                parent.setDealResult(playerScore, dealerScore, playerHasBlackJack, dealerHasBlackJack);
            }

        });
        thread.start();
    }
    int dealerScore = 0;
    boolean dealerHasAce = false;
    boolean dealerHas10PointCard = false;
    boolean dealerHasBlackJack = false;
    private void evaluateDealerHand() {
        dealerScore = 0;
        dealerHasBlackJack = false;
        dealerHas10PointCard = false;
        dealerHasAce = false;
        int maxValueAces = 0;
        for (Card card : dealerHand) {
            if (card.value.value < 9)
                dealerScore += card.value.value + 2;
            else if (card.value.value < 12) {
                dealerHas10PointCard = true;
                dealerScore += 10;
            } else {
                dealerHasAce = true;
                maxValueAces++;
                dealerScore += 10;
            }
        }
        if(dealerHand.size() == 2 && dealerHasAce && dealerHas10PointCard) {
            dealerHasBlackJack = true;
            dealerScore = 21;
        } else if(dealerHand.size() == 2 && maxValueAces == 2) {
            dealerHasBlackJack = true;
            dealerScore = 21;
        }
        while (dealerScore > 21 && maxValueAces > 0) {
            dealerScore -= 9;
            maxValueAces--;
        }
    }

    private int playerScore = 0;
    private boolean playerHasAce = false;
    private boolean playerHas10PointCard = false;
    private boolean playerHasBlackJack = false;
    private void evaluatePlayerHand() {
        playerScore = 0;
        playerHasBlackJack = false;
        playerHas10PointCard = false;
        playerHasAce = false;
        int maxValueAces = 0;
        for (Card card : playerHand) {
            if (card.value.value < 9)
                playerScore += card.value.value + 2;
            else if (card.value.value < 12) {
                playerHas10PointCard = true;
                playerScore += 10;
            } else {
                playerHasAce = true;
                maxValueAces++;
                playerScore += 10;
            }
        }
        if(playerHand.size() == 2 && playerHasAce && playerHas10PointCard) {
            playerHasBlackJack = true;
            playerScore = 21;
        } else if(playerHand.size() == 2 && maxValueAces == 2) {
            playerHasBlackJack = true;
            playerScore = 21;
        } while (playerScore > 21 && maxValueAces > 0) {
            playerScore -= 9;
            maxValueAces--;
        }
    }

    private int cardWidth;
    private int cardHeight;
    public void scale(){
        cardWidth = parent.getWidth() / 8;
        cardHeight = parent.getHeight() / 4;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Image deck = ImageHandler.getImage("Back");
        deck = deck.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        g.drawImage(deck, this.getWidth() - (int)(cardWidth * 1.5) - cardWidth / 2, this.getHeight()/6 - cardHeight/2, null);

        Image cardImage;

        ArrayList<CardVisual> snapshot;
        synchronized (cardVisuals) {
            snapshot = new ArrayList<>(cardVisuals);
        }

        for(CardVisual cardVisual : snapshot) {
            if (cardVisual.isReversed)
                cardImage = ImageHandler.getImage("Back");
            else
                cardImage = ImageHandler.getImage(cardVisual.card.value + "_" + cardVisual.card.suit);
            cardImage = cardImage.getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
            g.drawImage(cardImage, cardVisual.x, cardVisual.y, null);
        }
    }
}
