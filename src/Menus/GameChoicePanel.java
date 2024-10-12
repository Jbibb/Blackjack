package Menus;

import Blackjack.SettingsPanel;
import Logic.PlayerModel;
import Blackjack.BlackjackGame;
import Modes.Game;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class GameChoicePanel extends JPanel {
    private static Game[] games = new Game[] {
            new BlackjackGame()
    };

    private HashMap<JButton, Game> buttonsAndModes = new HashMap<>();
    public GameChoicePanel(MainWindow mainWindow, PlayerListPanel playerListPanel){
        for (Game game : games){
            JButton button = new JButton(game.getName());
            button.setPreferredSize(new Dimension(100, 30));
            add(button);
            button.setFocusable(false);
            buttonsAndModes.put(button, game);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Game game = buttonsAndModes.get(button);
                    PlayerModel playerModel = playerListPanel.getSelectedPlayer();
                    if(playerModel != null) {
                        if(game instanceof BlackjackGame) {
                            if(playerModel != mainWindow.getPlayerModel()) {
                                mainWindow.setPlayerModel(playerModel);
                                game.setPlayerModel(playerModel);

                                mainWindow.showInTheCenter((panel) -> {
                                    JPanel contentPanel = new JPanel();
                                    contentPanel.setLayout(new GridLayout(1, 2));
                                    SettingsPanel settingsPanel = new SettingsPanel();
                                    contentPanel.add(settingsPanel);

                                    JPanel strategyTablePanel = new JPanel(new BorderLayout(20, 20));
                                    strategyTablePanel.setBackground(Palette.BACKGROUND_COLOR);
                                    strategyTablePanel.add(mainWindow.getStrategyTableJScrollPane(), BorderLayout.CENTER);

                                    JLabel strategyTableLabel = new JLabel("Strategy table is used for automatic play.");
                                    strategyTableLabel.setHorizontalAlignment(SwingConstants.CENTER);
                                    strategyTableLabel.setBackground(Palette.BACKGROUND_COLOR);
                                    strategyTableLabel.setForeground(Palette.DEFAULT_FONT_COLOR);
                                    strategyTableLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
                                    strategyTablePanel.add(strategyTableLabel, BorderLayout.PAGE_START);

                                    contentPanel.add(strategyTablePanel);

                                    panel.setLayout(new BorderLayout());
                                    panel.add(contentPanel, BorderLayout.CENTER);

                                    JButton startButton = new JButton("Start");
                                    startButton.addActionListener((event) -> {
                                        ((BlackjackGame) game).setStrategyTableModel(mainWindow.getStrategyTableModel());
                                        ((BlackjackGame) game).setTableRules(settingsPanel.getTableMinimum(), settingsPanel.getTableMaximum(), settingsPanel.getBetInterval(), settingsPanel.getDeckAmount());
                                        GameSession gameSession = new GameSession(game, game.getGamePanel(mainWindow.getWidth(), mainWindow.getHeight()));
                                        mainWindow.setCurrentGameSession(gameSession);
                                        mainWindow.startGameSession(gameSession);
                                    });

                                    JPanel startButtonPanel = new JPanel();
                                    startButtonPanel.setBackground(Palette.BACKGROUND_COLOR);
                                    startButtonPanel.add(startButton);
                                    settingsPanel.add(startButtonPanel);
                                });
                            }
                        }
                    }
                }
            });
        }

        setBackground(Palette.HIGHLIGHT_COLOR);
        setBorder(new LineBorder(Palette.BORDER_COLOR));

        this.setVisible(true);
    }
}
