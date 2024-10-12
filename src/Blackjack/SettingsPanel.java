package Blackjack;

import Menus.Palette;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private JSpinner minimumSpinner, maximumSpinner, intervalSpinner, deckAmountSpinner;

    public SettingsPanel(){
        setBackground(Palette.BACKGROUND_COLOR);

        JLabel minimumLabel = new JLabel("Minimal bet");
        minimumLabel.setFont(Palette.DEFAULT_FONT);
        minimumLabel.setBackground(Palette.BACKGROUND_COLOR);
        minimumLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

        JLabel maximumLabel = new JLabel("Maximum bet");
        maximumLabel.setFont(Palette.DEFAULT_FONT);
        maximumLabel.setBackground(Palette.BACKGROUND_COLOR);
        maximumLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

        JLabel intervalLabel = new JLabel("Bet interval");
        intervalLabel.setFont(Palette.DEFAULT_FONT);
        intervalLabel.setBackground(Palette.BACKGROUND_COLOR);
        intervalLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

        JLabel deckAmountLabel = new JLabel("Amount of decks in a shoe");
        deckAmountLabel.setFont(Palette.DEFAULT_FONT);
        deckAmountLabel.setBackground(Palette.BACKGROUND_COLOR);
        deckAmountLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

        minimumSpinner = new JSpinner(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
        minimumSpinner.setBackground(Palette.BACKGROUND_COLOR);

        maximumSpinner = new JSpinner(new SpinnerNumberModel(5_000, 1, Integer.MAX_VALUE, 1));
        maximumSpinner.setBackground(Palette.BACKGROUND_COLOR);

        intervalSpinner = new JSpinner(new SpinnerNumberModel(50, 1, Integer.MAX_VALUE, 1));
        intervalSpinner.setBackground(Palette.BACKGROUND_COLOR);

        deckAmountSpinner = new JSpinner(new SpinnerNumberModel(6, 1, Integer.MAX_VALUE, 1));
        intervalSpinner.setBackground(Palette.BACKGROUND_COLOR);

        GridLayout layout = new GridLayout(10, 1);
        setLayout(layout);

        JPanel settingPanel = new JPanel(new GridLayout(1, 2));
        settingPanel.setBackground(Palette.BACKGROUND_COLOR);
        settingPanel.add(maximumLabel);
        settingPanel.add(maximumSpinner);
        add(settingPanel);

        settingPanel = new JPanel(new GridLayout(1, 2));
        settingPanel.setBackground(Palette.BACKGROUND_COLOR);
        settingPanel.add(minimumLabel);
        settingPanel.add(minimumSpinner);
        add(settingPanel);

        settingPanel = new JPanel(new GridLayout(1, 2));
        settingPanel.setBackground(Palette.BACKGROUND_COLOR);
        settingPanel.add(intervalLabel);
        settingPanel.add(intervalSpinner);
        add(settingPanel);

        settingPanel = new JPanel(new GridLayout(1, 2));
        settingPanel.setBackground(Palette.BACKGROUND_COLOR);
        settingPanel.add(deckAmountLabel);
        settingPanel.add(deckAmountSpinner);
        add(settingPanel);
    }

    public int getTableMinimum(){
        return (int) minimumSpinner.getValue();
    }

    public int getTableMaximum(){
        return (int) maximumSpinner.getValue();
    }

    public int getBetInterval(){
        return (int) intervalSpinner.getValue();
    }

    public int getDeckAmount(){
        return (int) deckAmountSpinner.getValue();
    }
}
