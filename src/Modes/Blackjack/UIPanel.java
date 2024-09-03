package Modes.Blackjack;

import Menus.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import Logic.PlayerModel;
import Menus.Palette;
import Modes.GamePanel;

public class UIPanel extends JPanel implements GamePanel {
    private String fontName = "Verdana";
    private Logic gameLogic;
    private JPanel buttonPanel;
    private JButton dealButton, doubleButton;
    private JPanel betPanel;
    private JPanel pageStartPanel;
    private JTextField moneyField, betField, balanceField;
    private JSpinner betSpinner;
    private int balance = 0;
    private boolean doubleDown = false;
    private boolean playerHasHit = false;
    private int currentBet = 0;
    private JTextField resultField;
    public PlayerModel playerModel;
    private AudioPlayer backgroundMusicPlayer = new AudioPlayer("Walk Through The Park - TrackTribe.wav");
    public UIPanel(PlayerModel playerModel) {
        this.playerModel = playerModel;
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        pageStartPanel = new JPanel(flowLayout);

        pageStartPanel.setBackground(Palette.BACKGROUND_COLOR);

        moneyField = new JTextField();
        moneyField.setBorder(BorderFactory.createEmptyBorder());
        moneyField.setEditable(false);
        moneyField.setText("$" + playerModel.getMoney());
        moneyField.setFont(new Font(fontName, Font.BOLD, 22));
        moneyField.setForeground(Color.YELLOW);
        moneyField.setBackground(Palette.BACKGROUND_COLOR);

        betField = new JTextField();
        betField.setBorder(BorderFactory.createEmptyBorder());
        betField.setEditable(false);
        betField.setText(" Current bet: $" + currentBet);
        betField.setFont(new Font(fontName, 3, 16));
        betField.setForeground(Color.ORANGE);
        betField.setBackground(Palette.BACKGROUND_COLOR);

        balanceField = new JTextField();
        balanceField.setBorder(BorderFactory.createEmptyBorder());
        balanceField.setEditable(false);
        balanceField.setText(" Balance: " + balance + "$\t\t");
        balanceField.setFont(new Font(fontName, 3, 16));
        balanceField.setForeground(Color.YELLOW);
        balanceField.setBackground(Palette.BACKGROUND_COLOR);

        betPanel = new JPanel();
        betPanel.setLayout(new FlowLayout());

        betSpinner = new JSpinner(new SpinnerNumberModel(50, 50, 5_000, 50));
        betSpinner.setFocusable(false);
        betPanel.add(betSpinner);

        resultField = new JTextField();
        resultField.setBackground(Palette.BACKGROUND_COLOR);
        resultField.setBorder(BorderFactory.createEmptyBorder());
        resultField.setForeground(Color.ORANGE);
        resultField.setFont(new Font(fontName, 4, 24));
        resultField.setEditable(false);

        pageStartPanel.add(moneyField);
        pageStartPanel.add(betField);
        pageStartPanel.add(balanceField);
        pageStartPanel.add(resultField);

        CardPanel cardPanel = new CardPanel(this);
        gameLogic = new Logic(cardPanel);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                cardPanel.scale();
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });



        this.setLayout(new BorderLayout());
        this.add(cardPanel, BorderLayout.CENTER);
        dealButton = new JButton("Deal");
        dealButton.setFocusable(false);
        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int spinnerBetValue = (Integer)(betSpinner.getValue());
                if(spinnerBetValue <= playerModel.getMoney()) {
                    currentBet = spinnerBetValue;
                    playerModel.setMoney(playerModel.getMoney() - currentBet);

                    moneyField.setText("$" + playerModel.getMoney());
                    moneyField.revalidate();

                    betField.setText(" Current bet: $" + currentBet);
                    betField.revalidate();

                    betSpinner.setVisible(false);
                    betSpinner.getParent().revalidate();

                    dealButton.setVisible(false);
                    buttonPanel.setVisible(false);

                    resultField.setVisible(false);
                    resultField.getParent().getParent().revalidate();
                    resultField.getParent().revalidate();

                    gameLogic.setBet(currentBet);
                    gameLogic.deal();
                }
            }
        });

        this.buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton standButton = new JButton("Stand");
        standButton.setFocusable(false);
        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setVisible(false);
                gameLogic.stand();
            }
        });

        JButton hitButton = new JButton("Hit");
        hitButton.setFocusable(false);
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerHasHit = true;
                buttonPanel.setVisible(false);
                gameLogic.hit();
            }
        });

        doubleButton = new JButton("Double Down");
        doubleButton.setFocusable(false);
        doubleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(playerModel.getMoney() >= currentBet) {
                    playerModel.setMoney(playerModel.getMoney() - currentBet);
                    moneyField.setText("$" + playerModel.getMoney());

                    currentBet *= 2;
                    betField.setText(" Current bet: $" + currentBet);
                    betField.revalidate();

                    doubleDown = true;
                    buttonPanel.revalidate();
                    buttonPanel.setVisible(false);
                    gameLogic.doubleDown();
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

        buttonPanel.setBackground(Palette.ALT_BACKGROUND_COLOR);
        buttonPanel.add(betPanel);

        this.add(buttonPanel, BorderLayout.PAGE_END);
        buttonPanel.revalidate();
        buttonPanel.setVisible(true);
        buttonPanel.revalidate();

        this.add(pageStartPanel, BorderLayout.PAGE_START);
        backgroundMusicPlayer.play();
    }

    public void offerChoice() {
        for(Component c : buttonPanel.getComponents()) {
            c.setVisible(true);
        }
        betPanel.setVisible(false);
        if(playerHasHit)
            doubleButton.setVisible(false);
        dealButton.setVisible(false);
        buttonPanel.revalidate();
        buttonPanel.setVisible(true);
    }
    public void setDealResult(int balanceChange, Logic.EndStates endState){
        String labelMessage = endState.label;
        Color labelColor;
        Color loseMessageColor = Palette.NEGATIVE_FONT_COLOR;
        Color winMessageColor = Palette.POSITIVE_FONT_COLOR;

        balance += balanceChange;
        playerModel.setMoney(playerModel.getMoney() + balanceChange + currentBet);

        resultField.setText(labelMessage);
        if(endState == Logic.EndStates.PlayerWins || endState == Logic.EndStates.DealerBust) {
            labelColor = winMessageColor;
        } else if (endState == Logic.EndStates.Tie) {
            labelColor = Palette.DEFAULT_FONT_COLOR;
        } else {
            labelColor = loseMessageColor;
        }

        resultField.setForeground(labelColor);
        resultField.setVisible(true);
        resultField.repaint();
        resultField.getParent().revalidate();
        resultField.revalidate();

        balanceField.setText(" Balance: " + balance + "$\t\t");
        if(balance > 0)
            balanceField.setForeground(Palette.POSITIVE_FONT_COLOR);
        else if (balance == 0)
            balanceField.setForeground(Color.YELLOW);
        else
            balanceField.setForeground(Palette.NEGATIVE_FONT_COLOR);

        balanceField.revalidate();

        doubleDown = false;
        playerHasHit = false;

        if(playerModel.getMoney() == 0)
            //todo
            ;
        moneyField.setText("$" + playerModel.getMoney());
        moneyField.getParent().revalidate();

        buttonPanel.revalidate();
        buttonPanel.setVisible(true);
        for(Component c : buttonPanel.getComponents())
            c.setVisible(false);
        dealButton.setVisible(true);
        betPanel.setVisible(true);
        betSpinner.setVisible(true);

        betPanel.revalidate();

        resultField.setVisible(true);
    }

    @Override
    public void end() {
        backgroundMusicPlayer.stop();
    }
}
