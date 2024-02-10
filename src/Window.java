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
    private JTextField moneyField, betField, balanceField;
    private JSpinner betSpinner;
    private int balance = 0;
    private int money = 5_000;
    private boolean doubleDown = false;
    private boolean playerHasHit = false;
    private int currentBet = 0;
    private AudioPlayer audioPlayer;
    public Window(Card[] deck, AudioPlayer audioPlayer){
        this.deck = deck;
        this.audioPlayer = audioPlayer;
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
        moneyField.setText("$" + money);
        moneyField.setFont(new Font("Arial", Font.BOLD, 22));
        moneyField.setForeground(Color.YELLOW);
        moneyField.setBackground(new Color(20, 100, 20));

        betField = new JTextField();
        betField.setBorder(BorderFactory.createEmptyBorder());
        betField.setEditable(false);
        betField.setText(" Current bet: $" + currentBet);
        betField.setFont(new Font("Arial", 3, 16));
        betField.setForeground(Color.ORANGE);
        betField.setBackground(new Color(20, 100, 20));

        balanceField = new JTextField();
        balanceField.setBorder(BorderFactory.createEmptyBorder());
        balanceField.setEditable(false);
        balanceField.setText(" Balance: " + balance + "$\t\t");
        balanceField.setFont(new Font("Arial", 3, 16));
        balanceField.setForeground(Color.YELLOW);
        balanceField.setBackground(new Color(20, 100, 20));

        betPanel = new JPanel();
        betPanel.setLayout(new FlowLayout());

        betSpinner = new JSpinner(new SpinnerNumberModel(50, 50, 5_000, 50));
        betPanel.add(betSpinner);

        pageStartPanel.add(moneyField);
        pageStartPanel.add(betField);
        pageStartPanel.add(balanceField);

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

                    betField.setText(" Current bet: $" + currentBet);
                    betField.revalidate();

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
                    moneyField.setText("$" + money);

                    betField.setText(" Current bet: $" + 2*currentBet);
                    betField.revalidate();

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
        Color labelColor;
        if(playerHasBlackJack) {
            labelMessage = "Player Wins!";
            labelColor = Color.GREEN;
            money += (int) (currentBet * 1.5d);
            balance += (int)(currentBet * 0.5d);
        } else if(dealerScore > 21) {
            labelMessage = "Dealer Bust!";
            labelColor = Color.GREEN;
            money += doubleDown ? currentBet * 4 : currentBet * 2;
            balance += doubleDown ? currentBet * 2 : currentBet;
        } else if (playerScore > 21) {
            labelMessage = "Player Bust!";
            labelColor = Color.RED;
            balance -= doubleDown ? currentBet * 2 : currentBet;;
        } else if(playerScore > dealerScore) {
            labelMessage = "Player Wins!";
            labelColor = Color.GREEN;
            money += doubleDown ? currentBet * 4 : currentBet * 2;
            balance += doubleDown ? currentBet * 2 : currentBet;
        } else if(playerScore == dealerScore) {
            if (playerHasBlackJack && !dealerHasBlackJack) {
                labelMessage = "Player Wins!";
                labelColor = Color.GREEN;
                money += doubleDown ? currentBet * 4 : currentBet * 2;
                balance += doubleDown ? currentBet * 2 : currentBet;
            } else if (!playerHasBlackJack && dealerHasBlackJack) {
                labelMessage = "Dealer Wins!";
                labelColor = Color.RED;
                balance -= doubleDown ? currentBet * 2 : currentBet;
            } else {
                labelMessage = "Tie";
                labelColor = Color.YELLOW;
                money += doubleDown ? currentBet * 2 : currentBet;
            }
        } else {
            labelMessage = "Dealer Wins!";
            labelColor = Color.RED;
            balance -= doubleDown ? currentBet * 2 : currentBet;
        }
        if(resultField == null) {
            resultField = new JTextField();
            resultField.setBackground(new Color(20, 100, 20));
            resultField.setBorder(BorderFactory.createEmptyBorder());
            resultField.setForeground(Color.ORANGE);
            resultField.setFont(new Font("Arial", 2, 30));
            resultField.setEditable(false);
            pageStartPanel.add(resultField);
        }

        balanceField.setText(" Balance: " + balance + "$\t\t");
        if(balance > 0)
            balanceField.setForeground(Color.GREEN);
        else if (balance == 0)
            balanceField.setForeground(Color.YELLOW);
        else
            balanceField.setForeground(new Color(242, 73, 73));

        balanceField.revalidate();

        doubleDown = false;
        playerHasHit = false;

        if(money == 0)
            money = 5_000;

        moneyField.setText("$" + money);
        moneyField.getParent().revalidate();

        resultField.setText(labelMessage);
        resultField.setForeground(labelColor);
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
