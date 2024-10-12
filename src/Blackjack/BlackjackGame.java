package Blackjack;
import Modes.Game;

import javax.swing.*;

public class BlackjackGame extends Game {
    private StrategyTableModel strategyTableModel;
    private int minimumBet, maximumBet, betInterval, deckAmount;
    public BlackjackGame() {}

    public void setTableRules(int minimumBet, int maximumBet, int betInterval, int deckAmount){
        this.minimumBet = minimumBet;
        this.maximumBet = maximumBet;
        this.betInterval = betInterval;
        this.deckAmount = deckAmount;
    }

    public void setStrategyTableModel(StrategyTableModel strategyTableModel){
        this.strategyTableModel = strategyTableModel;
    }

    @Override
    public String getName(){
        return "Blackjack";
    }
    @Override
    public String getDescription(){
        return "You draw cards and then do stuff";
    }

    @Override
    public UIPanel getGamePanel(int width, int height) {
        return new UIPanel(this.playerModel, strategyTableModel, width, height, minimumBet, maximumBet, betInterval, deckAmount);
    }

    @Override
    public JPanel getSettingsPanel() {
        return new SettingsPanel();
    }
}
