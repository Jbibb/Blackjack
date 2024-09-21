package Modes.Blackjack;
import Modes.GameMode;

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
    public UIPanel getGamePanel(int width, int height) {
        return new UIPanel(this.playerModel, strategyTableModel, width, height);
    }
}
