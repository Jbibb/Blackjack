import Logic.PlayerModel;
import Menus.MainWindow;
import Modes.Blackjack.ImageHandler;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        ImageHandler.loadImages();
        SwingUtilities.invokeLater(new MainWindow());
    }
}