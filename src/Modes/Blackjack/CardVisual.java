package Modes.Blackjack;

import Logic.Card;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CardVisual {
    public Card card;
    public boolean isHidden;
    public boolean isMoving;
    public Logic.CardDealtTo dealtTo;
    public int x, y, destinationX, destinationY;
    private int animationLength = 100;
    private Component parent;
    private int frameTime = 8;

    private double speedX;
    private double speedY;
    private double xDistance;
    private double yDistance;

    private Thread animationThread = new Thread(new Runnable() {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(frameTime);
                } catch (InterruptedException e){
                    isMoving = false;
                    return;
                }

                x += speedX;
                y += speedY;

                if (xDistance < 0 && x < destinationX)
                    x = destinationX;
                if (xDistance > 0 && x > destinationX)
                    x = destinationX;

                if (yDistance < 0 && y < destinationY)
                    y = destinationY;
                if (yDistance > 0 && y > destinationY)
                    y = destinationY;

                //parent.repaint();
                if (x == destinationX && y == destinationY) {
                    isMoving = false;
                    return;
                }
            }
        }});

    public CardVisual(Card card, int x, int y, Component parent, boolean isHidden, Logic.CardDealtTo dealtTo){
        this.card = card;
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.isHidden = isHidden;
        this.dealtTo = dealtTo;
    }

    public void moveTo(int destinationX, int destinationY){
        this.destinationX = destinationX;
        this.destinationY = destinationY;

        xDistance = destinationX - x;
        yDistance = destinationY - y;
        speedX = (xDistance / animationLength) * frameTime;
        speedY = (yDistance / animationLength) * frameTime;

        isMoving = true;
        animationThread.start();
    }

    public void hide(){
        isHidden = true;
        parent.repaint();
    }

    public void reveal(){
        isHidden = false;
        parent.repaint();
    }
}
