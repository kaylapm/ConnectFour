package connectfour;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.initGame() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can the static variable SoundEffect.volume to SoundEffect.Volume.MUTE
 *    to mute the sound.
 *
 * For Eclipse, place the audio file under "src", which will be copied into "bin".
 */
public enum soundEffect {
    EAT_FOOD("audio/mouse click.wav"),
    BACKSOUND("audio/backsound.wav"),
    WIN("audio/violin-win-5-185128.wav"),
    DIE("audio/game-die.wav");

    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    private Clip clip;

    private soundEffect(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    static void initGame() {
        values();
    }
}