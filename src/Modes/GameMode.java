package Modes;

import Logic.PlayerModel;

import javax.swing.*;

public abstract class GameMode {
    protected PlayerModel playerModel;
    public GameMode(){}
    public void setPlayerModel(PlayerModel playerModel){
        this.playerModel = playerModel;
    }

    public abstract String getName();
    public abstract String getDescription();

    public abstract JPanel getGamePanel();
}
