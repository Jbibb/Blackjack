package Modes;

import javax.swing.*;

public abstract class GameMode {
    public GameMode(){}

    public abstract String getName();
    public abstract String getDescription();

    public abstract JPanel getGamePanel();
}
