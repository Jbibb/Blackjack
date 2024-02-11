import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageHandler {
    private static HashMap<String, Image> images = new HashMap<>();

    public static void loadImages() throws IOException {
        for(File imageFile : new File("cardImages").listFiles()) {
            images.put(imageFile.getName().split(".png")[0], ImageIO.read(new FileInputStream(imageFile)));
        }
    }

    public static void scaleImages(int width, int height) {
        for(Map.Entry entry : images.entrySet()){
            entry.setValue(((Image)(entry.getValue())).getScaledInstance(width, height, Image.SCALE_SMOOTH));
        }
    }

    public static Image getImage(String imageName){
        return images.get(imageName);
    }
}
