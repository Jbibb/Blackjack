package Menus;

import Blackjack.ImageHandler;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    private Clip clip;

    public AudioPlayer(String soundName) {
        ClassLoader classLoader = ImageHandler.class.getClassLoader();
        String soundPath = "sounds/" + soundName;
        URL resourceUrl = classLoader.getResource(soundPath);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(resourceUrl);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException e) {
            System.out.println("error with sound " + soundName);
        }
    }

    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void playOnce(){
        if(clip != null)
            clip.loop(1);
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
