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
    private int nextDealerCardX, nextDealerCardY;
    private int nextPlayerCardX, nextPlayerCardY;
    private int deckX, deckY;
    private int cardWidth = 209;
    private int cardHeight = 303;
    private int balanceChange;
    private Logic.EndStates dealEndState;
    private int waitTimeBetweenDealerActions = 500;
    private java.util.Queue<Object[]> cardVisualsToBeDealt = new LinkedList<>();
    private java.util.Queue<Runnable> executionQueue = new LinkedList();
    private final Runnable instantPlayerBlackjackAction = () -> {
        uiPanel.setDealResult(balanceChange, dealEndState);
    };
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
            Object[] cardVisualAndDealDestination = cardVisualsToBeDealt.poll();
            if (cardVisualAndDealDestination != null) {
                CardVisual cardVisual = (CardVisual) cardVisualAndDealDestination[0];
                Logic.CardDealtTo cardDealtTo = (Logic.CardDealtTo) cardVisualAndDealDestination[1];
                audioPlayer.playOnce();
                cardVisuals.add(cardVisual);
                if (cardDealtTo == Logic.CardDealtTo.Player) {
                    nextPlayerCardX += cardWidth;
                    cardVisual.moveTo(nextPlayerCardX / 2, nextPlayerCardY);
                } else {
                    nextDealerCardX += cardWidth;
                    cardVisual.moveTo(nextDealerCardX / 2, nextDealerCardY);
                }
            }
    };
    public void fireNewDeal(){
        cardVisuals.clear();
        deckX = this.getWidth() - (int) (cardWidth * 1.5) - cardWidth / 2;
        deckY = this.getHeight() / 6 - cardHeight / 2;
        nextPlayerCardX = this.getWidth() / 2 - cardWidth;
        nextPlayerCardY = (int) (this.getHeight() / 1.4);
        nextDealerCardX = this.getWidth() / 2 - cardWidth / 2 * 3;
        nextDealerCardY = cardWidth/8;
    }
    public void fireCardDeal(boolean isHidden, Card card, Logic.CardDealtTo cardDealtTo) {
        CardVisual cardVisual = new CardVisual(card, deckX, deckY, this, isHidden);
        cardVisualsToBeDealt.add(new Object[]{cardVisual, cardDealtTo});
        executionQueue.add(cardDealAction);
    }
    public void firePlayerInstantBlackJack(){
        executionQueue.add(revealHiddenCardAction);
        executionQueue.add(instantPlayerBlackjackAction);
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
    public void scale(){
        cardWidth = this.getParent().getWidth() / 8;
        cardHeight = this.getParent().getHeight() / 4;
        ImageHandler.scaleImages(cardWidth, cardHeight);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image deck = ImageHandler.getImage("Back");
        g.drawImage(deck, this.getWidth() - (int)(cardWidth * 1.5) - cardWidth / 2, this.getHeight()/6 - cardHeight/2, null);
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
