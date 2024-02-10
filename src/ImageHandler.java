import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ImageHandler {
    private static HashMap<String, BufferedImage> bufferedImages = new HashMap<>();

    public static void loadImages() throws IOException {
        for(File imageFile : new File("cardImages").listFiles()) {
            bufferedImages.put(imageFile.getName().split(".png")[0], ImageIO.read(new FileInputStream(imageFile)));
        }
    }

    public static BufferedImage getImage(String imageName){
        return bufferedImages.get(imageName);
    }
}
