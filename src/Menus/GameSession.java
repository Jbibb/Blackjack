package Menus;

import Modes.GameMode;

import javax.swing.*;

public class GameSession {
    private GameMode gameMode;
    private JPanel gamePanel;
    public GameSession(GameMode gameMode, JPanel gamePanel){
        this.gameMode = gameMode;
        this.gamePanel = gameMode.getGamePanel();
    }

    public JPanel getGamePanel(){
        return gamePanel;
    }
}
