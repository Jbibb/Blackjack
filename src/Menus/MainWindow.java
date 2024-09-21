package Menus;

import Logic.PlayerModel;
import Modes.Blackjack.BlackjackGame;
import Modes.Blackjack.StrategyTable;
import Modes.Blackjack.StrategyTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements Runnable {

    private final static int DEFAULT_WIDTH = 1024, DEFAULT_HEIGHT = 800;

    private JPanel contentPanel;
    private JButton returnButton;
    private PlayerInfoPanel playerInfoPanel;
    private GameSession currentGameSession = null;

    private PlayerListPanel playerListPanel = new PlayerListPanel();
    private ModeChoicePanel modeChoicePanel = new ModeChoicePanel(this, playerListPanel);
    private JScrollPane strategyTableJScrollPane;
    private StrategyTableModel strategyTableModel;

    @Override
    public void run(){
        getContentPane().setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Palette.BACKGROUND_COLOR);

        returnButton = new JButton("Return");
        returnButton.setFocusable(false);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToGameChoice();
            }
        });

        playerInfoPanel = new PlayerInfoPanel(returnButton);
        getContentPane().add(playerInfoPanel, BorderLayout.NORTH);
        contentPanel.setBackground(Palette.BACKGROUND_COLOR);

        StrategyTable strategyTable = new Modes.Blackjack.StrategyTable();
        strategyTableModel = strategyTable.getModel();

        strategyTableJScrollPane = new JScrollPane(strategyTable);
        strategyTable.setAlignmentX(Component.CENTER_ALIGNMENT);
        strategyTableJScrollPane.setBackground(Palette.BACKGROUND_COLOR);
        strategyTableJScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        strategyTableJScrollPane.setAlignmentY(Component.CENTER_ALIGNMENT);


        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        goToGameChoice();
    }

    public void startGameSession(GameSession gameSession) {
        returnButton.setVisible(true);
        this.contentPanel.removeAll();
        this.contentPanel.add(gameSession.getGamePanel(), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
        gameSession.getGamePanel().scale();

    }



    public void goToGameChoice() {
        if(currentGameSession != null) {
            currentGameSession.end();
        }
        returnButton.setVisible(false);
        this.contentPanel.removeAll();

        this.contentPanel.add(modeChoicePanel, BorderLayout.NORTH);
        this.contentPanel.add(playerListPanel, BorderLayout.SOUTH);
        contentPanel.add(strategyTableJScrollPane, BorderLayout.CENTER);
        playerInfoPanel.setPlayerModel(null);
        this.revalidate();
        this.repaint();
    }

    public void setCurrentGameSession(GameSession currentGameSession) {
        this.currentGameSession = currentGameSession;
    }

    public void setPlayerModel(PlayerModel playerModel) {
        playerInfoPanel.setPlayerModel(playerModel);
    }

    public StrategyTableModel getStrategyTableModel() {
        return strategyTableModel;
    }
}
