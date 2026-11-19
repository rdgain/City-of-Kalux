package utilities;

// change package name to fit your own package structure!

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

// SoundManager for Asteroids

public class SoundManager {

	static int nBullet = 0, nSBullet = 0;
	static boolean thrusting = false;

	// this may need modifying
	final static String path = "sounds/";

	// note: having too many clips open may cause
	// "LineUnavailableException: No Free Voices"
	public final static Clip huntedMode = getClip("huntedMode");
	public final static Clip map1M = getClip("map1");
	public final static Clip map2M = getClip("map2");
	public final static Clip map3M = getClip("map3");
	public final static Clip map4M = getClip("map4");
	public final static Clip map5M = getClip("map5");
	public final static Clip collectS = getClip("collect");
	public final static Clip dropS = getClip("drop");

	public final static Clip[] clips = {huntedMode, map1M, map2M, map3M, map4M, map5M, collectS, dropS};

	public static void main(String[] args) throws Exception {
		for (Clip clip : clips) {
			play(clip);
			Thread.sleep(1000);
		}
	}

	// methods which do not modify any fields

	public static void play(Clip clip) {
		clip.setFramePosition(0);
		clip.start();
	}

	public static void loop(Clip clip) {
		map1M.stop();
		map2M.stop();
		map3M.stop();
		map4M.stop();
		map5M.stop();
		clip.setFramePosition(0);
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}

	private static Clip getClip(String filename) {
		try (InputStream resource = new BufferedInputStream(
				ResourceManager.open("/" + path + filename + ".wav"));
			 AudioInputStream sample = AudioSystem.getAudioInputStream(resource)) {
			Clip clip = AudioSystem.getClip();
			clip.open(sample);
			return clip;
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
			throw new IllegalStateException("Unable to load audio file: " + filename + ".wav", e);
		}
	}

}
