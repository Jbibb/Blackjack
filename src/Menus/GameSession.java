package Menus;

import Modes.GameMode;
import Modes.GamePanel;

import javax.swing.*;

public class GameSession {
    private GameMode gameMode;
    private JPanel gamePanel;
    public GameSession(GameMode gameMode, JPanel gamePanel){
        this.gameMode = gameMode;
        this.gamePanel = gamePanel;
    }

    public JPanel getGamePanel(){
        return gamePanel;
    }

    public void end(){
        ((GamePanel)gamePanel).end();
    }
}
