package Menus;

import Logic.PlayerModel;
import Modes.Blackjack.BlackjackGame;
import Modes.GameMode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class ModeChoicePanel extends JPanel {
    private PlayerChoicePanel playerChoicePanel;

    private static GameMode[] gameModes = new GameMode[] {
            new BlackjackGame()
    };

    private HashMap<JButton, GameMode> buttonsAndModes = new HashMap<>();
    public ModeChoicePanel(MainWindow mainWindow, PlayerChoicePanel playerChoicePanel){
        for (GameMode gameMode : gameModes){
            JButton button = new JButton(gameMode.getName());
            this.add(button);
            buttonsAndModes.put(button, gameMode);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameMode gameMode = buttonsAndModes.get(button);
                    PlayerModel playerModel = playerChoicePanel.getSelectedPlayer();
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
        this.setVisible(true);
    }
}
