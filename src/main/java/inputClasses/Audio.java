package inputClasses;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import stuntMan.MainMenu;

public class Audio {
	
	static Clip clip;
	public static boolean running = true;
	
	public static void play(String filePath) {
        final File file = new File(filePath); 
        try (final AudioInputStream in = AudioSystem.getAudioInputStream(file)) {
            clip = AudioSystem.getClip();
            clip.open(in);
            FloatControl ctrl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            ctrl.setValue(-30.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public static void setAudio() {
		MainMenu.labels.get("Audio Display").setText(!running+"");
		running = !running;
		if (!running) {
			clip.stop();
			return;
		}
		clip.start();
	}
}
