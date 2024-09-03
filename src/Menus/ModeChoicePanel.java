package Menus;

import Logic.PlayerModel;
import Modes.Blackjack.BlackjackGame;
import Modes.GameMode;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ModeChoicePanel extends JPanel {
    private PlayerListPanel playerListPanel;

    private static GameMode[] gameModes = new GameMode[] {
            new BlackjackGame()
    };

    private HashMap<JButton, GameMode> buttonsAndModes = new HashMap<>();
    public ModeChoicePanel(MainWindow mainWindow, PlayerListPanel playerListPanel){
        for (GameMode gameMode : gameModes){
            JButton button = new JButton(gameMode.getName());
            button.setPreferredSize(new Dimension(100, 50));
            add(button);
            button.setFocusable(false);
            buttonsAndModes.put(button, gameMode);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameMode gameMode = buttonsAndModes.get(button);
                    PlayerModel playerModel = playerListPanel.getSelectedPlayer();
                    if(playerModel != null) {
                        mainWindow.setPlayerModel(playerModel);
                        gameMode.setPlayerModel(playerModel);

                        GameSession gameSession = new GameSession(gameMode, gameMode.getGamePanel());
                        mainWindow.setCurrentGameSession(gameSession);
                        mainWindow.startGameSession(gameSession);
                    }
                }
            });
        }

        setBackground(Palette.ALT_BACKGROUND_COLOR);

        this.setVisible(true);
    }
}
