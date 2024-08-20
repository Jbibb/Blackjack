package Menus;

import Logic.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;

public class PlayerChoicePanel extends JPanel {
    private JTextField newNameField;
    private JList jList;
    public PlayerChoicePanel(){
        PlayerModel.loadSavedPlayers();

        jList = new JList();
        DefaultListModel<PlayerModel> listModel = new DefaultListModel<>();
        for(PlayerModel playerModel : PlayerModel.players) {
            listModel.addElement(playerModel);
        }
        jList.setModel(listModel);

        add(jList);

        newNameField = new JTextField();
        newNameField.setPreferredSize(new Dimension(100, 50));
        add(newNameField);

        JButton addButton = new JButton("Add new player");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newNameField.getText() != "") {
                    PlayerModel playerModel = new PlayerModel(newNameField.getText(), 5_000);
                    PlayerModel.players.add(playerModel);
                    listModel.addElement(playerModel);
                    PlayerModel.savePlayers();
                }
            }
        });
        add(addButton);
    }

    public PlayerModel getSelectedPlayer(){
        return (PlayerModel) jList.getSelectedValue();
    }
}
