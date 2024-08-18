package Modes.Blackjack;

import Logic.Card;
import Menus.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.LinkedList;

public class CardPanel extends JPanel {
    private ArrayList<CardVisual> cardVisuals = new ArrayList<>();
    private int nextPlayerCardX, nextPlayerCardY;
    private int nextDealerCardX, nextDealerCardY;
    private int deckX, deckY;
    private int cardWidth = 209;
    private int cardHeight = 303;
    private int balanceChange;
    private Logic.EndStates dealEndState;
    private int waitTimeBetweenDealerActions = 500;
    private UIPanel uiPanel;
    private AudioPlayer audioPlayer = new AudioPlayer("flipcard.wav");
    private int frameTime = 8;
    public CardPanel(UIPanel uiPanel){
        this.uiPanel = uiPanel;
        setBackground(new Color(20, 100, 20));
        Timer repaintTimer = new Timer(frameTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });

        repaintTimer.start();
        executor.start();
    }
    private java.util.Queue<CardVisual> cardVisualsToBeDealt = new LinkedList<>();
    private java.util.Queue<Runnable> executionQueue = new LinkedList();
    private final Runnable offerChoiceAction = () -> {
        uiPanel.offerChoice();
    };
    private final Runnable revealHiddenCardAction = () -> {
        cardVisuals.get(3).reveal();
    };
    private final Runnable dealResultAction = () -> {
        uiPanel.setDealResult(balanceChange, dealEndState);
    };
    private Timer executor = new Timer(waitTimeBetweenDealerActions, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Runnable runnable = executionQueue.poll();
            if(runnable != null)
                runnable.run();
        }
    });
    private Runnable cardDealAction = () -> {
            CardVisual cardVisual = cardVisualsToBeDealt.poll();
            if (cardVisual != null) {
                audioPlayer.playOnce();
                cardVisuals.add(cardVisual);
                if (cardVisual.dealtTo == Logic.CardDealtTo.Player) {
                    cardVisual.moveTo(nextPlayerCardX, nextPlayerCardY);
                    nextPlayerCardX += cardWidth / 2;
                } else {
                    cardVisual.moveTo(nextDealerCardX, nextDealerCardY);
                    nextDealerCardX += cardWidth / 2;
                }
            }
    };
    public void fireNewDeal(){
        cardVisuals.clear();
        resetCardBases();
    }
    public void fireCardDeal(boolean isHidden, Card card, Logic.CardDealtTo cardDealtTo) {
        CardVisual cardVisual = new CardVisual(card, deckX, deckY, this, isHidden, cardDealtTo);
        cardVisualsToBeDealt.add(cardVisual);
        executionQueue.add(cardDealAction);
    }
    public void fireOfferChoice(){
        executionQueue.add(offerChoiceAction);
    }
    public void fireRevealDealerHiddenCard(){
        executionQueue.add(revealHiddenCardAction);
    }
    public void fireDealResult(int balanceChange, Logic.EndStates endState){
        this.balanceChange = balanceChange;
        this.dealEndState = endState;
        executionQueue.add(dealResultAction);
    }
    public void scale() {
        resizeCardVisuals();
        repositionCardVisuals();
    }

    private void resizeCardVisuals() {
        int parentWidth = this.getParent().getWidth();
        int parentHeight = this.getParent().getHeight();

        int targetWidth = parentWidth / 8;
        int targetHeight = parentHeight / 4;

        double widthRatio = (double) targetWidth / ImageHandler.originalCardWidth;
        double heightRatio = (double) targetHeight / ImageHandler.originalcardHeight;

        double scaleFactor = Math.min(widthRatio, heightRatio);

        cardWidth = (int) (ImageHandler.originalCardWidth * scaleFactor);
        cardHeight = (int) (ImageHandler.originalcardHeight * scaleFactor);

        ImageHandler.scaleImages(cardWidth, cardHeight);
    }

    private void repositionCardVisuals(){
        deckX = (int)(this.getWidth() * 0.75);
        deckY = (int)(this.getHeight() * 0.005);

        resetCardBases();

        for(CardVisual cardVisual : cardVisuals){
            if(cardVisual.dealtTo == Logic.CardDealtTo.Player){
                cardVisual.x = nextPlayerCardX;
                cardVisual.y = nextPlayerCardY;
                nextPlayerCardX += cardWidth / 2;
            } else {
                cardVisual.x = nextDealerCardX;
                cardVisual.y = nextDealerCardY;
                nextDealerCardX += cardWidth / 2;
            }
        }
    }

    private void resetCardBases(){
        nextPlayerCardX = this.getWidth() / 2 - cardWidth;
        nextPlayerCardY = (int)(this.getHeight() * 0.9) - cardWidth;
        nextDealerCardX = this.getWidth() / 2 - cardWidth;
        nextDealerCardY = (int)(this.getHeight() * 0.001);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image deck = ImageHandler.getImage("Back");
        g.drawImage(deck, deckX, deckY, null);
        Image cardImage;
        for(CardVisual cardVisual : cardVisuals) {
            if (cardVisual.isHidden)
                cardImage = ImageHandler.getImage("Back");
            else {
                cardImage = ImageHandler.getImage(cardVisual.card.value + "_" + cardVisual.card.suit);
            }
            g.drawImage(cardImage, cardVisual.x, cardVisual.y, null);
        }
    }
}
