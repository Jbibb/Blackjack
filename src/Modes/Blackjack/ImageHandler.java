package Modes.Blackjack;

import Logic.CardImageNamesFetcher;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler {
    private static HashMap<String, Image> originalImages = new HashMap<>();
    private static HashMap<String, Image> scaledImages = new HashMap<>();

    public static void loadImages() {
        ClassLoader classLoader = ImageHandler.class.getClassLoader();
        for (String imageName : CardImageNamesFetcher.fetchCardImageNames()) {
            String imagePath = "images/cardImages/" + imageName;
            URL resourceUrl = classLoader.getResource(imagePath);
            if (resourceUrl != null) {
                imageName = imageName.split(".png")[0];
                originalImages.put(imageName, new ImageIcon(resourceUrl).getImage());
                scaledImages.put(imageName, new ImageIcon(resourceUrl).getImage());
            }
        }
    }

    public static void scaleImages(int width, int height) {
        if(originalImages.get("Nine_Clubs").getWidth(null) <= width){
            for(Map.Entry entry : scaledImages.entrySet()){
                entry.setValue(((originalImages.get(entry.getKey()))));
            }
        } else {
            for (Map.Entry entry : scaledImages.entrySet()) {
                entry.setValue(((originalImages.get(entry.getKey()))).getScaledInstance(width, height, Image.SCALE_SMOOTH));
            }
        }
    }

    public static Image getImage(String imageName){
        return scaledImages.get(imageName);
    }
}
