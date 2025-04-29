package net.dinglezz.torgrays_trials.main;

import net.dinglezz.torgrays_trials.tile.MapHandler;
import org.json.JSONException;
import org.json.JSONObject;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Sound {
    Clip clip;
    public HashMap<String, URL> soundLibrary = new HashMap<>();
    FloatControl floatControl;
    int volumeScale = 3;
    float volume;

    public Sound() {
        // Music
        soundLibrary.put("Tech Geek", getClass().getResource("/sound/music/tech_geek.wav"));
        soundLibrary.put("Umbral Force", getClass().getResource("/sound/music/umbral_force.wav"));
        soundLibrary.put("Coin Toss", getClass().getResource("/sound/music/coin_toss.wav"));
        soundLibrary.put("Gloom Over Torgray", getClass().getResource("/sound/music/gloom_over_torgray.wav"));
        // Unused
        soundLibrary.put("Journey", getClass().getResource("/sound/music/unused/journey.wav")); // By AWESOME_DRAGON
        soundLibrary.put("Dark Mystery", getClass().getResource("/sound/music/unused/dark_mystery.wav")); // By LHTD

        // SFX
        soundLibrary.put("Coin", getClass().getResource("/sound/sfx/coin.wav"));
        soundLibrary.put("Power Up", getClass().getResource("/sound/sfx/power_up.wav"));
        soundLibrary.put("Unlock", getClass().getResource("/sound/sfx/unlock.wav"));
        soundLibrary.put("Hit Monster", getClass().getResource("/sound/sfx/hit_monster.wav"));
        soundLibrary.put("Receive Damage", getClass().getResource("/sound/sfx/receive_damage.wav"));
        soundLibrary.put("Cursor", getClass().getResource("/sound/sfx/cursor.wav"));
        soundLibrary.put("Game Over", getClass().getResource("/sound/sfx/game_over.wav"));
        soundLibrary.put("Teleport", getClass().getResource("/sound/sfx/teleport.wav"));
        soundLibrary.put("Swing", getClass().getResource("/sound/sfx/swing.wav"));
        // Unused
        soundLibrary.put("Way", getClass().getResource("/sound/sfx/unused/way.wav"));
    }

    public void getFile(String soundName) {
        try {
            if (soundLibrary.get(soundName) != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundLibrary.get(soundName));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                checkVolume();
            } else {
                System.err.println("Warning: \"" + soundName + "\" is not a valid sfx.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void play() {
        if (clip != null) {
            clip.start();
        } else {
            System.err.println("Warning: No clip found to play");}
    }
    public void loop() {
        if ( clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Warning: No clip found to loop");
        }
    }
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
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
        floatControl.setValue(volume);
    }

    // Static Stuff
    public static Sound music = new Sound();
    public static Sound sfx = new Sound();

    public static void playMusic(String songName) {
        stopMusic();
        music.getFile(songName);
        music.play();
        music.loop();
    }
    public static void playMapMusic() {
        stopMusic();
        JSONObject currentMapFile = Main.game.mapHandler.mapFiles.get(Main.game.currentMap);

        try {
            playMusic(currentMapFile.getString("music"));
        } catch (JSONException jsonException) {
            currentMapFile = Main.game.mapHandler.mapFiles.get("Disabled");
            playMusic(currentMapFile.getString("music"));
        }
    }
    public static void stopMusic() {
        music.stop();
    }
    public static void playSFX(String sfxName) {
        sfx.getFile(sfxName);
        sfx.play();
    }
}
