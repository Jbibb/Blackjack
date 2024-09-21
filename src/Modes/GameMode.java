package Modes;

import Logic.PlayerModel;
import Modes.Blackjack.UIPanel;

public abstract class GameMode {
    protected PlayerModel playerModel;
    public GameMode(){}
    public void setPlayerModel(PlayerModel playerModel){
        this.playerModel = playerModel;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract UIPanel getGamePanel(int width, int height);
}
