import Menus.MainWindow;
import Blackjack.ImageHandler;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        ImageHandler.loadImages();
        SwingUtilities.invokeLater(new MainWindow());
    }
}