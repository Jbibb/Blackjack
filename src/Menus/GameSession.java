package Menus;

import Blackjack.UIPanel;
import Modes.Game;
import Modes.GamePanel;

public class GameSession {
    private Game game;
    private UIPanel gamePanel;
    public GameSession(Game game, UIPanel gamePanel){
        this.game = game;
        this.gamePanel = gamePanel;
    }

    public UIPanel getGamePanel(){
        return gamePanel;
    }

    public void end(){
        ((GamePanel)gamePanel).end();
    }
}
