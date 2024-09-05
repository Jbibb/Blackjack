package Modes.Blackjack;
import Modes.GameMode;

import javax.swing.*;
import Logic.PlayerModel;

public class BlackjackGame extends GameMode {
    private StrategyTableModel strategyTableModel;

    public BlackjackGame() {}

    public void setStrategyTableModel(StrategyTableModel strategyTableModel){
        this.strategyTableModel = strategyTableModel;
    }

    @Override
    public String getName(){
        return "BlackJack";
    }
    @Override
    public String getDescription(){
        return "You draw cards and then do stuff";
    }


    @Override
    public JPanel getGamePanel() {
        return new UIPanel(this.playerModel, strategyTableModel);
    }
}
