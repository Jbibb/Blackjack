import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements Runnable {

    private Card[] deck;
    private CardPanel cardPanel;
    private JPanel buttonPanel;
    private JButton dealButton, doubleButton;
    private JPanel betPanel;
    private JPanel pageStartPanel;
    private JTextField moneyField;
    private JSpinner betSpinner;
    private int money = 5_000;
    private boolean doubleDown = false;
    private boolean playerHasHit = false; //(so he can't double down)
    private int currentBet;
    public Window(Card[] deck){
        this.deck = deck;
    }

    @Override
    public void run() {
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        pageStartPanel = new JPanel(flowLayout);
        pageStartPanel.setBackground(new Color(20, 100, 20));

        moneyField = new JTextField();
        moneyField.setBorder(BorderFactory.createEmptyBorder());
        moneyField.setEditable(false);
        moneyField.setText("$" + String.valueOf(money));
        moneyField.setFont(new Font("Arial", 3, 22));
        moneyField.setForeground(Color.YELLOW);
        moneyField.setBackground(new Color(20, 100, 20));

        betPanel = new JPanel();
        betPanel.setLayout(new FlowLayout());

        betSpinner = new JSpinner(new SpinnerNumberModel(50, 50, 1_000, 50));
        betPanel.add(betSpinner);

        pageStartPanel.add(moneyField);

        this.cardPanel = new CardPanel(deck, this);
        cardPanel.setBackground(new Color(20, 100, 20));

        setSize(975, 795);
        cardPanel.scale();

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(cardPanel, BorderLayout.CENTER);
        dealButton = new JButton("Deal");
        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int spinnerBetValue = (Integer)(betSpinner.getValue());
                if(spinnerBetValue <= money) {
                    currentBet = spinnerBetValue;
                    money -= currentBet;

                    moneyField.setText("$" + money);
                    moneyField.revalidate();

                    betSpinner.setVisible(false);
                    betSpinner.getParent().revalidate();

                    dealButton.setVisible(false);
                    if (resultField != null) {
                        resultField.setVisible(false);
                        resultField.getParent().getParent().revalidate();
                        resultField.getParent().revalidate();
                    }
                    cardPanel.deal();
                }
            }
        });

        this.buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton standButton = new JButton("Stand");
        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setVisible(false);
                cardPanel.stand();
            }
        });

        JButton hitButton = new JButton("Hit");
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerHasHit = true;
                buttonPanel.setVisible(false);
                cardPanel.hit();
            }
        });

        doubleButton = new JButton("Double Down");
        doubleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(money >= currentBet && !playerHasHit) {
                    money -= currentBet;
                    doubleDown = true;
                    buttonPanel.setVisible(false);
                    cardPanel.doubleDown();
                }
            }
        });

        buttonPanel.add(dealButton);

        buttonPanel.add(standButton);
        standButton.setVisible(false);

        buttonPanel.add(hitButton);
        hitButton.setVisible(false);

        buttonPanel.add(doubleButton);
        doubleButton.setVisible(false);

        buttonPanel.add(betPanel);

        this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
        buttonPanel.setVisible(true);
        buttonPanel.revalidate();

        this.getContentPane().add(pageStartPanel, BorderLayout.PAGE_START);

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void offerChoice(){
        this.buttonPanel.setVisible(true);
        for(Component c : buttonPanel.getComponents())
            c.setVisible(true);
        if(playerHasHit)
            doubleButton.setVisible(false);
        dealButton.setVisible(false);
        buttonPanel.revalidate();
    }

    private JTextField resultField;
    public void setDealResult(int playerScore, int dealerScore, boolean playerHasBlackJack, boolean dealerHasBlackJack){
        System.out.println("Player: " + playerScore + " Player BlackJack: " + playerHasBlackJack + "\nDealer: " + dealerScore + " Dealer BlackJack: " + dealerHasBlackJack);
        String labelMessage;
        if(playerHasBlackJack) {
            labelMessage = "Player Wins!";
            money += (int) (currentBet * 1.5d);
        } else if(dealerScore > 21) {
            labelMessage = "Dealer Bust!";
            money += doubleDown ? currentBet * 4 : currentBet * 2;
        } else if (playerScore > 21)
            labelMessage = "Player Bust!";
        else if(playerScore > dealerScore) {
            labelMessage = "Player Wins!";
            money += doubleDown ? currentBet * 4 : currentBet * 2;
        } else if(playerScore == dealerScore) {
            if (playerHasBlackJack && !dealerHasBlackJack) {
                labelMessage = "Player Wins!";
                money += doubleDown ? currentBet * 4 : currentBet * 2;
            } else if (!playerHasBlackJack && dealerHasBlackJack)
                labelMessage = "Dealer Wins!";
            else {
                labelMessage = "Tie";
                money += doubleDown ? currentBet * 2 : currentBet;
            }
        } else labelMessage = "Dealer Wins!";
        if(resultField == null) {
            resultField = new JTextField();
            resultField.setBackground(new Color(20, 100, 20));
            resultField.setBorder(BorderFactory.createEmptyBorder());
            resultField.setForeground(Color.ORANGE);
            resultField.setFont(new Font("Arial", 2, 30));
            resultField.setEditable(false);
            pageStartPanel.add(resultField);
        }

        doubleDown = false;
        playerHasHit = false;

        moneyField.setText("$" + money);
        moneyField.getParent().revalidate();

        resultField.setText(labelMessage);
        resultField.setVisible(true);

        resultField.getParent().revalidate();

        buttonPanel.setVisible(true);
        for(Component c : buttonPanel.getComponents())
            c.setVisible(false);
        dealButton.setVisible(true);
        betPanel.setVisible(true);
        betSpinner.setVisible(true);

        betPanel.revalidate();
        this.getContentPane().revalidate();
    }
}
