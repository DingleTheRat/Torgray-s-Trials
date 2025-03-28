package net.dinglezz.torgrays_trials.main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;

public class Sound {
    Clip clip;
    public HashMap<String, URL> soundLibrary = new HashMap<>();
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
        soundLibrary.put("Journey", getClass().getResource("/sound/journey.wav"));
        soundLibrary.put("Coin", getClass().getResource("/sound/coin.wav"));
        soundLibrary.put("Power Up", getClass().getResource("/sound/power_up.wav"));
        soundLibrary.put("Unlock", getClass().getResource("/sound/unlock.wav"));
        soundLibrary.put("Way", getClass().getResource("/sound/way.wav"));
        soundLibrary.put("Tech Geek", getClass().getResource("/sound/tech_geek.wav"));
        soundLibrary.put("Hit Monster", getClass().getResource("/sound/hit_monster.wav"));
        soundLibrary.put("Receive Damage", getClass().getResource("/sound/receive_damage.wav"));
        soundLibrary.put("Cursor", getClass().getResource("/sound/cursor.wav"));
        soundLibrary.put("Game Over", getClass().getResource("/sound/game_over.wav"));
        soundLibrary.put("Teleport", getClass().getResource("/sound/teleport.wav"));
    }

    public void getFile(String soundName) {
        try {
            if (soundLibrary.get(soundName) != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundLibrary.get(soundName));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                checkVolume();
            } else {
                System.out.println("Warning: \"" + soundName + "\" is not a valid sound.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play() {
        clip.start();
    }
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();
    }
    public void checkVolume() {
        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f; break;
            case 4: volume = 1f; break;
            case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
