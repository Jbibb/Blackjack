package Menus;

import Modes.Blackjack.UIPanel;
import Modes.GameMode;
import Modes.GamePanel;

public class GameSession {
    private GameMode gameMode;
    private UIPanel gamePanel;
    public GameSession(GameMode gameMode, UIPanel gamePanel){
        this.gameMode = gameMode;
        this.gamePanel = gamePanel;
    }

    public UIPanel getGamePanel(){
        return gamePanel;
    }

    public void end(){
        ((GamePanel)gamePanel).end();
    }
}
