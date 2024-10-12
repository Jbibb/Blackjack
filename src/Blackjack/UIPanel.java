package Blackjack;

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

public class UIPanel extends JLayeredPane implements GamePanel {
    private GameLogic gameLogic;
    private int balance = 0;
    private boolean doubleDown = false;
    private boolean playerHasHit = false;
    private int currentBet = 0;
    public PlayerModel playerModel;

    private String fontName = "Verdana";

    private JLayeredPane layeredPane = this;
    private JPanel borderPanel;

    private JPanel betPanel;
    private JPanel buttonPanel;
    private JSpinner betSpinner;
    private JButton dealButton, doubleButton;

    private JPanel pageStartPanel;

    private JPanel leftPageStartPanel;
    private JTextField moneyField, betField, balanceField;
    private JTextField resultField;

    private JPanel resultPanel;

    private JPanel rightPageStartPanel;


    private JButton settingsButton;
    private JPanel settingsPanel;
    private JCheckBox strategyTableCheckbox;
    private JSpinner delaySpinner;
    private JButton applyDelayButton;

    private JButton tipButton;
    private JPanel tipPanel;
    private JTextArea tipField;

    private AudioPlayer backgroundMusicPlayer = new AudioPlayer("Walk Through The Park - TrackTribe.wav");


    public UIPanel(PlayerModel playerModel, StrategyTableModel strategyTableModel, int width, int height, int minimumBet, int maximumBet, int betInterval, int deckAmount) {
        this.playerModel = playerModel;

        borderPanel = new JPanel(new BorderLayout());
        this.add(borderPanel, DEFAULT_LAYER);

        pageStartPanel = new JPanel(new BorderLayout());
        pageStartPanel.setBackground(Palette.BACKGROUND_COLOR);

        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        leftPageStartPanel = new JPanel(flowLayout);
        leftPageStartPanel.setBackground(Palette.BACKGROUND_COLOR);

        flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);
        rightPageStartPanel = new JPanel(flowLayout);
        rightPageStartPanel.setBackground(Palette.BACKGROUND_COLOR);

        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(Palette.BACKGROUND_COLOR);

        pageStartPanel.add(leftPageStartPanel, BorderLayout.LINE_START);
        pageStartPanel.add(rightPageStartPanel, BorderLayout.LINE_END);

        this.add(resultPanel, PALETTE_LAYER);
        resultPanel.setOpaque(false);
        resultPanel.setBounds(width/2, height/2, 100, 100);


        {
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
        }
        {
        betPanel = new JPanel();
        betPanel.setBorder(BorderFactory.createLineBorder(Palette.BORDER_COLOR));
        betPanel.setLayout(new FlowLayout());

        betSpinner = new JSpinner(new SpinnerNumberModel(minimumBet, minimumBet, maximumBet, betInterval));
        betSpinner.setFocusable(false);
        betPanel.add(betSpinner);

        resultField = new JTextField();
        resultField.setBackground(Palette.ALT_BACKGROUND_COLOR);
        resultField.setBorder(BorderFactory.createLineBorder(Palette.POSITIVE_FONT_COLOR));
        resultField.setForeground(Color.ORANGE);
        resultField.setFont(new Font(fontName, Font.PLAIN, 24));
        resultField.setHorizontalAlignment(SwingConstants.CENTER);
        resultField.setEditable(false);
        resultField.setVisible(false);
        resultPanel.setVisible(false);

        strategyTableCheckbox = new JCheckBox("Toggle automatic play");
        strategyTableCheckbox.setBackground(Palette.HIGHLIGHT_COLOR);
        strategyTableCheckbox.setForeground(Palette.DEFAULT_FONT_COLOR);
        strategyTableCheckbox.setFont(Palette.DEFAULT_FONT);
        strategyTableCheckbox.setFocusable(false);
        strategyTableCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameLogic.setUseStrategyTable(strategyTableCheckbox.isSelected());
            }
        });
        }

        settingsPanel = new JPanel();
        this.add(settingsPanel, POPUP_LAYER);
        settingsPanel.setBounds(50, 50, 250, 100);
        settingsPanel.setBackground(Palette.HIGHLIGHT_COLOR);
        settingsPanel.setBorder(BorderFactory.createLineBorder(Palette.ALT_BACKGROUND_COLOR));
        settingsPanel.setVisible(false);


        settingsButton = new JButton("Settings");
        settingsButton.setHorizontalAlignment(SwingConstants.RIGHT);
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsPanel.setVisible(!settingsPanel.isVisible());
            }
        });
        settingsButton.setFocusable(false);

        delaySpinner = new JSpinner(new SpinnerNumberModel(350, 0, 2_000, 10));
        applyDelayButton = new JButton("Apply");
        applyDelayButton.setFocusable(false);

        JLabel delayLabel = new JLabel("Set delay between dealer actions (ms)");
        delayLabel.setBackground(Palette.HIGHLIGHT_COLOR);
        delayLabel.setForeground(Palette.DEFAULT_FONT_COLOR);
        delayLabel.setFont(new Font(fontName, Font.PLAIN, 12));

        tipButton = new JButton("Tip");
        tipButton.setHorizontalAlignment(SwingConstants.RIGHT);
        tipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tipField.setText(gameLogic.getTip());
                tipPanel.setVisible(!tipPanel.isVisible());
            }
        });
        tipButton.setFocusable(false);

        tipPanel = new JPanel();
        this.add(tipPanel, POPUP_LAYER);
        tipPanel.setBounds(0, 200, 300, 300);
        tipPanel.setBackground(Palette.HIGHLIGHT_COLOR);
        tipPanel.setBorder(BorderFactory.createLineBorder(Palette.ALT_BACKGROUND_COLOR));
        tipPanel.setVisible(false);

        tipField = new JTextArea();
        tipField.setFont(new Font(fontName, Font.PLAIN, 16));
        tipField.setForeground(Palette.DEFAULT_FONT_COLOR);
        tipField.setBackground(Palette.HIGHLIGHT_COLOR);
        tipField.setEditable(false);
        tipField.setLineWrap(true);
        tipField.setWrapStyleWord(true);

        JScrollPane tipScrollPane = new JScrollPane(tipField);
        tipScrollPane.setPreferredSize(new Dimension(300 - 5, 300 - 5));

        tipPanel.add(tipScrollPane);

        leftPageStartPanel.add(moneyField);
        leftPageStartPanel.add(betField);
        leftPageStartPanel.add(balanceField);

        resultPanel.add(resultField, BorderLayout.CENTER);

        rightPageStartPanel.add(tipButton);
        rightPageStartPanel.add(settingsButton);

        settingsPanel.add(strategyTableCheckbox);
        settingsPanel.add(delayLabel);
        settingsPanel.add(delaySpinner);
        settingsPanel.add(applyDelayButton);

        CardPanel cardPanel = new CardPanel(this, (Integer) delaySpinner.getModel().getValue());
        gameLogic = new GameLogic(cardPanel, strategyTableModel, deckAmount);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                scale();
                cardPanel.scale();
            }
            @Override
            public void componentMoved(ComponentEvent e) {}
            @Override
            public void componentShown(ComponentEvent e) {}
            @Override
            public void componentHidden(ComponentEvent e) {}
        });

        applyDelayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardPanel.changeWaitTimeBetweenDealerActions((Integer) delaySpinner.getModel().getValue());
            }
        });



        borderPanel.add(cardPanel, BorderLayout.CENTER);
        dealButton = new JButton("Deal");
        dealButton.setFocusable(false);
        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newDeal();
            }
        });

        buttonPanel = new JPanel();
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

        borderPanel.add(buttonPanel, BorderLayout.PAGE_END);
        buttonPanel.revalidate();
        buttonPanel.setVisible(true);
        buttonPanel.revalidate();

        borderPanel.add(pageStartPanel, BorderLayout.PAGE_START);

        backgroundMusicPlayer.play();
    }

    private void newDeal() {
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
            resultPanel.setVisible(false);
            resultField.getParent().getParent().revalidate();
            resultField.getParent().revalidate();

            gameLogic.setBet(currentBet);
            gameLogic.deal();
        }
    }

    public void scale(){
        int width = layeredPane.getParent().getWidth();
        int height = layeredPane.getParent().getHeight();
        layeredPane.setBounds(0, 0, width, height);
        borderPanel.setBounds(0, 0, width, height);
        resultPanel.setBounds(width/2 - 75, height/2 - 100, 170, 100);
    }

    public void offerChoice() {

        if(tipPanel.isVisible())
            tipField.setText(gameLogic.getTip());

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
    public void setDealResult(int balanceChange, GameLogic.EndStates endState){
        String labelMessage = endState.label;
        Color labelColor;
        Color loseMessageColor = Palette.NEGATIVE_FONT_COLOR;
        Color winMessageColor = Palette.POSITIVE_FONT_COLOR;

        balance += balanceChange;
        playerModel.setMoney(playerModel.getMoney() + balanceChange + currentBet);

        resultField.setText(labelMessage);
        if(endState == GameLogic.EndStates.PlayerWins || endState == GameLogic.EndStates.DealerBust) {
            labelColor = winMessageColor;
        } else if (endState == GameLogic.EndStates.Tie) {
            labelColor = Palette.DEFAULT_FONT_COLOR;
        } else {
            labelColor = loseMessageColor;
        }

        resultField.setForeground(labelColor);
        resultField.setBorder(BorderFactory.createLineBorder(labelColor));
        resultField.setVisible(true);
        resultPanel.setVisible(true);
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
            System.out.println("yer done");
            //todo
        moneyField.setText("$" + playerModel.getMoney());
        moneyField.getParent().revalidate();

        if(!gameLogic.isUsingStrategyTable()) {
            buttonPanel.revalidate();
            buttonPanel.setVisible(true);
            for (Component c : buttonPanel.getComponents())
                c.setVisible(false);
            dealButton.setVisible(true);
            betPanel.setVisible(true);
            betSpinner.setVisible(true);
            betPanel.revalidate();
        } else {
            newDeal();
        }

        if(tipPanel.isVisible())
            tipField.setText("");

        resultField.setVisible(true);
        resultPanel.setVisible(true);
    }

    @Override
    public void end() {
        backgroundMusicPlayer.stop();
        gameLogic.setUseStrategyTable(false);
    }
}
