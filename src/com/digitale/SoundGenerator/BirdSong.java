package com.digitale.SoundGenerator;

import java.util.Random;

import com.digitale.lullabyepro.AnimationView;
import com.digitale.lullabyepro.Lullabye;
import com.digitale.lullabyepro.SoundManager;

import android.util.Log;
/**
 * @author Richard Beech
 */
public class BirdSong {
	private String tag="BirdSong";

	/**
	 * @param currentVolume
	 */
	public void birdsong(AnimationView animationView,float currentVolume) {
		Random random = new Random();
		if (Lullabye.deepDebug) Log.d(tag,"entering birdsong");

		int RandomNumber = random.nextInt(Lullabye.getMscaleRandom());
		if (Lullabye.debug) Log.d(tag,"bird number is "+RandomNumber);

		// nightingale
		if ((RandomNumber >= 30) && (RandomNumber <= 39)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 2);
		}

		// thrush
		if ((RandomNumber >= 50) && (RandomNumber <= 59)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume);
		}
		// crow
		if ((RandomNumber >= 70) && (RandomNumber <= 79)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 6);
		}
		// cookoo
		if ((RandomNumber >= 10) && (RandomNumber <= 11)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume/2);

		}
		// background sounds
		RandomNumber = random.nextInt(Lullabye.getMscaleRandom() / 2);
		// Log.d(tag,"bird number is "+RandomNumber);

		// nightingale
		if ((RandomNumber >= 30) && (RandomNumber <= 39)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}
		// thrush
		if ((RandomNumber >= 50) && (RandomNumber <= 59)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 8);
		}
		// crow
		if ((RandomNumber >= 70) && (RandomNumber <= 79)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 6);
			// cookoo
		}
		if ((RandomNumber >= 10) && (RandomNumber <= 11)) {
			SoundManager.playSound(RandomNumber, 1, currentVolume / 6);
		}

	}

}
