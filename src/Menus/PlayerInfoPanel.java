package Menus;

import Logic.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class PlayerInfoPanel extends JPanel {
    private PlayerModel playerModel;
    private JLabel nameLabel;

    public PlayerInfoPanel(JButton returnButton){
        this.setLayout(new BorderLayout());

        nameLabel = new JLabel();

        this.add(nameLabel, BorderLayout.LINE_START);
        this.add(returnButton, BorderLayout.LINE_END);
    }

    public void setPlayerModel(PlayerModel playerModel){
        this.playerModel = playerModel;
        if(playerModel != null)
            nameLabel.setText(playerModel.getName());
        else
            nameLabel.setText("");
    }
}
