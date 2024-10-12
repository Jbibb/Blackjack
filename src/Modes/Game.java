package Modes;

import Logic.PlayerModel;
import Blackjack.UIPanel;

import javax.swing.*;

public abstract class Game {
    protected PlayerModel playerModel;
    public Game(){}
    public void setPlayerModel(PlayerModel playerModel){
        this.playerModel = playerModel;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract UIPanel getGamePanel(int width, int height);
    public abstract JPanel getSettingsPanel();
}
