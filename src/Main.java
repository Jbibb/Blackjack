import Logic.PlayerModel;
import Menus.MainWindow;
import Modes.Blackjack.ImageHandler;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        PlayerModel playerModel = new PlayerModel("player1", 5_000);
        ImageHandler.loadImages();
        SwingUtilities.invokeLater(new MainWindow(playerModel));
    }
}