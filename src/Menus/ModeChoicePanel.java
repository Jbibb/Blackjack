package Menus;

import Modes.Blackjack.BlackjackGame;
import Modes.GameMode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class ModeChoicePanel extends JPanel {

    private static GameMode[] gameModes = new GameMode[] {
            new BlackjackGame()
    };

    private HashMap<JButton, GameMode> buttonsAndModes = new HashMap<>();
    public ModeChoicePanel(JFrame window){
        for (GameMode gameMode : gameModes){
            JButton button = new JButton(gameMode.getName());
            this.add(button);
            buttonsAndModes.put(button, gameMode);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameMode gameMode = buttonsAndModes.get(button);
                    ((MainWindowInterface)(window)).startGameSession(new GameSession(gameMode, gameMode.getGamePanel()));
                }
            });
        }
        this.setVisible(true);
    }
}
