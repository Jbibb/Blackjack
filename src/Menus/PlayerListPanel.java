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
                    panel.setBackground(index % 2 == 0 ? Palette.HIGHLIGHT_COLOR : Palette.ALT_HIGHLIGHT_COLOR);
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
        jList.setBackground(Palette.BACKGROUND_COLOR);


        setLayout(new GridLayout(2,1));

        JScrollPane scrollPane = new JScrollPane(jList);
        scrollPane.setBorder(BorderFactory.createLineBorder(Palette.BORDER_COLOR));
        add(scrollPane);
        jList.setFixedCellWidth(700);

        JPanel addPanel = new JPanel();
        addPanel.setBackground(Palette.ALT_BACKGROUND_COLOR);

        newNameField = new JTextField();
        newNameField.setPreferredSize(new Dimension(100, 50));
        addPanel.add(newNameField);

        JButton addButton = new JButton("Add");
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
        addPanel.add(addButton);

        JPanel removePanel = new JPanel();
        removePanel.setBackground(Palette.ALT_BACKGROUND_COLOR);

        JButton removeButton = new JButton("Remove");
        removeButton.setFocusable(false);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(jList.getSelectedValue() != null) {
                    listModel.removeElement(jList.getSelectedValue());
                    PlayerModel.players.remove(jList.getSelectedValue());
                    PlayerModel.savePlayers();
                }
            }
        });
        removePanel.add(removeButton);
        setBorder(BorderFactory.createLineBorder(Palette.BORDER_COLOR));
        JPanel containerPanel = new JPanel(new GridLayout(1, 2));
        containerPanel.add(removePanel);
        containerPanel.add(addPanel);
        containerPanel.setBackground(Palette.ALT_BACKGROUND_COLOR);
        add(containerPanel);
    }

    public PlayerModel getSelectedPlayer(){
        return (PlayerModel) jList.getSelectedValue();
    }
}
