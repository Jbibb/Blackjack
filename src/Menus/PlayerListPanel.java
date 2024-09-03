package Menus;

import Logic.PlayerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerListPanel extends JPanel {
    private JTextField newNameField;
    private JList jList;
    public PlayerListPanel(){
        PlayerModel.loadSavedPlayers();

        jList = new JList();
        DefaultListModel<PlayerModel> listModel = new DefaultListModel<>();
        DefaultListCellRenderer cellRenderer = new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                //super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                JPanel panel = new JPanel();
                if(!isSelected)
                    panel.setBackground(index % 2 == 0 ? new Color(34, 139, 34) : new Color(45, 173, 23));
                else
                    panel.setBackground(new Color(170, 245, 83));
                panel.setLayout(new GridLayout(0, 3));

                PlayerModel playerModel = (PlayerModel) value;
                JLabel nameLabel = new JLabel(playerModel.getName());
                JLabel moneyLabel = new JLabel(playerModel.getMoney() + "$");
                JLabel changeLabel = new JLabel((playerModel.getChangePercentage() > 0 ? "+" : "") + playerModel.getChangePercentage() + "%");

                Font font = new Font("Verdana", Font.BOLD, 14);

                nameLabel.setFont(font);
                moneyLabel.setFont(font);
                changeLabel.setFont(font);

                nameLabel.setForeground(Palette.DEFAULT_FONT_COLOR);
                moneyLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

                if(playerModel.getChangePercentage() > 0)
                    changeLabel.setForeground(Palette.POSITIVE_FONT_COLOR);
                else if(playerModel.getChangePercentage() < 0)
                    changeLabel.setForeground(Palette.NEGATIVE_FONT_COLOR);
                else
                    changeLabel.setForeground(Palette.DEFAULT_FONT_COLOR);

                panel.add(nameLabel);
                nameLabel.setHorizontalAlignment(LEFT);

                panel.add(moneyLabel);
                moneyLabel.setHorizontalAlignment(CENTER);

                panel.add(changeLabel);
                changeLabel.setHorizontalAlignment(RIGHT);


                return panel;
            }
        };
        for(PlayerModel playerModel : PlayerModel.players) {
            listModel.addElement(playerModel);
        }
        jList.setModel(listModel);
        jList.setCellRenderer(cellRenderer);

        add(jList);

        newNameField = new JTextField();
        newNameField.setPreferredSize(new Dimension(100, 50));
        add(newNameField);

        JButton addButton = new JButton("Add new player");
        addButton.setFocusable(false);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern pattern = Pattern.compile("^ *$");
                Matcher matcher = pattern.matcher(newNameField.getText());
                if(!matcher.find()) {
                    PlayerModel playerModel = new PlayerModel(newNameField.getText(), 5_000);
                    PlayerModel.players.add(playerModel);
                    listModel.addElement(playerModel);
                    PlayerModel.savePlayers();
                }
            }
        });
        add(addButton);

        setBackground(Palette.ALT_BACKGROUND_COLOR);
    }

    public PlayerModel getSelectedPlayer(){
        return (PlayerModel) jList.getSelectedValue();
    }
}
