package Menus;

import Logic.PlayerModel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame implements Runnable, MainWindowInterface {

    private final static int DEFAULT_WIDTH = 1024, DEFAULT_HEIGHT = 800;

    private JPanel contentPanel;
    private PlayerModel playerModel;
    private GameSession currentGameSession = null;
    public MainWindow(PlayerModel playerModel){
        this.playerModel = playerModel;
    }

    @Override
    public void run(){
        getContentPane().setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(new PlayerInfoPanel(playerModel), BorderLayout.NORTH);

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        goToGameChoice();
    }

    @Override
    public void startGameSession(GameSession gameSession) {
        this.contentPanel.removeAll();
        this.contentPanel.add(gameSession.getGamePanel(), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    @Override
    public void goToGameChoice() {
        this.contentPanel.removeAll();
        this.contentPanel.add(new ModeChoicePanel(this), BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
