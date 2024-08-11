package Modes.Blackjack;

import Modes.GameMode;

import javax.swing.*;

public class BlackjackGame extends GameMode {

    public BlackjackGame() {

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
        return new UIPanel(5_000);
    }
}
