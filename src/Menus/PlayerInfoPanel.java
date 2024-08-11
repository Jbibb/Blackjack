package Menus;

import Logic.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel implements Observer {
    private PlayerModel playerModel;
    private JLabel nameLabel, moneyLabel;

    public PlayerInfoPanel(PlayerModel playerModel){
        this.playerModel = playerModel;
        this.setLayout(new BorderLayout());

        nameLabel = new JLabel();
        moneyLabel = new JLabel();

        nameLabel.setText(playerModel.getName());
        moneyLabel.setText(String.valueOf(playerModel.getMoney()));

        this.add(nameLabel, BorderLayout.LINE_START);
        this.add(moneyLabel, BorderLayout.LINE_END);
    }

    @Override
    public void fire() {
        moneyLabel.setText(String.valueOf(playerModel.getMoney()));
    }
}
