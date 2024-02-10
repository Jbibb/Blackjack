import java.awt.*;

class CardVisual extends Thread {
    public Card card;
    public boolean isReversed = true;
    public int x, y, destinationX, destinationY;
    private int animationLength = 75;
    private Component parent;

    public CardVisual(Card card, int x, int y, Component parent){
        this.card = card;
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    public void move(int destinationX, int destinationY){
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.start();
    }

    public void reverse(){
        isReversed = false;
        parent.repaint();
    }

    public void run() {
        double xDistance = destinationX - x;
        double yDistance = destinationY - y;
        int frameTime = 4;
        double speedX = (xDistance / animationLength) * frameTime;
        double speedY = (yDistance / animationLength) * frameTime;

        while (x != destinationX || y != destinationY) {
            try {
                Thread.sleep(frameTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.x += speedX;
            this.y += speedY;

            if (xDistance < 0 && this.x < destinationX)
                this.x = destinationX;
            if (xDistance > 0 && this.x > destinationX)
                this.x = destinationX;

            if (yDistance < 0 && this.y < destinationY)
                this.y = destinationY;
            if (yDistance > 0 && this.y > destinationY)
                this.y = destinationY;

            parent.repaint();
        }
    }
}